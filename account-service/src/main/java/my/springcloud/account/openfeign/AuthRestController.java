package my.springcloud.account.openfeign;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.service.AuthService;
import my.springcloud.common.logging.CustomLogger;
import my.springcloud.common.logging.SubSvcClassType;
import my.springcloud.common.logging.SvcClassType;
import my.springcloud.common.logging.SvcType;
import my.springcloud.common.model.auth.AuthCheck;

@Tag(name = "인증 API", description = "")
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AuthRestController {

	private final AuthService authService;

	@SuppressWarnings("rawtypes")
	@Operation(
		summary = "인증번호 확인 (완료)",
		description = "인증번호 확인 후 JWT(accessToken)를 반환한다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = String.class)))
		}
	)
	@CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H30)
	@PostMapping("/auth/check-otp")
	public ResponseEntity checkOtp(@RequestBody AuthCheck dto) {
		return ResponseEntity.ok(this.authService.authFinal(dto));
	}

}
