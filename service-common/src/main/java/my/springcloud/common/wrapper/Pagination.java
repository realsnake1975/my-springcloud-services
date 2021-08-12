package my.springcloud.common.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * 페이징 클래스
 */
@AllArgsConstructor
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pagination implements Serializable {

	private static final long serialVersionUID = 3995316322424049566L;

	/**
     * 페이징 목록 수
     */
    private final Integer size;

    /**
     * 현재 페이지 번호
     */
    private final Integer page;

    /**
     * 조회된 전체 목록 개수
     */
    private final Long totalCount;

    /**
     * 조회된 전체 페이지 수
     */
    private final Integer totalPage;

}