package my.springcloud.account.service;

import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.account.exception.AdminAuthException;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PasswordValidator {

    private PasswordValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static void validatePassword(String password) {
        if (password == null || "".equals(password)) { // 공백 체크
            log.error("> Detected({}): No Password", password);
            throw new AdminAuthException(ResponseCodeType.SERVER_ERROR_41001014);
        }

        checkPasswordRule(password);

        // 3자리 연속 문자 정규식
        final String samePattern = "(\\w)\\1\\1";
        // 동일한 문자 3개 이상 체크
        Matcher sameCheckMatcher = Pattern.compile(samePattern).matcher(password);
        if (sameCheckMatcher.find()) {
            log.error("Detected({}): 3개 이상 동일한 문자 또는 숫자", password);
            throw new AdminAuthException(ResponseCodeType.SERVER_ERROR_41001014);
        }

        checkContinuousPassword(password);
    }

    private static boolean checkPasswordRule(String password) {
        // 영문, 숫자, 특수문자
        String pwPattern1 = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
        Matcher passwordCheckMatcher1 = Pattern.compile(pwPattern1).matcher(password);
        if (passwordCheckMatcher1.find()) {
            log.debug("> Detected({}): pattern 일치, 영문, 숫자, 특수문자, 8~20", password);
            return true;
        }

        // 영문, 숫자
        String pwPattern2 = "^[A-Za-z[0-9]]{10,20}$";
        Matcher passwordCheckMatcher2 = Pattern.compile(pwPattern2).matcher(password);
        if (passwordCheckMatcher2.find()) {
            log.debug("> Detected({}): pattern 일치, 영문, 숫자, 10~20", password);
            return true;
        }

        // 영문, 특수문자
        String pwPattern3 = "^[[0-9]$@$!%*#?&]{10,20}$";
        Matcher passwordCheckMatcher3 = Pattern.compile(pwPattern3).matcher(password);
        if (passwordCheckMatcher3.find()) {
            log.debug("> Detected({}): pattern 일치, 영문, 특수문자, 10~20", password);
            return true;
        }

        // 특수문자, 숫자
        String pwPattern4 = "^[[A-Za-z]$@$!%*#?&]{10,20}$";
        Matcher passwordCheckMatcher4 = Pattern.compile(pwPattern4).matcher(password);
        if (passwordCheckMatcher4.find()) {
            log.debug("> Detected({}): pattern 일치, 특수문자, 숫자, 10~20", password);
            return true;
        }

        log.debug("> Detected({}): 패스워드 조합 정책 불일치", password);
        throw new AdminAuthException(ResponseCodeType.SERVER_ERROR_41001014);
    }

    private static boolean checkContinuousPassword(String password) {
        int o = 0;
        int d = 0;
        int p = 0;
        int n = 0;
        int limit = 3;

        for (int i=0; i<password.length(); i++) {
            char tempVal = password.charAt(i);
            if (i > 0 && (p = o - tempVal) > -2 && (n = p == d ? n + 1 :0) > limit -3) {
                log.error("Detected({}): 3자리 이상 연속된 숫자 또는 문자", password);
                throw new AdminAuthException(ResponseCodeType.SERVER_ERROR_41001014);
            }
            d = p;
            o = tempVal;
        }

        return true;
    }

}
