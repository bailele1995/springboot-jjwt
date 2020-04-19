package com.pjb.springbootjjwt.result;

/**
 * 失败错误码枚举
 * 0开头为系统错误 catch之后包装返回
 * 1开头为业务错误 业务中断
 */
public enum UserError {
    PASSWORD_NOT_EQUALS_CONFIRMWORD("40001","密码与确认密码不一致"),
    PASSWORD_OR_NAME_IS_ERROR("40003","用户名或密码错误"),
    EMP_IS_NULL_EXIT("400004","人员不存在"),
    EMP_IS_EXIT("40005","人员已存在"),
    EMP_IS_NOT_OPENED("40006","您的账号已被禁用，暂无法登录"),
    TOKEN_IS_VERITYED("40007","无效的令牌"),
    TOKEN_IS_NOT_EXIT("40007","令牌不存在")
    ;
    String errorCode;
    String errorMessage;
    private static final String ns = "work-order";

    UserError(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return ns + "." + errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
