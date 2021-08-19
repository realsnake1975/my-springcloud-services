package my.springcloud.account.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.service.MenuService;
import my.springcloud.common.logging.CustomLogger;
import my.springcloud.common.logging.SubSvcClassType;
import my.springcloud.common.logging.SvcClassType;
import my.springcloud.common.logging.SvcType;
import my.springcloud.common.model.account.MenuDetail;
import my.springcloud.config.swagger.OpenApiConfig;

@Tag(name = "메뉴 API", description = "")
@Slf4j
@RequestMapping(value = "/opr/v1/menus", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class MenuController {

	private final MenuService menuService;

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "메뉴 목록 조회(완료)",
		description = "메뉴 목록 조회",
		security = {
			@SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = MenuDetail.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H42)
	@GetMapping(value = "")
	public ResponseEntity findMenu() {
		return ResponseEntity.ok(this.menuService.findMenu());
	}
}
