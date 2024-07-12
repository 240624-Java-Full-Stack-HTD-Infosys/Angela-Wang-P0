package com.revature.project0.dao;


import com.revature.project0.model.BankAccount;
import com.revature.project0.model.SharedAccount;
import com.revature.project0.utils.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SharedAccountDAOImpl implements SharedAccountDAO{
    @Override
    public boolean addSharedAccount(SharedAccount sharedAccount) {
        try(Connection conn = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO SharedAccounts (account_id, user_id) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sharedAccount.getAccountId());
            pstmt.setInt(2, sharedAccount.getUserId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<SharedAccount> getAllSharedAccounts() {
        List<SharedAccount> accounts = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM SharedAccounts";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                accounts.add(new SharedAccount(
                        rs.getInt("shared_account_id"),
                        rs.getInt("account_id"),
                        rs.getInt("user_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }


    @Override
    public SharedAccount getSharedAccountById(int sharedAccountId) {
        try(Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM SharedAccounts WHERE shared_account_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sharedAccountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new SharedAccount(
                        rs.getInt("shared_account_id"),
                        rs.getInt("account_id"),
                        rs.getInt("user_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SharedAccount> getSharedAccountsByUserId(int userId) {
        List<SharedAccount> sharedAccounts = new ArrayList<>();
        try(Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM SharedAccounts WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                sharedAccounts.add(new SharedAccount(
                        rs.getInt("shared_account_id"),
                        rs.getInt("account_id"),
                        rs.getInt("user_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sharedAccounts;
    }

    @Override
    public List<SharedAccount> getSharedAccountsByAccountId(int accountId) {
        List<SharedAccount> sharedAccounts = new ArrayList<>();
        try(Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM SharedAccounts WHERE account_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                sharedAccounts.add(new SharedAccount(
                        rs.getInt("shared_account_id"),
                        rs.getInt("account_id"),
                        rs.getInt("user_id")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sharedAccounts;
    }

    @Override
    public boolean deleteSharedAccount(int sharedAccountId) {
        try(Connection conn = ConnectionUtil.getConnection()) {
            String sql = "DELETE FROM SharedAccounts WHERE shared_account_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sharedAccountId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
