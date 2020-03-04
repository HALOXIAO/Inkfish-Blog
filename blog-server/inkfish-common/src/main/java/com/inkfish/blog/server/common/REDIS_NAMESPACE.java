package com.inkfish.blog.server.common;

/**
 * @author HALOXIAO
 **/
public enum REDIS_NAMESPACE {

    //找回密码功能命名空间
    EMAIL_VERIFICATION_FORGET_PASSWORD_NAMESPACE("email:verification:forgetPassword:"),

    //注册账号功能命名空间
    EMAIL_VERIFICATION_REGISTER_NAMESPACE("email:verification:register:"),

    //文章点赞数
    ARTICLE_INFORMATION_LIKE("article:information:vote:likesNumber"),

    //文章浏览人数
    ARTICLE_INFORMATION_WATCH("article:information:watch:viewsNumber"),

    //文章已点赞用户
    ARTICLE_INFORMATION_ALREADY_LIKE_NAMESPACE("article:information:alreadyLike:");



    private final String value;

    REDIS_NAMESPACE(final String value) {
        this.value = value;
    }

    public final String getValue() {
        return value;
    }
}
