package my.springcloud.account.mapper;

import my.springcloud.account.domain.aggregate.Authority;
import my.springcloud.common.model.account.AuthorityDetail;
import my.springcloud.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface AuthorityMapper {

	Authority toEntity(AuthorityDetail dto);

	AuthorityDetail toDto(Authority entity);

}
