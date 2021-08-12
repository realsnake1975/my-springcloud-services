package my.springcloud.account.service;

import my.springcloud.common.model.dto.CodeDto;
import my.springcloud.common.model.dto.account.AccountDto;
import my.springcloud.common.model.dto.account.AuthorityDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CodeService {

    private final AuthorityService authorityService;
    private final AccountService accountService;

    public List<CodeDto> getAutorities() {
        return this.authorityService.findAll().stream()
                .filter(AuthorityDto::getUseYn)
                .map(a -> new CodeDto(String.valueOf(a.getAuthorityId()), a.getAuthorityName(), false))
                .collect(Collectors.toList());
    }

    public List<CodeDto> getCompanies() {
        return this.accountService.findAll().stream()
                .map(AccountDto::getCompanyName)
                .distinct()
                .map(cn -> new CodeDto(cn, cn, false))
                .sorted(Comparator.comparing(CodeDto::getCode))
                .collect(Collectors.toList());
    }

}
