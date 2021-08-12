package my.springcloud.config.swagger;

import my.springcloud.common.wrapper.CommonModel;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;
import java.util.Optional;

@Configuration
public class OpenApiConfig {

    // 토큰인증헤더 키
    public static final String HEADER_NAME_AUTHORIZATION = "Authorization";
    // 토큰인증헤더 키 (Basic)
    public static final String HEADER_NAME_BASIC = "basic";
    // API 헤더 키
    public static final String HEADER_NAME_APIKEY = "apiKey";

    @Value("${spring.application.name}")
    private String appName;

    static {
        // ingnore swagger parameter
        // https://springdoc.org/faq.html#how-can-i-hide-a-parameter-from-the-documentation-
        SpringDocUtils.getConfig()
                    .addSimpleTypesForParameterObject()
                    .addRequestWrapperToIgnore(
                            Pageable.class,
                            Specification.class,
                            Map.class
                    );
    }

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(this.generateComponents())
                .info(new Info()
                        .version("v1")
						.title("[" + Optional.ofNullable(this.appName).orElse("").toUpperCase() + "] API")
						.description("my-cloud-services의 " + Optional.ofNullable(this.appName).orElse("").toUpperCase() + " API 문서입니다.")
                        .contact(new Contact()
                                .name("(주)리얼스네이크")
                                .email("realsnake1975@gamil.com")
                                .url("https://")
                        )
                        .license(new License().name("Copyrightⓒ2021 realsnake All rights reserved").url("https://"))
                );
    }

    private Components generateComponents() {
            return new Components()
                    // 인증헤더 설정
                    .addSecuritySchemes(OpenApiConfig.HEADER_NAME_AUTHORIZATION,
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("Bearer")
                                    .bearerFormat("JWT")
                    )
                    /*
                    // BASIC
                    .addSecuritySchemes(OpenApiConfig.HEADER_NAME_BASIC,
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("Basic")
                    )
                    // API 키 헤더 설정
                    .addSecuritySchemes(OpenApiConfig.HEADER_NAME_APIKEY,
                            new SecurityScheme()
                                    .in(SecurityScheme.In.HEADER)
                                    .name(this.apiKey)
                                    .type(SecurityScheme.Type.APIKEY)
                    )
                     */
                    ;
    }

    /**
     * 전역응답 모델설정
     *
     * @return
     */
    @Bean
    public OpenApiCustomiser globalOpenApiResponses() {
        return openApi -> openApi.getPaths().values()
                .forEach(path ->
                    path.readOperations()
                            .forEach(operation -> {
                                ApiResponses responses = operation.getResponses();
                                responses.addApiResponse("401", makeApiResponse("Unauthorized", new CommonModel<>(HttpStatus.UNAUTHORIZED)));
                                responses.addApiResponse("403", makeApiResponse("Forbidden", new CommonModel<>(HttpStatus.FORBIDDEN)));
                                responses.addApiResponse("404", makeApiResponse("Not Found", new CommonModel<>(HttpStatus.NOT_FOUND)));
                                responses.addApiResponse("500", makeApiResponse("Internal server error", new CommonModel<>(HttpStatus.INTERNAL_SERVER_ERROR)));
                            }));
    }

    /**
     * 기본응답 데이터 확인
     * @param desc
     * @param message
     * @return
     */
    private ApiResponse makeApiResponse(String desc, CommonModel<?> message) {
        return new ApiResponse()
                .description(desc)
                .content(
                        new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().schema(new Schema<CommonModel<?>>().example(message)))
                );
    }

}
