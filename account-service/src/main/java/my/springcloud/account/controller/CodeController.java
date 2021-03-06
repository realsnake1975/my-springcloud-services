package my.springcloud.account.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.service.CodeService;
import my.springcloud.common.model.CodeDetail;
import my.springcloud.common.utils.CodeUtils;

@Tag(name = "공통코드 조회 API", description = "")
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/v1/codes", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class CodeController {

	private final CodeService service;

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "계정 상태 공통코드 조회",
		description = "계정 상태 공통코드 목록을 조회한다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CodeDetail.class))))
		}
	)
	@GetMapping(value = "/account-status")
	public ResponseEntity getAccountStatusType() {
		return ResponseEntity.ok(CodeUtils.getAccountStatusType());
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "소속회사 공통코드 조회",
		description = "소속회사 공통코드 목록을 조회한다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CodeDetail.class))))
		}
	)
	@GetMapping(value = "/company")
	public ResponseEntity getCompanies() {
		return ResponseEntity.ok(this.service.getCompanies());
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "권한 유형 공통코드 조회",
		description = "권한 유형 공통코드 목록을 조회한다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CodeDetail.class))))
		}
	)
	@GetMapping(value = "/authority")
	public ResponseEntity getAutorities() {
		return ResponseEntity.ok(this.service.getAutorities());
	}

}
