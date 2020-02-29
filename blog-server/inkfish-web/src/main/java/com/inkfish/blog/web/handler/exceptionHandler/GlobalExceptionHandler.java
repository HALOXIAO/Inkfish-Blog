package com.inkfish.blog.web.handler.exceptionHandler;

import com.inkfish.blog.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.common.ResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author HALOXIAO
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler  {




    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResultBean<String> SQLIntegrityConstraintViolationExceptionHandle(SQLIntegrityConstraintViolationException e) {
        log.error(e.getMessage());
        return new ResultBean<>("repeated username or email", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
    }

    @ExceptionHandler(SQLException.class)
    public ResultBean<String> exceptionHandle(SQLException e) {
        log.error(e.getMessage());
        return new ResultBean<String>("fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
    }

}
