package com.inkfish.blog.common.exception;

import java.io.IOException;

/**
 * @author HALOXIAO
 **/
public class CreateFileException extends IOException {
    public CreateFileException(String message){
        super(message);
    }
}
