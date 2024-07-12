package com.revature.project0.service;

import com.revature.project0.dao.TransactionDAO;
import com.revature.project0.dao.TransactionDAOImpl;
import com.revature.project0.model.Transaction;

import java.util.List;

public class TransactionService {
    private TransactionDAO transactionDAO;
    public TransactionService(){
        this.transactionDAO = new TransactionDAOImpl();
    }


    public boolean addTransaction(Transaction transaction) {
        return transactionDAO.addTransaction(transaction);
    }


    public List<Transaction> getTransactionsByAccountId(int accountId) {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }
}
