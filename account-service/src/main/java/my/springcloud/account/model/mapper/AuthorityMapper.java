package my.springcloud.account.model.mapper;

import my.springcloud.account.model.aggregate.Authority;
import my.springcloud.common.model.dto.account.AuthorityDto;
import my.springcloud.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface AuthorityMapper {

	Authority toEntity(AuthorityDto dto);

	AuthorityDto toDto(Authority entity);

}
