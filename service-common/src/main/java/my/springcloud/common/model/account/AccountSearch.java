package my.springcloud.common.model.account;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountSearch implements Serializable {

	// 검색조건 정의
	private final String searchCondition;
	private final String searchKeyword;
	private final List<String> searchType;

}
