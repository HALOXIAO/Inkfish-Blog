package com.inkfish.blog.web.exception;

/**
 * @author HALOXIAO
 **/
public class DBTransactionalException extends RuntimeException {
    public DBTransactionalException(String message) {
        super(message);
    }
}
