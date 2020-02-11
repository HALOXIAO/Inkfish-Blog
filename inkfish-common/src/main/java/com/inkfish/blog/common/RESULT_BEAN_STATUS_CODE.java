package com.inkfish.blog.common;

public enum RESULT_BEAN_STATUS_CODE {

    NO_LOGIN(-1),
    SUCCESS(0),
    CHECK_FAIL(1),
    NO_PERMISSION(2),
    UNKNOWN_EXCEPTION(-99),
    ARGUMENT_EXCEPTION(3);

    private final int value;

    RESULT_BEAN_STATUS_CODE(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

}
