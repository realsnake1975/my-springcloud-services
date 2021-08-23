package my.springcloud.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "CodeDetail", description = "공통코드 응답 DTO")
public class CodeDetail {

	@Schema(name = "code", description = "코드")
	private final String code;

	@Schema(name = "desc", description = "설명")
	private final String desc;

	@Schema(name = "defaultConfigYn", description = "기본 설정 여부")
	private final boolean defaultConfigYn;

}
