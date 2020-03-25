package com.inkfish.blog.server.common;

public enum RESULT_BEAN_STATUS_CODE {

//    未登录
    NO_LOGIN(-1),
//    成功
    SUCCESS(0),
//    （账户密码）匹配失败
    CHECK_FAIL(1),
//    无权限
    NO_PERMISSION(2),
//    未知异常
    UNKNOWN_EXCEPTION(-99),
//    参数异常
    ARGUMENT_EXCEPTION(3);

    private final int value;

    RESULT_BEAN_STATUS_CODE(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

}
