package com.revature.project0.service;

import com.revature.project0.dao.BankAccountDAO;
import com.revature.project0.dao.BankAccountDAOImpl;
import com.revature.project0.dao.TransactionDAO;
import com.revature.project0.dao.TransactionDAOImpl;
import com.revature.project0.model.BankAccount;
import com.revature.project0.model.Transaction;


import java.time.LocalDateTime;
import java.util.List;

import static com.revature.project0.utils.DateTimeUtil.localDateTimeToString;

public class BankAccountService {
    private BankAccountDAO bankAccountDAO;
    private TransactionDAO transactionDAO;
    public BankAccountService(){
        this.bankAccountDAO = new BankAccountDAOImpl();
        this.transactionDAO = new TransactionDAOImpl();
    }


    public boolean createBankAccount(BankAccount account) {
        return bankAccountDAO.addBankAccount(account);
    }

    public List<BankAccount> getAllAccounts() {
        return bankAccountDAO.getAllBankAccounts();
    }

    public BankAccount getBankAccountById(int accountId) {
        return bankAccountDAO.getBankAccountById(accountId);
    }


    public List<BankAccount> getBankAccountsByUserId(int userId) {
        return bankAccountDAO.getBankAccountsByUserId(userId);
    }


    public boolean updateBankAccount(BankAccount account) {
        return bankAccountDAO.updateBankAccount(account);
    }


    public boolean deleteBankAccount(int accountId) {
        BankAccount account = bankAccountDAO.getBankAccountById(accountId);
        if (account != null && account.getBalance() == 0.00) {
            return bankAccountDAO.deleteBankAccount(accountId);
        }
        return false;
    }


    public boolean deposit(int accountId, double amount) {
        if (amount > 0) {
            BankAccount account = bankAccountDAO.getBankAccountById(accountId);
            if (account != null) {
                account.setBalance(account.getBalance() + amount);
                if (bankAccountDAO.updateBankAccount(account)) {
                    Transaction transaction = new Transaction(0, accountId, "deposit", amount, localDateTimeToString(LocalDateTime.now()));
                    return transactionDAO.addTransaction(transaction);
                }
            }
        }
        return false;
    }


    public boolean withdraw(int accountId, double amount) {
        if (amount > 0) {
            BankAccount account = bankAccountDAO.getBankAccountById(accountId);
            if (account != null && account.getBalance() >= amount) {
                account.setBalance(account.getBalance() - amount);
                if (bankAccountDAO.updateBankAccount(account)) {
                    Transaction transaction = new Transaction(0, accountId, "withdrawal", amount, localDateTimeToString(LocalDateTime.now()));
                    return transactionDAO.addTransaction(transaction);
                }
            }
        }
        return false;
    }


    public boolean transfer(int sourceAccountId, int destinationAccountId, double amount) {
        if (amount > 0) {
            BankAccount sourceAccount = bankAccountDAO.getBankAccountById(sourceAccountId);
            BankAccount destinationAccount = bankAccountDAO.getBankAccountById(destinationAccountId);
            if (sourceAccount != null && destinationAccount != null && sourceAccount.getBalance() >= amount) {
                sourceAccount.setBalance(sourceAccount.getBalance() - amount);
                destinationAccount.setBalance(destinationAccount.getBalance() + amount);
                if (bankAccountDAO.updateBankAccount(sourceAccount) && bankAccountDAO.updateBankAccount(destinationAccount)) {
                    Transaction withdrawal = new Transaction(0, sourceAccountId, "transfer_out", amount, localDateTimeToString(LocalDateTime.now()));
                    Transaction deposit = new Transaction(0, destinationAccountId, "transfer_in", amount, localDateTimeToString(LocalDateTime.now()));
                    return transactionDAO.addTransaction(withdrawal) && transactionDAO.addTransaction(deposit);
                }
            }
        }
        return false;
    }
}
