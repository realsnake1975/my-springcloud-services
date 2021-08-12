package my.springcloud.common.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import my.springcloud.common.constants.AccountStatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(value = {
        "accountId"
        , "loginHistoryList"
        , "regId"
        , "regDt"
        , "modId"
        , "modDt"
        , "status"
})
@Schema(name = "AccountCreateDto", description = "권한 DTO 등록")
public class AccountCreate extends AccountDetail {

    private static final long serialVersionUID = -5154790211167492937L;

    public AccountCreate() {
        LocalDateTime now = LocalDateTime.now();
        setRegDt(now);
        setUpdDt(now);
        setPasswordUpdDt(now);
        setStatus(AccountStatusType.APPROVAL.code());
    }

}
