package my.springcloud.account.model.mapper;

import my.springcloud.account.model.entity.Menu;
import my.springcloud.common.model.dto.account.MenuDto;
import my.springcloud.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface MenuMapper {

	Menu toEntity(MenuDto dto);

	MenuDto toDto(Menu entity);

}
