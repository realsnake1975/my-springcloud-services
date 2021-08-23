package my.springcloud.account.mapper;

import org.mapstruct.Mapper;

import my.springcloud.account.domain.entity.Authority;
import my.springcloud.common.model.account.AuthorityDetail;
import my.springcloud.config.mapstruct.MapstructConfig;

@Mapper(config = MapstructConfig.class)
public interface AuthorityMapper {

	Authority toEntity(AuthorityDetail dto);

	AuthorityDetail toDto(Authority entity);

}
