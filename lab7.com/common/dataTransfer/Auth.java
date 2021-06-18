package com.lab7.common.dataTransfer;

import com.lab7.common.Utils.Hasher;

import java.io.Serializable;

public class Auth implements Serializable {
    public String login;
    public byte[] password;
    public Auth(String login, String password){
        this.login = login;
        this.password = Hasher.getHash(password);
    }
}
