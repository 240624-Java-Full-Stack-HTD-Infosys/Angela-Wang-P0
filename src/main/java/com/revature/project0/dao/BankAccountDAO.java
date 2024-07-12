package com.revature.project0.dao;

import com.revature.project0.model.BankAccount;

import java.util.List;

public interface BankAccountDAO {
    boolean addBankAccount(BankAccount account);
    List<BankAccount> getAllBankAccounts();
    BankAccount getBankAccountById(int accountId);
    List<BankAccount> getBankAccountsByUserId(int userId);
    boolean updateBankAccount(BankAccount account);
    boolean deleteBankAccount(int accountId);
}
