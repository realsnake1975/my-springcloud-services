package my.springcloud.common.model.account;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(name = "AccountSearch", description = "계정 검색 DTO")
public class AccountSearch implements Serializable {

	// 검색조건 정의
	private final String searchCondition;
	private final String searchKeyword;

}
