package com.example.andrei.chatapplication.account;

/**
 * @author Andrei
 *         TODO Account lab for later use
 */
public class AccountLab {

    private static AccountLab sAccountLab;
    private Account mAccount;

    private AccountLab() {

    }

    public static AccountLab getInstance() {
        if (sAccountLab == null) {
            sAccountLab = new AccountLab();
        }
        return sAccountLab;
    }

    public Account createAccount(String login, String passwd) {
        mAccount = new Account(login, passwd);
        return this.getAccount();
    }

    public Account getAccount() {
        return mAccount;
    }

}
