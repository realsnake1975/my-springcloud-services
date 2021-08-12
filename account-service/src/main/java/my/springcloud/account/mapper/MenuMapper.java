package my.springcloud.account.mapper;

import my.springcloud.account.domain.entity.Menu;
import my.springcloud.common.model.account.MenuDetail;
import my.springcloud.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface MenuMapper {

	MenuDetail toDto(Menu entity);

}
