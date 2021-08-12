package my.springcloud.common.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class AttachFileDto implements Serializable {

    private static final long serialVersionUID = -3450318880193835651L;

    @Schema(name = "orgName", description = "원본파일명")
    private String orgName;

    @Schema(name = "mime", description = "mime 타입")
    private String mime;

    @Schema(name = "size", description = "첨부파일 사이즈")
    private Long size;

    public AttachFileDto(String orgName, String mime, Long size) {
        this.orgName = orgName;
        this.mime = mime;
        this.size = size;
    }

}
