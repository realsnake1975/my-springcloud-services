package my.springcloud.account.controller;

import my.springcloud.common.logging.CustomLogger;
import my.springcloud.common.logging.SubSvcClassType;
import my.springcloud.common.logging.SvcClassType;
import my.springcloud.common.logging.SvcType;
import my.springcloud.common.model.auth.LoginCheck;
import my.springcloud.common.model.auth.PasswordCheck;
import my.springcloud.common.model.auth.PasswordUpdate;
import my.springcloud.common.sec.model.CustomUserDetails;
import my.springcloud.config.swagger.OpenApiConfig;
import my.springcloud.account.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 API", description = "")
@Slf4j
@RequestMapping(value = "/opr/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "로그인 (완료)",
            description = "로그인 아이디와 비밀번호로 로그인한다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H28)
    @PostMapping("/auth/login")
    public ResponseEntity login(@RequestBody LoginCheck dto) {
        return ResponseEntity.ok(this.authService.login(dto));
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "SMS 인증번호 요청 (완료)",
            description = "인증토큰에 해당하는 사용자의 핸드폰에 SMS 인증번호를 발송한다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Boolean.class)))
            }
    )
    @CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H29)
    @PostMapping("/auth/request-sms-otp")
    public ResponseEntity requestSmsOtp(@RequestHeader("x-auth-token") String authToken) {
        return ResponseEntity.ok(this.authService.requestSmsOtp(authToken));
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "토큰 갱신 (완료)",
            description = "토큰을 갱신한다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Boolean.class)))
            }
    )
    @PostMapping("/auth/refresh")
    public ResponseEntity refreshAccessToken(@RequestHeader("x-refresh-token") String refreshToken) {
        return ResponseEntity.ok(this.authService.refreshAccessToken(refreshToken));
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "비밀번호 변경 (완료)",
            description = "비밀번호를 변경한다.",
            security = {
                    @SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Boolean.class)))
            }
    )
    @CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H31)
    @PutMapping("/accounts/change-password")
    public <U extends UserDetails> ResponseEntity changePassword(@AuthenticationPrincipal U principal, @RequestBody PasswordUpdate dto) {
        return ResponseEntity.ok(this.authService.changePassword((CustomUserDetails) principal, dto));
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "비밀번호 확인 (완료)",
            description = "비밀번호를 확인한다.",
            security = {
                    @SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Boolean.class)))
            }
    )
    @PostMapping("/accounts/check-password")
    public <U extends UserDetails> ResponseEntity checkPassword(@AuthenticationPrincipal U principal, @RequestBody PasswordCheck dto) {
        return ResponseEntity.ok(this.authService.checkPassword((CustomUserDetails) principal, dto));
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "리컨펌 토큰 체크 (테스트)",
            description = "리컨펌 토큰을 확인한다.",
            security = {
                    @SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Boolean.class)))
            }
    )
    @PostMapping("/accounts/check-reconfirm-token")
    public <U extends UserDetails> ResponseEntity checkReconfirmToken(@AuthenticationPrincipal U principal, @RequestHeader("Reconfirm-Token") String reconfirmToken) {
        return ResponseEntity.ok(this.authService.checkReconfirmToken((CustomUserDetails) principal, reconfirmToken));
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "로그아웃 (완료)",
            description = "로그아웃",
            security = {
                    @SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Boolean.class)))
            }
    )
    @PostMapping("/logout")
    @Deprecated
    public ResponseEntity logout() {
        return ResponseEntity.ok(this.authService.logout());
    }

}
