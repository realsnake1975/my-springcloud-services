package my.springcloud.account.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
import my.springcloud.account.domain.spec.AuthoritySpec;
import my.springcloud.account.service.AuthorityService;
import my.springcloud.common.logging.CustomLogger;
import my.springcloud.common.logging.SubSvcClassType;
import my.springcloud.common.logging.SvcClassType;
import my.springcloud.common.logging.SvcType;
import my.springcloud.common.model.account.AccountDetail;
import my.springcloud.common.model.account.AuthorityDetail;
import my.springcloud.common.model.account.AuthorityHandle;
import my.springcloud.common.model.account.MenuDetail;
import my.springcloud.config.swagger.OpenApiConfig;

@Tag(name = "권한 API", description = "")
@Slf4j
@RequestMapping(value = "/v1/authorities", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class AuthorityController {

	private final AuthorityService authorityService;

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "권한 등록",
		description = "권한을 등록한다",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = AccountDetail.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H37)
	@PostMapping(value = "")
	public <U extends UserDetails> ResponseEntity create(@AuthenticationPrincipal U principal, @RequestBody AuthorityHandle authorityHandle) {
		return ResponseEntity.ok(this.authorityService.create(principal, authorityHandle));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "권한 단건 조회",
		description = "권한 상세를 조회한다.",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		parameters = {
			@Parameter(name = "id", in = ParameterIn.PATH, description = "권한 일련번호", schema = @Schema(type = "integer", format = "int64"))
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = AuthorityDetail.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H39)
	@GetMapping(value = "/{id}")
	public ResponseEntity find(@PathVariable long id) {
		return ResponseEntity.ok(this.authorityService.find(id));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "권한 목록 조회",
		description = "권한 목록을 조회한다.",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		parameters = {
			@Parameter(name = "page", in = ParameterIn.QUERY, description = "페이지 번호", example = "0", schema = @Schema(type = "integer")),
			@Parameter(name = "size", in = ParameterIn.QUERY, description = "페이지 목록 사이즈", example = "10", schema = @Schema(type = "integer")),
			@Parameter(name = "sort", in = ParameterIn.QUERY, description = "정렬 조건(,asc|desc)", array = @ArraySchema(schema = @Schema(type = "string")))
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthorityDetail.class))))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H40)
	@GetMapping(value = "")
	public ResponseEntity find(AuthoritySpec spec, @PageableDefault(sort = "authorityId", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(this.authorityService.find(spec, pageable));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "권한 수정",
		description = "권한을 수정한다.",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		parameters = {
			@Parameter(name = "id", in = ParameterIn.PATH, description = "권한 일련번호", schema = @Schema(type = "integer", format = "int64"))
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = AuthorityDetail.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H38)
	@PutMapping(value = "/{id}")
	public <U extends UserDetails> ResponseEntity modify(@AuthenticationPrincipal U principal, @PathVariable long id, @RequestBody AuthorityHandle authorityHandle) {
		return ResponseEntity.ok(authorityService.modify(principal, id, authorityHandle));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "메뉴 목록 조회",
		description = "메뉴 목록을 조회한다.",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MenuDetail.class))))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H42)
	@GetMapping(value = "/menus")
	public ResponseEntity findMenus() {
		return ResponseEntity.ok(this.authorityService.findMenus());
	}

}
