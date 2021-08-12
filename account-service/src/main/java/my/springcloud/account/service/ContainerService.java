package my.springcloud.account.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lguplus.homeshoppingmoa.common.api.OrganizedProductApiClient;
import my.springcloud.common.model.dto.organizedproduct.ContainerCreateDto;
import my.springcloud.common.model.dto.organizedproduct.ContainerDto;
import my.springcloud.common.model.dto.organizedproduct.ContainerModifyDto;
import my.springcloud.common.wrapper.CommonModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContainerService {

    private final OrganizedProductApiClient apiClient;
    private final ObjectMapper objectMapper;

    public CommonModel<ContainerDto> createContainer(ContainerCreateDto containerCreateDto) {
        CommonModel<ContainerDto> cm = apiClient.createContainer(containerCreateDto);
        cm.getResult().convertXss();
        return cm;
    }

    public CommonModel<ContainerDto> findContainer(long id) {
        CommonModel<ContainerDto> cm = apiClient.findContainer(id);
        cm.getResult().convertXss();
        return cm;
    }

    public CommonModel findContainers(String searchCondition, String searchKeyword, List<String> searchStatus, Integer page, Integer size, String sort) {
        CommonModel<List<ContainerDto>> cm =
                objectMapper.convertValue(
                        apiClient.findContainers(searchCondition, searchKeyword, searchStatus, page, size, sort),
                        new TypeReference<CommonModel<List<ContainerDto>>>() {
                        });
        cm.getResult().forEach(ContainerDto::convertXss);
        return cm;
    }

    public CommonModel findContainers(String searchKeyword, List<String> searchType, Integer page, Integer size, String sort) {
        CommonModel<List<ContainerDto>> cm =
                objectMapper.convertValue(
                        apiClient.findContainers(searchKeyword, searchType, page, size, sort),
                        new TypeReference<CommonModel<List<ContainerDto>>>() {
                        });
        cm.getResult().forEach(ContainerDto::convertXss);
        return cm;
    }

    public CommonModel<ContainerDto> modifyContainer(long id, ContainerModifyDto containerModifyDto) {
        CommonModel<ContainerDto> cm = apiClient.modifyContainer(id, containerModifyDto);
        cm.getResult().convertXss();
        return cm;
    }
}
