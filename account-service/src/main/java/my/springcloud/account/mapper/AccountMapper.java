package my.springcloud.account.mapper;

import my.springcloud.account.domain.aggregate.Account;
import my.springcloud.common.model.account.AccountDetail;
import my.springcloud.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface AccountMapper {

	Account toEntity(AccountDetail dto);

    @Mapping(target = "loginHistories", ignore = true)
	AccountDetail toDto(Account entity);

}
