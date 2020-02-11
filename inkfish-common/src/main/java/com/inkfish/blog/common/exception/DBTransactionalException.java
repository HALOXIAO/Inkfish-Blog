package com.inkfish.blog.common.exception;

/**
 * @author HALOXIAO
 **/
public class DBTransactionalException extends RuntimeException {
    public DBTransactionalException(String message) {
        super(message);
    }
}
