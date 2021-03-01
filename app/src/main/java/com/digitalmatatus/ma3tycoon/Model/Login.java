package com.digitalmatatus.ma3tycoon.Model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by stephineosoro on 28/06/16.
 */

public class Login extends DataSupport {

    @Column(unique = true)
    private long id;

    private String email;

    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }


}

