package com.inkfish.blog.common;

/**
 * @author HALOXIAO
 **/
public enum ARTICLE_STATUS_CODE {

    ARTICLE_PUBLISH(0),
    ARTICLE_DRAFT(1);

    private final int value;

    ARTICLE_STATUS_CODE(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }
}
