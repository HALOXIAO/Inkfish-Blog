package com.inkfish.blog.server.model.front;

import javax.validation.constraints.NotEmpty;

/**
 * @author HALOXIAO
 **/
public class Email {

    @javax.validation.constraints.Email
    @NotEmpty
    private String Email;

    @NotEmpty
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public String toString() {
        return "Email{" +
                "Email='" + Email + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
