package com.revature.project0.dao;

import com.revature.project0.model.User;

import java.util.List;

public interface UserDAO {
    // CRUD operations
    boolean addUser(User user);
    User getUserById(int userId);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(int userId);
}
