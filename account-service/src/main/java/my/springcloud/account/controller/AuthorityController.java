package my.springcloud.account.controller;

import my.springcloud.account.model.spec.AuthoritySpec;
import my.springcloud.account.service.AuthorityService;
import my.springcloud.common.logging.CustomLogger;
import my.springcloud.common.logging.SubSvcClassType;
import my.springcloud.common.logging.SvcClassType;
import my.springcloud.common.logging.SvcType;
import my.springcloud.common.model.dto.account.AccountDto;
import my.springcloud.common.model.dto.account.AuthorityDto;
import my.springcloud.common.model.dto.account.AuthorityHandleDto;
import my.springcloud.config.swagger.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "권한 API", description = "")
@Slf4j
@RequestMapping(value = "/opr/v1/authority", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class AuthorityController {

    private final AuthorityService authorityService;

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "권한 등록(완료)",
            description = "권한을 등록한다<br />" +
                    "{<br />" +
                        "\"authorityName\": \"name1\",<br />" +
                        "\"description\": \"설명2\", <br />" +
                        "\"readYn\": [\"false\", \"true\", \"true\", \"true\", \"false\", \"true\", \"true\", \"true\"],<br />" +
                        "\"controlYn\": [\"false\", \"false\", \"true\", \"true\", \"false\", \"true\", \"true\", \"true\"],<br />" +
                        "\"useYn\": \"true\"<br />" +
                    "}<br />" +
                    "각각 메뉴에 대해서 true/false 를 위에서부터 순서대로 배열로 묶은값이 input입니다."
            ,
            security = {
                    @SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = AccountDto.class)))
            }
    )
    @CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H37)
    @PostMapping(value = "")
    public <U extends UserDetails> ResponseEntity create(@AuthenticationPrincipal U principal, @RequestBody AuthorityHandleDto authorityCreateDto) {
        return ResponseEntity.ok(this.authorityService.create(principal, authorityCreateDto));
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "권한 단건 조회(완료)",
            description = "권한 단건 조회",
            security = {
                    @SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = AuthorityDto.class)))
            }
    )
    @CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H39)
    @GetMapping(value = "/{id}")
    public ResponseEntity find(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        return ResponseEntity.ok(this.authorityService.find(userDetails, id));
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "권한 목록 조회(완료)",
            description = "권한 목록 조회",
            security = {
                    @SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
            },
            parameters = {
                    @Parameter(name = "page", in = ParameterIn.QUERY, required = false, description = "페이지 번호", example = ""),
                    @Parameter(name = "size", in = ParameterIn.QUERY, required = false, description = "페이지 목록 사이즈", example = ""),
                    // sort순서 일단 id기준
                    @Parameter(name = "sort", in = ParameterIn.QUERY, required = false, description = "정렬 조건(sort=authorityId,asc)", example = "")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = AccountDto.class)))
            }
    )
    @CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H40)
    @GetMapping(value = "")
    public ResponseEntity find(@AuthenticationPrincipal UserDetails userDetails, AuthoritySpec spec, @PageableDefault(page = 0, size = 10) @SortDefault.SortDefaults({
            @SortDefault(sort = "authorityId", direction = Sort.Direction.ASC),
    }) Pageable pageable) {
        return ResponseEntity.ok(this.authorityService.find(userDetails, spec, pageable));
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "권한 수정(완료)",
            description = "권한을 수정한다 <br />" +
                    "{<br />" +
                    "\"authorityName\": \"name1\",<br />" +
                    "\"description\": \"설명2\", <br />" +
                    "\"readYn\": [\"false\", \"true\", \"true\", \"true\", \"false\", \"true\", \"true\", \"true\"],<br />" +
                    "\"controlYn\": [\"false\", \"false\", \"true\", \"true\", \"false\", \"true\", \"true\", \"true\"],<br />" +
                    "\"useYn\": \"true\"<br />" +
                    "}",
            security = {
                    @SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = AuthorityHandleDto.class)))
            }
    )
    @CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H38)
    @PutMapping(value = "/{id}")
    public <U extends UserDetails> ResponseEntity modify(@AuthenticationPrincipal U principal, @PathVariable long id, @RequestBody AuthorityHandleDto authorityHandleDto) {

        return ResponseEntity.ok(authorityService.modify(principal, id, authorityHandleDto));
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "메뉴 목록 조회(완료)",
            description = "메뉴 목록 조회",
            security = {
                    @SecurityRequirement(name = OpenApiConfig.HEADER_NAME_AUTHORIZATION)
            },
            parameters = {
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = AccountDto.class)))
            }
    )
    @CustomLogger(svcType = SvcType.SVC08, svcClassType = SvcClassType.F09, subSvcClassType = SubSvcClassType.H42)
    @GetMapping(value = "/find-menu")
    public ResponseEntity findMenu() {
        return ResponseEntity.ok(this.authorityService.findMenu());
    }
}
