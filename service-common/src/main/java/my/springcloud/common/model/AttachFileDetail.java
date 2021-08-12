package my.springcloud.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachFileDetail implements Serializable {

    private static final long serialVersionUID = -3450318880193835651L;

    @Schema(name = "orgName", description = "원본파일명")
    private final String orgName;

    @Schema(name = "mime", description = "mime 타입")
    private final String mime;

    @Schema(name = "size", description = "첨부파일 사이즈")
    private final Long size;

}
