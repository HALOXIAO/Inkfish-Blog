package com.inkfish.blog.server.common;

/**
 * @author HALOXIAO
 **/
public enum REDIS_CACHE_NAMESPACE {

    //    Article Cache String
    CACHE_ARTICLE_INFORMATION_NAMESPACE("cache:article:information::"),

    //    Article Home Cache Sorted Set
    CACHE_ARTICLE_HOME_OVERVIEW("cache:article:home:overview");

    private final String value;

    REDIS_CACHE_NAMESPACE(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
