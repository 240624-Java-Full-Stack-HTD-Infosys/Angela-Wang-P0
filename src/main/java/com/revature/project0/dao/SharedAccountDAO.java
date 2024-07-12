package com.revature.project0.dao;

import java.util.List;
import com.revature.project0.model.SharedAccount;

public interface SharedAccountDAO {
    boolean addSharedAccount(SharedAccount sharedAccount);
    List<SharedAccount> getAllSharedAccounts();
    SharedAccount getSharedAccountById(int sharedAccountId);
    List<SharedAccount> getSharedAccountsByUserId(int userId);
    List<SharedAccount> getSharedAccountsByAccountId(int accountId);
    boolean deleteSharedAccount(int sharedAccountId);
}

