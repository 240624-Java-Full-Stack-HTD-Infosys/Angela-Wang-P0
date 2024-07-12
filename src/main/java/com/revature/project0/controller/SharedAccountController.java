package com.revature.project0.controller;

import com.revature.project0.model.BankAccount;
import com.revature.project0.model.SharedAccount;
import com.revature.project0.model.User;
import com.revature.project0.service.BankAccountService;
import com.revature.project0.service.SharedAccountService;
import io.javalin.http.Context;

import java.util.List;

public class SharedAccountController {
    private SharedAccountService sharedAccountService;
    private BankAccountService bankAccountService;
    public SharedAccountController() {
        this.sharedAccountService = new SharedAccountService();
        this.bankAccountService = new BankAccountService();
    }

    private boolean isOwnerOfAccount(int accountId, User currentUser) {
        BankAccount account = bankAccountService.getBankAccountById(accountId);
        return account != null && account.getUserId() == currentUser.getUserId();
    }

    public void shareAccount(Context ctx) {
        SharedAccount sharedAccount = ctx.bodyAsClass(SharedAccount.class);
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        if (isAdmin || isOwnerOfAccount(sharedAccount.getAccountId(), currentUser)) {
            if (sharedAccountService.shareAccount(sharedAccount)) {
                ctx.status(201).json(sharedAccount);
            } else {
                ctx.status(400).result("Failed to share account");
            }
        } else {
            ctx.status(403).result("Forbidden");
        }
    }

    public void getAllSharedAccounts(Context ctx) {
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        if (isAdmin) {
            List<SharedAccount> accounts = sharedAccountService.getAllSharedAccounts();
            ctx.status(200).json(accounts);
        } else {
            ctx.status(403).result("Forbidden");
        }
    }

    public void getSharedAccountsByUserId(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        if (isAdmin || userId == currentUser.getUserId()) {
            List<SharedAccount> sharedAccounts = sharedAccountService.getSharedAccountsByUserId(userId);
            ctx.status(200).json(sharedAccounts);
        } else {
            ctx.status(403).result("Forbidden");
        }
    }

    public void getSharedAccountsByAccountId(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("accountId"));
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        if (isAdmin || isOwnerOfAccount(accountId, currentUser)) {
            List<SharedAccount> sharedAccounts = sharedAccountService.getSharedAccountsByAccountId(accountId);
            ctx.status(200).json(sharedAccounts);
        } else {
            ctx.status(403).result("Forbidden");
        }
    }

    public void unshareAccount(Context ctx) {
        int sharedAccountId = Integer.parseInt(ctx.pathParam("id"));
        SharedAccount sharedAccount = sharedAccountService.getSharedAccountById(sharedAccountId);
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        if (isAdmin || isOwnerOfAccount(sharedAccount.getAccountId(), currentUser)) {
            if (sharedAccountService.unshareAccount(sharedAccountId)) {
                ctx.status(200).result("Un-shared");
            } else {
                ctx.status(400).result("Failed to un-share account");
            }
        } else {
            ctx.status(403).result("Forbidden");
        }
    }
}
