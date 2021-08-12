package my.springcloud.account.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lguplus.homeshoppingmoa.common.api.OrganizedProductApiClient;
import my.springcloud.common.model.dto.organizedproduct.AppMenuCreateDto;
import my.springcloud.common.model.dto.organizedproduct.AppMenuDto;
import my.springcloud.common.model.dto.organizedproduct.AppMenuModifyDto;
import my.springcloud.common.wrapper.CommonModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppMenuService {

    private final OrganizedProductApiClient organizedProductApiClient;
    private final ObjectMapper objectMapper;

    public CommonModel<AppMenuDto> createAppMenu(AppMenuCreateDto appMenuCreateDto) {
        CommonModel<AppMenuDto> cm = organizedProductApiClient.createAppMenu(appMenuCreateDto);
        cm.getResult().convertXss();
        return cm;
    }

    public CommonModel<AppMenuDto> findAppMenu(long id) {
        CommonModel<AppMenuDto> cm = organizedProductApiClient.findAppMenu(id);
        cm.getResult().convertXss();
        return cm;
    }

    public CommonModel findAppMenu(String searchCondition, String searchKeyword, List<String> searchStatus, Integer page, Integer size, String sort) {
        CommonModel<List<AppMenuDto>> cm =
                objectMapper.convertValue(
                        organizedProductApiClient.findAppMenu(searchCondition, searchKeyword, searchStatus, page, size, sort),
                        new TypeReference<CommonModel<List<AppMenuDto>>>() {
                        });
        cm.getResult().forEach(AppMenuDto::convertXss);
        return cm;
    }

    public Object modifyAppMenu(long id, AppMenuModifyDto appMenuModifyDto) {
        CommonModel<AppMenuDto> cm = organizedProductApiClient.modifyAppMenu(id, appMenuModifyDto);
        cm.getResult().convertXss();
        return cm;
    }

}
