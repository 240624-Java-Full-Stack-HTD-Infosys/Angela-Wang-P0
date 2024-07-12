package com.revature.project0.dao;

import com.revature.project0.model.Transaction;
import java.util.List;

public interface TransactionDAO {
    boolean addTransaction(Transaction transaction);
    List<Transaction> getTransactionsByAccountId(int accountId);
}
