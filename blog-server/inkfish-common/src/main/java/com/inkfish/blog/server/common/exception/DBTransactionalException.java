package com.inkfish.blog.server.common.exception;

/**
 * @author HALOXIAO
 **/
public class DBTransactionalException extends RuntimeException {
    public DBTransactionalException(String message) {
        super(message);
    }
}
