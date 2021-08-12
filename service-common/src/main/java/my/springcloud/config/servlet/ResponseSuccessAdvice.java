package my.springcloud.config.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.springcloud.common.wrapper.CommonModel;
import my.springcloud.config.jackson.JacksonMapperConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
public class ResponseSuccessAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper = new JacksonMapperConfig().objectMapper();

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /*
     * Response OK 응답에 대해서 추가적인 공통 모델을 포함하여 리턴
     *
     * @param body
     * @param methodParameter
     * @param mediaType
     * @param httpMessageConverter
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> httpMessageConverter, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        HttpStatus httpStatus = HttpStatus.OK;
        if (serverHttpResponse instanceof ServletServerHttpResponse) {
            httpStatus = HttpStatus.valueOf(((ServletServerHttpResponse) serverHttpResponse).getServletResponse().getStatus());
        }

        serverHttpResponse.setStatusCode(httpStatus);

        if (httpMessageConverter.isAssignableFrom(StringHttpMessageConverter.class)) {
            return this.objectMapper.writeValueAsString(new CommonModel<>(body));
        }

        if ("image".equalsIgnoreCase(mediaType.getType())) {
            return body;
        }

        return new CommonModel<>(body);
    }

}
