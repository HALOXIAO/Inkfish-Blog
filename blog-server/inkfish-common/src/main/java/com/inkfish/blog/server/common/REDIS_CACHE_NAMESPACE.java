package com.inkfish.blog.server.common;

/**
 * @author HALOXIAO
 **/
public enum  REDIS_CACHE_NAMESPACE {

//    Article Cache String
    ARTICLE_CACHE_NAMESPACE("cache:article:information::");


    private final String value;

    REDIS_CACHE_NAMESPACE(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
