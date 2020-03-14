package com.inkfish.blog.server.model.front;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @author HALOXIAO
 **/

public class RecoverPassword {

    @NotEmpty
    private String code;

    @NotEmpty
    private String password;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RecoverPassword{" +
                "code='" + code + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
