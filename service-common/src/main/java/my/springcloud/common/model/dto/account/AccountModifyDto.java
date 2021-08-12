package my.springcloud.common.model.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(value = {
        "accountId"
        , "username"
        , "loginHistoryList"
        , "accountName"
        , "companyName"
        , "regId"
        , "regDt"
        , "modId"
        , "modDt"
        , "status"
})
@Schema(name = "AccountCreateDto", description = "권한 DTO 등록")
public class AccountModifyDto extends AccountDto {

    private static final long serialVersionUID = -5154790211167492937L;

}
