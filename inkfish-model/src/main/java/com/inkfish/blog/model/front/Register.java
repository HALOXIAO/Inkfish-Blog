package com.inkfish.blog.model.front;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author HALOXIAO
 **/
public class Register {

    @Email
    @NotNull
    String eMail;

    @NotEmpty
    String username;

    @NotEmpty
    String password;

}
