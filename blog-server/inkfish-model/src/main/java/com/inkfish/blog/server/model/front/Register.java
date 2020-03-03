package com.inkfish.blog.server.model.front;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @author HALOXIAO
 **/
public class Register {


    @Email
    String email;

    @NotEmpty
    String username;

    @NotEmpty
    String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
