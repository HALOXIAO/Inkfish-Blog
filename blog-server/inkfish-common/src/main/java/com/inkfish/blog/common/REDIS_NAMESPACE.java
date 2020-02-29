package com.inkfish.blog.common;

/**
 * @author HALOXIAO
 **/
public enum REDIS_NAMESPACE {

    //找回密码功能命名空间
    EMAIL_VERIFICATION_FORGET_PASSWORD_NAMESPACE("email:verification:forgetPassword:"),

    //注册账号功能命名空间
    EMAIL_VERIFICATION_REGISTER_NAMESPACE("email:verification:register:"),

    //文章点赞数
    ARTICLE_INFORMATION_VOTE("article:information:vote:voteNumber1"),

    //文章浏览人数
    ARTICLE_INFORMATION_WATCH("article:information:watch:watchNumber1");

    private final String value;

    REDIS_NAMESPACE(final String value) {
        this.value = value;
    }

    public final String getValue() {
        return value;
    }
}
