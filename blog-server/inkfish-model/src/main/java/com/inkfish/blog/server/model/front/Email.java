package com.inkfish.blog.server.model.front;

import javax.validation.constraints.NotEmpty;

/**
 * @author HALOXIAO
 **/
public class Email {

    @javax.validation.constraints.Email
    @NotEmpty
    private String Email;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
