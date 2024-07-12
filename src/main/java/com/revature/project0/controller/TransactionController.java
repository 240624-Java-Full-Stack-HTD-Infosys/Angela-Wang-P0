package com.revature.project0.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project0.model.BankAccount;
import com.revature.project0.model.Transaction;
import com.revature.project0.model.SharedAccount;
import com.revature.project0.model.User;
import com.revature.project0.service.BankAccountService;
import com.revature.project0.service.SharedAccountService;
import com.revature.project0.service.TransactionService;

import io.javalin.http.Context;

import java.util.List;

public class TransactionController {
    private TransactionService transactionService;
    private BankAccountService bankAccountService;
    private SharedAccountService sharedAccountService;


    public TransactionController() {
        this.transactionService = new TransactionService();
        this.bankAccountService = new BankAccountService();
        this.sharedAccountService = new SharedAccountService();
    }

    private boolean hasAccessToAccount(int accountId, User currentUser, boolean isAdmin) {
        BankAccount account = bankAccountService.getBankAccountById(accountId);
        if (account == null || currentUser == null) {
            return false;
        }
        if (isAdmin || account.getUserId() == currentUser.getUserId()) {
            return true;
        }
        List<SharedAccount> sharedAccounts = sharedAccountService.getSharedAccountsByAccountId(accountId);
        for (SharedAccount sharedAccount : sharedAccounts) {
            if (sharedAccount.getUserId() == currentUser.getUserId()) {
                return true;
            }
        }
        return false;
    }

    public void getTransactionsByAccountId(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("accountId"));
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        if (hasAccessToAccount(accountId, currentUser, isAdmin)) {
            List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
            ctx.status(200).json(transactions);
        } else {
            ctx.status(403).result("Forbidden");
        }
    }
}
