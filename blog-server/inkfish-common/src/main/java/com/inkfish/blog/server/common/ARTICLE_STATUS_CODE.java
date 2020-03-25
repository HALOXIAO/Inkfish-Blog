package com.inkfish.blog.server.common;

/**
 * @author HALOXIAO
 **/
public enum ARTICLE_STATUS_CODE {

    //    文章已发布
    ARTICLE_PUBLISH(0),
    //    文章为草稿
    ARTICLE_DRAFT(1),

    //    开启评论
    ARTICLE_COMMENT_ENABLE(0),
    //    关闭评论
    ARTICLE_COMMENT_DISABLE(1);


    private final int value;

    ARTICLE_STATUS_CODE(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }
}
