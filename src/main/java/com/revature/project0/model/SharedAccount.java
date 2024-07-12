package com.revature.project0.model;

public class SharedAccount {
    private int sharedAccountId;
    private int accountId;
    private int userId;


    public SharedAccount() {}

    public SharedAccount(int sharedAccountId, int accountId, int userId) {
        this.sharedAccountId = sharedAccountId;
        this.accountId = accountId;
        this.userId = userId;
    }


    public int getSharedAccountId() {
        return sharedAccountId;
    }

    public void setSharedAccountId(int sharedAccountId) {
        this.sharedAccountId = sharedAccountId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }



}
