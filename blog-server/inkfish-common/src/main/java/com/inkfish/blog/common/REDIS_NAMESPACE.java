package com.inkfish.blog.common;

/**
 * @author HALOXIAO
 **/
public enum REDIS_NAMESPACE {

    //找回密码功能
    EMAIL_VERIFICATION_FORGET_PASSWORD("email:verification:forgetPassword:"),

    //注册账号功能
    EMAIL_VERIFICATION_REGISTER("email:verification:register:");

    private final String value;

    REDIS_NAMESPACE(final String value) {
        this.value = value;
    }

    public final String getValue() {
        return value;
    }
}
