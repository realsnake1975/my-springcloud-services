package my.springcloud.common.model.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AccountSearch implements Serializable {

    // 검색조건 정의
    private final String searchCondition;
    private final String searchKeyword;
    private final List<String> searchType;

}
