package deviceet.common.exception;

import lombok.Getter;

//400：请求验证错误
//401：认证错误
//403：权限不够
//404：资源未找到
//409：业务异常
//500：系统错误
@Getter
public enum ErrorCode {
    //400
    BAD_REQUEST(400),
    REQUEST_VALIDATION_FAILED(400),

    //401
    AUTHENTICATION_FAILED(401),

    //403
    ACCESS_DENIED(403),

    //404
    NOT_FOUND(404),
    AR_NOT_FOUND(404),
    MAINTENANCE_RECORD_NOT_FOUND(404),

    //405
    METHOD_NOT_ALLOWED(405),

    //409
    CONFLICT(409),
    NOT_SAME_ORG(409),
    EQUIPMENT_NAME_ALREADY_EXISTS(409),

    //429
    TOO_MANY_REQUEST(429),

    //500
    SYSTEM_ERROR(500);

    private final int status;

    ErrorCode(int status) {
        this.status = status;
    }

}
