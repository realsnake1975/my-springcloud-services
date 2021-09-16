package my.springcloud.common.model.account;

import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.Default;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import my.springcloud.common.model.AttachFileDetail;

@Getter
@JsonIgnoreProperties(value = {
	"resource"
})
@Schema(name = "AccountAttachFileDetail", description = "계정 첨부파일 DTO")
public class AccountAttachFileDetail extends AttachFileDetail {

	private static final long serialVersionUID = 5591465592981161549L;

	public AccountAttachFileDetail(String orgName, String updatedName, String mime, Long size, String path) {
		super(orgName, updatedName, mime, size, path);
	}

	public AccountAttachFileDetail(AttachFileDetail afd) {
		this(afd.getOrgName(), afd.getUpdatedName(), afd.getMime(), afd.getSize(), afd.getPath());
	}

	@Default
	public AccountAttachFileDetail(String orgName, String updatedName, String mime, Long size, String path, String attachFileId, Long accountId) {
		this(orgName, updatedName, mime, size, path);
		this.attachFileId = attachFileId;
		this.accountId = accountId;
	}

	@Schema(name = "attachFileId", description = "첨부파일 아이디")
	private String attachFileId;

	@Schema(name = "accountId", description = "계정 일련번호")
	private Long accountId;

	@Setter
	private Resource resource;

}
