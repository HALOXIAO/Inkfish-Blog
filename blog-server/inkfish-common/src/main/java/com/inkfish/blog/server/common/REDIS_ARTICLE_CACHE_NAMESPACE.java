package com.inkfish.blog.server.common;

/**
 * @author HALOXIAO
 **/
public enum REDIS_ARTICLE_CACHE_NAMESPACE {

    //    Article Cache String
    CACHE_ARTICLE_INFORMATION_PREFIX("cache:article:information::"),

    //    Article Home Cache Sorted Set ArticleOverviewVO JSON
    CACHE_ARTICLE_HOME_OVERVIEW("cache:article:home:overview"),

    //    Article Status Cache Hash
    CACHE_ARTICLE_STATUS_INFORMATION("cache:article:status:article_status"),

    //    Article enableComment Cache Hash
    CACHE_ARTICLE_COMMENT_STATUS_INFORMATION("cache:article:comment:status:enableComment_status"),

    //    Article Count Cache String
    CACHE_ARTICLE_COUNT("cache:article:count:num");

    private final String value;

    REDIS_ARTICLE_CACHE_NAMESPACE(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
