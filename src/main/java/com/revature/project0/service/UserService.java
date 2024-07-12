package com.revature.project0.service;

import com.revature.project0.dao.UserDAO;
import com.revature.project0.dao.UserDAOImpl;
import com.revature.project0.model.User;


import java.util.List;

public class UserService {
    private UserDAO userDAO;
    public UserService() {
        this.userDAO = new UserDAOImpl();
    }



    public boolean registerUser(User user) {
        return userDAO.addUser(user);
    }

    public User loginUser(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if(user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }


    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }


    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }


    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }


    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }

    public boolean elevateUserToAdmin(int userId, int requesterId) {
        User requester = userDAO.getUserById(requesterId);
        if (requester != null && requester.isAdmin()) {
            User user = userDAO.getUserById(userId);
            if (user != null) {
                user.setAdmin(true);
                return userDAO.updateUser(user);
            }
        }
        return false;
    }


    public boolean demoteAdminToUser(int userId, int requesterId) {
        User requester = userDAO.getUserById(requesterId);
        if (requester != null && requester.isAdmin()) {
            User user = userDAO.getUserById(userId);
            if (user != null) {
                user.setAdmin(false);
                return userDAO.updateUser(user);
            }
        }
        return false;
    }


}
