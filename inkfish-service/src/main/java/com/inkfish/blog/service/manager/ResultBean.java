package com.inkfish.blog.service.manager;


import com.inkfish.blog.web.status.RESULT_BEAN_STATUS_CODE;
import lombok.Data;

/**
 * @author HALOXIAO
 **/
@Data
public class ResultBean<T> {

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
    private RESULT_BEAN_STATUS_CODE code;

    /**
     * 返回的数据
     */
    private T data;

    private ResultBean() {
        super();
    }

    public ResultBean(T data, RESULT_BEAN_STATUS_CODE code) {
        super();
        this.data = data;
        this.code = code;
    }

    public ResultBean(Throwable e, RESULT_BEAN_STATUS_CODE code) {
        super();
        this.msg = e.toString();
        this.code = code;
    }
}
