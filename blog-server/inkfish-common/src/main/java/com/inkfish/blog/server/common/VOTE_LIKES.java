package com.inkfish.blog.server.common;

/**
 * @author HALOXIAO
 **/
public enum VOTE_LIKES {
    //    点赞数
    VOTE("vote"),

    //    观看数
    WATCH("watch");


    private final String value;

    VOTE_LIKES(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
