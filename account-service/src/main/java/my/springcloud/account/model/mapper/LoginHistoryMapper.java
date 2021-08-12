package my.springcloud.account.model.mapper;

import my.springcloud.account.model.entity.LoginHistory;
import my.springcloud.common.model.dto.account.LoginHistoryDto;
import my.springcloud.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface LoginHistoryMapper {

	LoginHistory toEntity(LoginHistoryDto dto);

	LoginHistoryDto toDto(LoginHistory entity);

}
