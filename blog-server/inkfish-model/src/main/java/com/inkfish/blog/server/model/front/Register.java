package com.inkfish.blog.server.model.front;

import com.inkfish.blog.server.common.annotation.PasswordRestriction;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @author HALOXIAO
 **/
public class Register {


    @NotEmpty
    @Email
    String email;

    @NotEmpty
    String username;

    @PasswordRestriction
    @NotEmpty
    String password;

    @NotEmpty
    String code;



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    @Override
    public String toString() {
        return "Register{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
