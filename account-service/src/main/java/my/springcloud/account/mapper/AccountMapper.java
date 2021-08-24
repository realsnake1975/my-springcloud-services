package my.springcloud.account.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import my.springcloud.account.domain.aggregate.Account;
import my.springcloud.common.model.account.AccountDetail;
import my.springcloud.config.mapstruct.MapstructConfig;

@Mapper(config = MapstructConfig.class)
public interface AccountMapper {

	@Mapping(target = "publish", ignore = true)
	Account toEntity(AccountDetail dto);

	@Mapping(target = "loginHistories", ignore = true)
	AccountDetail toDto(Account entity);

}
