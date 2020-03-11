package com.inkfish.blog.server.common;

/**
 * @author HALOXIAO
 **/
public enum SESSION_CODE {

//    注册验证码
    REGISTER_VERIFICATION_CODE("registerCode"),
//    找回密码的验证码
    RECOVER_VERIFICATION_CODE("forgetPasswordCode");


    private final String value;

    SESSION_CODE(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
