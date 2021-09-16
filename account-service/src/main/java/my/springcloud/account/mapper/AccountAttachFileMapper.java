package my.springcloud.account.mapper;

import org.mapstruct.Mapper;

import my.springcloud.account.domain.entity.AccountAttachFile;
import my.springcloud.common.model.account.AccountAttachFileDetail;
import my.springcloud.config.mapstruct.MapstructConfig;

@Mapper(config = MapstructConfig.class)
public interface AccountAttachFileMapper {

	AccountAttachFile toEntity(AccountAttachFileDetail dto);

	AccountAttachFileDetail toDto(AccountAttachFile entity);

}
