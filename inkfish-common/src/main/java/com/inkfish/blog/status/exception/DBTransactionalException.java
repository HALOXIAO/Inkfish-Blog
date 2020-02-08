package com.inkfish.blog.status.exception;

/**
 * @author HALOXIAO
 **/
public class DBTransactionalException extends RuntimeException {
    public DBTransactionalException(String message) {
        super(message);
    }
}
