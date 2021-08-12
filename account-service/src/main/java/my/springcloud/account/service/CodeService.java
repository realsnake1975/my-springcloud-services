package my.springcloud.account.service;

import my.springcloud.common.model.CodeDetail;
import my.springcloud.common.model.account.AccountDetail;
import my.springcloud.common.model.account.AuthorityDetail;
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

    public List<CodeDetail> getAutorities() {
        return this.authorityService.findAll().stream()
                .filter(AuthorityDetail::getUseYn)
                .map(a -> new CodeDetail(String.valueOf(a.getAuthorityId()), a.getAuthorityName(), false))
                .collect(Collectors.toList());
    }

    public List<CodeDetail> getCompanies() {
        return this.accountService.findAll().stream()
                .map(AccountDetail::getCompanyName)
                .distinct()
                .map(cn -> new CodeDetail(cn, cn, false))
                .sorted(Comparator.comparing(CodeDetail::getCode))
                .collect(Collectors.toList());
    }

}
