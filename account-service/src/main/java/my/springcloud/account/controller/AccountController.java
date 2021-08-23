package my.springcloud.account.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.domain.spec.AccountSpec;
import my.springcloud.account.service.AccountService;
import my.springcloud.common.logging.CustomLogger;
import my.springcloud.common.logging.SubSvcClassType;
import my.springcloud.common.logging.SvcClassType;
import my.springcloud.common.logging.SvcType;
import my.springcloud.common.model.account.AccountCreate;
import my.springcloud.common.model.account.AccountDetail;
import my.springcloud.common.model.account.AccountModify;
import my.springcloud.config.swagger.OpenApiConfig;

@Tag(name = "계정 API", description = "")
@Slf4j
@RequestMapping(value = "/opr/v1/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "계정 등록(완료)",
		description = "계정을 등록한다 <br />" +
			"{<br />" +
			"\"username\": \"username1\",<br />" +
			"\"password\": \"passsword123\",<br />" +
			"\"authority\": { <br />" +
			"\"authorityId\": 21<br />" +
			"},<br />" +
			"\"accountName\": \"admin2\",<br />" +
			"\"companyName\": \"plea\",<br />" +
			"\"phoneNumber\": \"01012345678\",<br />" +
			"\"email\": \"abc@test.com\"<br />" +
			"}",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = AccountDetail.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H32)
	@PostMapping(value = "")
	public <U extends UserDetails> ResponseEntity create(@AuthenticationPrincipal U principal, @RequestBody AccountCreate accountCreate) {
		return ResponseEntity.ok(this.accountService.create(principal, accountCreate));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "계정 단건 조회(완료)",
		description = "계정 단건 조회",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = AccountDetail.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H35)
	@GetMapping(value = "/{id}")
	public ResponseEntity find(@PathVariable long id) {
		return ResponseEntity.ok(this.accountService.find(id));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "계정 목록 조회(완료)",
		description = "계정 목록 조회<br />" +
			"searchCondition: \"\", \"accountName\", \"username\", \"companyName\"<br />" +
			"searchType: authorityName중 선택된 항목들을 묶어서 ,로 구분시킨 String ex)\"운영자,슈퍼어드민,name\", \"운영자,슈퍼어드민\" etc...",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		parameters = {
			@Parameter(name = "searchCondition", in = ParameterIn.QUERY, required = false, description = "검색조건", example = ""),
			@Parameter(name = "searchKeyword", in = ParameterIn.QUERY, required = false, description = "검색어", example = ""),
			@Parameter(name = "searchType", in = ParameterIn.QUERY, required = false, description = "유형", example = "", content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))),
			@Parameter(name = "page", in = ParameterIn.QUERY, required = false, description = "페이지 번호", example = ""),
			@Parameter(name = "size", in = ParameterIn.QUERY, required = false, description = "페이지 목록 사이즈", example = ""),
			@Parameter(name = "sort", in = ParameterIn.QUERY, required = false, description = "정렬 조건(sort=regDt,asc, username,asc, accountName,asc)", example = "")
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = AccountDetail.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H36)
	@GetMapping(value = "")
	public ResponseEntity find(AccountSpec spec,
		@PageableDefault(page = 0, size = 10) @SortDefault.SortDefaults({@SortDefault(sort = "regDt", direction = Sort.Direction.ASC)}) Pageable pageable) {
		return ResponseEntity.ok(this.accountService.find(spec, pageable));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "계정 수정(완료)",
		description = "계정을 수정한다<br />" +
			"{<br />" +
			"\"username\": \"username1\",<br />" +
			"\"password\": \"passsword123\",<br />" +
			"\"authority\": { <br />" +
			"\"authorityId\": 21<br />" +
			"},<br />" +
			"\"phoneNumber\": \"01012345678\",<br />" +
			"\"email\": \"abc@test.com\"<br />" +
			"}",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = AccountDetail.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H33)
	@PutMapping(value = "/{id}")
	public <U extends UserDetails> ResponseEntity modify(@AuthenticationPrincipal U principal, @PathVariable long id, @RequestBody AccountModify accountModify) {
		return ResponseEntity.ok(this.accountService.modify(principal, id, accountModify));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "계정 삭제(완료)",
		description = "계정을 삭제한다",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Boolean.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H34)
	@DeleteMapping(value = "")
	public ResponseEntity remove(@RequestParam List<Long> ids) {
		return ResponseEntity.ok(this.accountService.remove(ids));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "계정 중복 조회(완료)",
		description = "계정이 중복인지 확인한다",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Boolean.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H43)
	@GetMapping(value = "/check/{username}")
	public ResponseEntity checkDuplicate(@PathVariable String username) {
		return ResponseEntity.ok(this.accountService.checkDuplicate(username));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "계정 차단(완료)",
		description = "계정을 차단한다",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Boolean.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H49)
	@PutMapping("/block")
	public <U extends UserDetails> ResponseEntity blockAccounts(@AuthenticationPrincipal U principal, @RequestParam List<Long> ids) {
		return ResponseEntity.ok(this.accountService.blockAccounts(principal, ids));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "계정 승인(완료)",
		description = "계정을 승인한다",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Boolean.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H50)
	@PutMapping("/permit")
	public <U extends UserDetails> ResponseEntity permitAccounts(@AuthenticationPrincipal U principal, @RequestParam List<Long> ids) {
		return ResponseEntity.ok(this.accountService.permitAccounts(principal, ids));
	}

}
