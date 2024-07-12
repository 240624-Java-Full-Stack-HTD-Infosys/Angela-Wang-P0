package com.revature.project0.dao;

import com.revature.project0.model.BankAccount;
import com.revature.project0.model.User;
import com.revature.project0.utils.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BankAccountDAOImpl implements BankAccountDAO {
    @Override
    public boolean addBankAccount(BankAccount account) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO BankAccounts (user_id, account_type, balance) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, account.getUserId());
            pstmt.setString(2, account.getAccountType());
            pstmt.setDouble(3, account.getBalance());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public List<BankAccount> getAllBankAccounts() {
        List<BankAccount> accounts = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM BankAccounts";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                accounts.add(new BankAccount(
                        rs.getInt("account_id"),
                        rs.getInt("user_id"),
                        rs.getString("account_type"),
                        rs.getDouble("balance")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;

    }

    @Override
    public BankAccount getBankAccountById(int accountId) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM BankAccounts WHERE account_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new BankAccount(
                        rs.getInt("account_id"),
                        rs.getInt("user_id"),
                        rs.getString("account_type"),
                        rs.getDouble("balance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BankAccount> getBankAccountsByUserId(int userId) {
        List<BankAccount> accounts = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM BankAccounts WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                accounts.add(new BankAccount(
                        rs.getInt("account_id"),
                        rs.getInt("user_id"),
                        rs.getString("account_type"),
                        rs.getDouble("balance")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public boolean updateBankAccount(BankAccount account) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "UPDATE BankAccounts SET account_type = ?, balance = ? WHERE account_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, account.getAccountType());
            pstmt.setDouble(2, account.getBalance());
            pstmt.setInt(3, account.getAccountId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteBankAccount(int accountId) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Delete related transactions
            String deleteTransactionsSql = "DELETE FROM Transactions WHERE account_id = ?;"
                    + "DELETE FROM BankAccounts WHERE account_id = ?;";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteTransactionsSql)) {
                pstmt.setInt(1, accountId);
                pstmt.setInt(2, accountId);
                pstmt.executeUpdate();
            }
            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = ConnectionUtil.getConnection()) {
                conn.rollback(); // Rollback transaction in case of error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
}
