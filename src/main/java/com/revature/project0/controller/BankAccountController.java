package com.revature.project0.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project0.model.BankAccount;
import com.revature.project0.model.SharedAccount;
import com.revature.project0.model.User;
import com.revature.project0.service.BankAccountService;
import com.revature.project0.service.SharedAccountService;
import io.javalin.http.Context;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class BankAccountController {
    private BankAccountService bankAccountService;
    private SharedAccountService sharedAccountService;
    private ObjectMapper mapper;
    public BankAccountController() {
        this.bankAccountService = new BankAccountService();
        this.sharedAccountService = new SharedAccountService();
        this.mapper = new ObjectMapper();
    }

    private boolean isOwnerOfAccount(int accountId, User currentUser) {
        BankAccount account = bankAccountService.getBankAccountById(accountId);
        return account != null && account.getUserId() == currentUser.getUserId();
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

    public void createBankAccount(Context ctx) {
        BankAccount account = ctx.bodyAsClass(BankAccount.class);
        if (bankAccountService.createBankAccount(account)) {
            ctx.status(201).json(account);
        } else {
            ctx.status(400).result("Bank account creation failed");
        }
    }

    public void getAllAccounts(Context ctx) {
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        if (isAdmin) {
            List<BankAccount> accounts = bankAccountService.getAllAccounts();
            ctx.status(200).json(accounts);
        } else {
            ctx.status(403).result("Forbidden");
        }
    }

    public void getBankAccountById(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("id"));
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        if (hasAccessToAccount(accountId, currentUser, isAdmin)) {
            BankAccount account = bankAccountService.getBankAccountById(accountId);
            if (account != null) {
                ctx.status(200).json(account);
            } else {
                ctx.status(404).result("Bank account not found");
            }
        } else {
            ctx.status(403).result("Forbidden");
        }
    }

    public void getBankAccountsByUserId(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        if (currentUser != null && (isAdmin || userId == currentUser.getUserId())) {
            List<BankAccount> accounts = bankAccountService.getBankAccountsByUserId(userId);
            List<SharedAccount> sharedAccounts = sharedAccountService.getSharedAccountsByUserId(userId);
            for (SharedAccount sharedAccount : sharedAccounts) {
                BankAccount account = bankAccountService.getBankAccountById(sharedAccount.getAccountId());
                if (account != null && !accounts.contains(account)) {
                    accounts.add(account);
                }
            }
            ctx.status(200).json(accounts);
        } else {
            ctx.status(403).result("Forbidden");
        }
    }

    public void updateBankAccount(Context ctx) {
        BankAccount account = ctx.bodyAsClass(BankAccount.class);
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        if (isAdmin || isOwnerOfAccount(account.getAccountId(), currentUser)) {
            if (bankAccountService.updateBankAccount(account)) {
                ctx.status(200).json(account);
            } else {
                ctx.status(400).result("Bank account update failed");
            }
        } else {
            ctx.status(403).result("Forbidden");
        }
    }

    public void deleteBankAccount(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("id"));
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        if (isAdmin || isOwnerOfAccount(accountId, currentUser)) {
            BankAccount account = bankAccountService.getBankAccountById(accountId);
            if (account != null && account.getBalance() == 0.00) {
                if (bankAccountService.deleteBankAccount(accountId)) {
                    ctx.status(200).result("Deleted");
                } else {
                    ctx.status(400).result("Bank account deletion failed");
                }
            } else {
                ctx.status(400).result("Account balance is not zero or account does not exist");
            }
        } else {
            ctx.status(403).result("Forbidden");
        }
    }

    public void deposit(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("id"));
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        try {
            JsonNode jsonNode = mapper.readTree(ctx.body());
            double amount = jsonNode.get("amount").asDouble();

            if (hasAccessToAccount(accountId, currentUser, isAdmin)) {
                if (bankAccountService.deposit(accountId, amount)) {
                    ctx.status(200).result("Deposit successful");
                } else {
                    ctx.status(400).result("Deposit failed");
                }
            } else {
                ctx.status(403).result("Forbidden");
            }
        } catch (IOException e) {
            ctx.status(400).result("Invalid JSON");
        }
    }

    public void withdraw(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("id"));
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        try {
            JsonNode jsonNode = mapper.readTree(ctx.body());
            double amount = jsonNode.get("amount").asDouble();

            if (hasAccessToAccount(accountId, currentUser, isAdmin)) {
                if (bankAccountService.withdraw(accountId, amount)) {
                    ctx.status(200).result("Withdrawal successful");
                } else {
                    ctx.status(400).result("Withdrawal failed, please check balance");
                }
            } else {
                ctx.status(403).result("Forbidden");
            }
        } catch (IOException e) {
            ctx.status(400).result("Invalid JSON");
        }
    }

    public void transfer(Context ctx) {
        int sourceAccountId = Integer.parseInt(ctx.pathParam("sourceId"));
        int destinationAccountId = Integer.parseInt(ctx.pathParam("destinationId"));
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        try {
            JsonNode jsonNode = mapper.readTree(ctx.body());
            double amount = jsonNode.get("amount").asDouble();

            if (hasAccessToAccount(sourceAccountId, currentUser, isAdmin)) {
                if (bankAccountService.transfer(sourceAccountId, destinationAccountId, amount)) {
                    ctx.status(200).result("Transfer successful");
                } else {
                    ctx.status(400).result("Transfer failed, please check balance");
                }
            } else {
                ctx.status(403).result("Forbidden");
            }
        } catch (IOException e) {
            ctx.status(400).result("Invalid JSON");
        }
    }
}