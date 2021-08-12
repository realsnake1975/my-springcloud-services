package my.springcloud.account.model.mapper;

import my.springcloud.account.model.aggregate.Account;
import my.springcloud.common.model.dto.account.AccountDto;
import my.springcloud.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface AccountMapper {

	Account toEntity(AccountDto dto);

	AccountDto toDto(Account entity);

}
