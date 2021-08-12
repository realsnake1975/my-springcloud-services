package my.springcloud.common.wrapper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import my.springcloud.common.constants.ResponseCodeType;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 공통 응답 모델
 */
@AllArgsConstructor
@Builder
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonModel<T> implements Serializable {

    private static final long serialVersionUID = 8088971173865321662L;

	private final String code;

    private final String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private final LocalDateTime timestamp;

    @Setter
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Pagination pagination;

    public CommonModel() {
		this.code = ResponseCodeType.SUCCESS.code();
		this.message = ResponseCodeType.SUCCESS.desc();
		this.timestamp = LocalDateTime.now();
    }

	public CommonModel(String code, String message) {
        this.code = code;
        this.message = message;
		this.timestamp = LocalDateTime.now();
    }

	public CommonModel(HttpStatus httpStatus) {
		this(String.valueOf(httpStatus.value()), httpStatus.getReasonPhrase());
	}

    public CommonModel(ResponseCodeType responseCodeType) {
		this(responseCodeType.code(), responseCodeType.desc());
    }

	public CommonModel(T result) {
        this();
        this.resultIfPage(result);
    }

	public CommonModel(ResponseCodeType responseCodeType, T result) {
		this(responseCodeType);
		this.resultIfPage(result);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void resultIfPage(T result) {
		if (Objects.nonNull(result)) {
			if (result instanceof Page) {
				Page page = (Page) result;
				this.result = (T) page.getContent();
				this.pagination = new Pagination(page.getPageable().getPageSize(), page.getPageable().getPageNumber(), page.getTotalElements(), page.getTotalPages());
			}
			else {
				this.result = result;
			}
		}
	}

}
