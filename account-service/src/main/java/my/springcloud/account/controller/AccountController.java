package my.springcloud.account.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import my.springcloud.common.model.account.AccountAttachFileDetail;
import my.springcloud.common.model.account.AccountCreate;
import my.springcloud.common.model.account.AccountDetail;
import my.springcloud.common.model.account.AccountModify;
import my.springcloud.config.swagger.OpenApiConfig;

@Tag(name = "?????? API", description = "")
@Slf4j
@RequestMapping(value = "/v1/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "?????? ??????",
		description = "????????? ????????????.",
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
		summary = "?????? ?????? ??????",
		description = "?????? ????????? ????????????.",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		parameters = {
			@Parameter(name = "id", in = ParameterIn.PATH, description = "?????? ????????????", schema = @Schema(type = "integer", format = "int64"))
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
		summary = "?????? ?????? ??????",
		description = "?????? ????????? ????????????.",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		parameters = {
			@Parameter(name = "searchCondition", in = ParameterIn.QUERY, description = "????????????", array = @ArraySchema(schema = @Schema(type = "string", allowableValues = { "", "accountName", "username", "companyName" }))),
			@Parameter(name = "searchKeyword", in = ParameterIn.QUERY, description = "?????????", schema = @Schema(type = "string")),
			@Parameter(name = "page", in = ParameterIn.QUERY, description = "????????? ??????", example = "0", schema = @Schema(type = "integer")),
			@Parameter(name = "size", in = ParameterIn.QUERY, description = "????????? ?????? ?????????", example = "10", schema = @Schema(type = "integer")),
			@Parameter(name = "sort", in = ParameterIn.QUERY, description = "?????? ??????(,asc|desc)", array = @ArraySchema(schema = @Schema(type = "string")))
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AccountDetail.class))))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H36)
	@GetMapping(value = "")
	public ResponseEntity find(AccountSpec spec, @PageableDefault(sort = "accountId", direction = Sort.Direction.DESC) Pageable pageable) {
		// https://yoonbing9.tistory.com/38
		// ????????? ?????? ?????? ????????? ???????????? @SortDefault ??????
		// @PageableDefault(page = 0, size = 10) @SortDefault.SortDefaults({ @SortDefault(sort = "regDt", direction = Sort.Direction.ASC) }) Pageable pageable) {
		return ResponseEntity.ok(this.accountService.find(spec, pageable));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "?????? ??????",
		description = "????????? ????????????.",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		parameters = {
			@Parameter(name = "id", in = ParameterIn.PATH, description = "?????? ????????????", schema = @Schema(type = "integer", format = "int64"))
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
		summary = "?????? ??????",
		description = "????????? ????????????.",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		parameters = {
			@Parameter(name = "ids", in = ParameterIn.QUERY, description = "?????? ???????????????", array = @ArraySchema(schema = @Schema(type = "integer", format = "int64")))
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success, ????????? ?????? ?????????????????? ????????????.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Long.class))))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H34)
	@DeleteMapping(value = "")
	public <U extends UserDetails> ResponseEntity remove(@AuthenticationPrincipal U principal, @RequestParam List<Long> ids) {
		return ResponseEntity.ok(this.accountService.remove(principal, ids));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "?????? ?????? ??????",
		description = "????????? ???????????? ????????????.",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		parameters = {
			@Parameter(name = "username", in = ParameterIn.PATH, description = "?????? ?????????", schema = @Schema(type = "string"))
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Boolean.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H43)
	@GetMapping(value = "/check-duplicate/{username}")
	public ResponseEntity checkDuplicate(@PathVariable String username) {
		return ResponseEntity.ok(this.accountService.checkDuplicate(username));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "?????? ??????",
		description = "????????? ????????????.",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		parameters = {
			@Parameter(name = "ids", in = ParameterIn.QUERY, description = "?????? ???????????????", array = @ArraySchema(schema = @Schema(type = "integer", format = "int64")))
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success, ????????? ?????? ?????????????????? ????????????.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Long.class))))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H49)
	@PutMapping("/block")
	public <U extends UserDetails> ResponseEntity blockAccounts(@AuthenticationPrincipal U principal, @RequestParam List<Long> ids) {
		return ResponseEntity.ok(this.accountService.blockAccounts(principal, ids));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "?????? ??????",
		description = "????????? ????????????.",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		parameters = {
			@Parameter(name = "ids", in = ParameterIn.QUERY, description = "?????? ???????????????", array = @ArraySchema(schema = @Schema(type = "integer", format = "int64")))
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success, ????????? ?????? ?????????????????? ????????????.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Long.class))))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H50)
	@PutMapping("/approve")
	public <U extends UserDetails> ResponseEntity approveAccounts(@AuthenticationPrincipal U principal, @RequestParam List<Long> ids) {
		return ResponseEntity.ok(this.accountService.approveAccounts(principal, ids));
	}

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "?????? ?????? & ???????????? ??????",
		description = "????????? ???????????? ??????????????? ????????????.",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = AccountDetail.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H32)
	@PostMapping(value = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public <U extends UserDetails> ResponseEntity createAndSave(@AuthenticationPrincipal U principal
		, @RequestPart("accountCreateJsonString") String accountCreateJsonString
		, @RequestPart(value = "attachFiles", required = false) MultipartFile[] attachFiles) {
		return ResponseEntity.ok(this.accountService.createAndSave(principal, accountCreateJsonString, attachFiles));
	}

	@Operation(
		summary = "???????????? ????????????",
		description = "??????????????? ??????????????????.",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		parameters  = {
			@Parameter(name = "attachFileId", in = ParameterIn.PATH, required = true, description = "???????????? ?????????", schema = @Schema(type = "string"))
		}
	)
	@GetMapping(value = "/download/{attachFileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public <U extends UserDetails> ResponseEntity<Resource> download(@AuthenticationPrincipal U principal, @PathVariable String attachFileId) {
		AccountAttachFileDetail attachFile = this.accountService.getAttachFile(attachFileId);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(attachFile.getOrgName(), StandardCharsets.UTF_8).build());
		headers.setCacheControl("no-cache, no-store, must-revalidate");
		headers.setPragma("no-cache");
		headers.setExpires(0L);

		return ResponseEntity.ok()
			.headers(headers)
			.contentLength(attachFile.getSize())
			.contentType(MediaType.APPLICATION_OCTET_STREAM)
			.body(attachFile.getResource());
	}

	@Operation(
		summary = "????????? ??????",
		description = "????????? ??????",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		parameters  = {
			@Parameter(name = "attachFileId", in = ParameterIn.PATH, required = true, description = "???????????? ?????????", schema = @Schema(type = "string"))
		}
	)
	@GetMapping(value = "/view-image/{attachFileId}", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE } )
	public <U extends UserDetails> ResponseEntity<Resource> viewImage(@AuthenticationPrincipal U principal, @PathVariable String attachFileId) {
		AccountAttachFileDetail attachFile = this.accountService.getAttachFile(attachFileId);

		return ResponseEntity.ok()
			.contentLength(attachFile.getSize())
			.contentType(MediaType.parseMediaType(attachFile.getMime()))
			.body(attachFile.getResource());
	}

}
