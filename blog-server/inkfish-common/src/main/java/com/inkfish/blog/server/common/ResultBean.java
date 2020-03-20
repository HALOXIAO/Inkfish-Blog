package com.inkfish.blog.server.common;


import lombok.Data;

import java.io.Serializable;

/**
 * @author HALOXIAO
 **/
@Data
public class ResultBean<T> implements Serializable {

    /**
     * 返回的信息(主要出错的时候使用)
     */
    private String msg ;

    /**
     * 接口返回码, 0表示成功
     * 0   : 成功
     * >0 : 表示已知的异常(例如提示错误等, 需要调用地方单独处理)
     * <0 : 表示未知的异常(不需要单独处理, 调用方统一处理)
     */
    private int code;

    /**
     * 返回的数据
     */
    private T data;

    private ResultBean() {
        super();
    }

    public ResultBean(String msg, RESULT_BEAN_STATUS_CODE code) {
        super();
        this.msg = msg;
        this.code = code.getValue();
    }

    public ResultBean(Throwable e, RESULT_BEAN_STATUS_CODE code) {
        super();
        this.msg = e.toString();
        this.code = code.getValue();
    }
}
