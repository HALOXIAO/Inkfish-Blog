package com.inkfish.blog.server.common;

/**
 * @author HALOXIAO
 **/
public enum REDIS_CACHE_NAMESPACE {

    //    Article Cache String
    ARTICLE_CACHE_NAMESPACE("cache:article:information::"),

    //    Article Home Cache Hash
    ARTICLE_HOME_CACHE_NAMESPACE("cache:article:home:information:");
    private final String value;

    REDIS_CACHE_NAMESPACE(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
