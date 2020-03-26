package com.inkfish.blog.server.common;

/**
 * @author HALOXIAO
 **/
public enum REDIS_TAG_CACHE_NAMESPACE {

//    已存在的tag
    CACHE_ARTICLE_TAG("cache:article:tag:exit_tag");


    private final String value;

    REDIS_TAG_CACHE_NAMESPACE(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
