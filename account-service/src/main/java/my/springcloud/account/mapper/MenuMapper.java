package my.springcloud.account.mapper;

import org.mapstruct.Mapper;

import my.springcloud.account.domain.entity.Menu;
import my.springcloud.common.model.account.MenuDetail;
import my.springcloud.config.mapstruct.MapstructConfig;

@Mapper(config = MapstructConfig.class)
public interface MenuMapper {

	MenuDetail toDto(Menu entity);

}
