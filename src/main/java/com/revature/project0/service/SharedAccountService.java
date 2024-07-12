package com.revature.project0.service;

import com.revature.project0.dao.SharedAccountDAO;
import com.revature.project0.dao.SharedAccountDAOImpl;
import com.revature.project0.model.SharedAccount;

import java.util.List;

public class SharedAccountService {
    private SharedAccountDAO sharedAccountDAO;
    public SharedAccountService(){
        this.sharedAccountDAO = new SharedAccountDAOImpl();
    }

    public List<SharedAccount> getAllSharedAccounts() {
        return sharedAccountDAO.getAllSharedAccounts();
    }

    public boolean shareAccount(SharedAccount sharedAccount) {
        return sharedAccountDAO.addSharedAccount(sharedAccount);
    }
    public SharedAccount getSharedAccountById(int sharedAccountId) {
        return sharedAccountDAO.getSharedAccountById(sharedAccountId);
    }

    public List<SharedAccount> getSharedAccountsByUserId(int userId) {
        return sharedAccountDAO.getSharedAccountsByUserId(userId);
    }



    public List<SharedAccount> getSharedAccountsByAccountId(int accountId) {
        return sharedAccountDAO.getSharedAccountsByAccountId(accountId);
    }


    public boolean unshareAccount(int sharedAccountId) {
        return sharedAccountDAO.deleteSharedAccount(sharedAccountId);
    }
}
