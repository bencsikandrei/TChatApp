package com.example.andrei.chatapplication.account;

/**
 * @author Andrei
 *         Simple POJO for messages
 */
/* account POJO */
public class Account {
    String mLogin;

    String mPasswd;


    public Account(String login, String passwd) {
        mLogin = login;

        mPasswd = passwd;
    }

    public String getLogin() {
        return mLogin;
    }

    public String getPasswd() {
        return mPasswd;
    }
}
