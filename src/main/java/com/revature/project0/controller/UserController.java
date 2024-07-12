package com.revature.project0.controller;

import com.revature.project0.JWT.JwtUtil;
import com.revature.project0.model.User;
import com.revature.project0.service.UserService;
import io.javalin.http.Context;

import java.util.List;
public class UserController {
    private UserService userService;
    public UserController() {
        this.userService = new UserService();
    }

    public boolean hasAccessToUser(int targetUserId, User currentUser, boolean isAdmin) {
        return isAdmin || currentUser.getUserId() == targetUserId;
    }

    public void registerUser(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        if (userService.registerUser(user)) {
            ctx.status(201).json(user);
        } else {
            ctx.status(400).result("User registration failed");
        }
    }

    public void loginUser(Context ctx) {
        User loginUser = ctx.bodyAsClass(User.class);
        User user = userService.loginUser(loginUser.getUsername(), loginUser.getPassword());
        if (user != null) {
            String token = JwtUtil.generateToken(user);
            ctx.cookie("token", token);
            ctx.status(200).json(user);
        } else {
            ctx.status(401).result("Invalid credentials");
        }
    }

    public void getUserById(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("id"));
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        if (hasAccessToUser(userId, currentUser, isAdmin)) {
            User user = userService.getUserById(userId);
            if (user != null) {
                ctx.status(200).json(user);
            } else {
                ctx.status(404).result("User not found");
            }
        } else {
            ctx.status(403).result("Forbidden");
        }
    }

    public void getAllUsers(Context ctx) {

        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));
        System.out.println("isAdmin: " + isAdmin);
        if (isAdmin) {
            List<User> users = userService.getAllUsers();
            ctx.status(200).json(users);
        } else {
            ctx.status(403).result("Forbidden");
        }
    }

    public void updateUser(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        if (hasAccessToUser(user.getUserId(), currentUser, isAdmin)) {
            if (!isAdmin && user.isAdmin() != currentUser.isAdmin()) {
                ctx.status(403).result("Forbidden: Non-admin users cannot update the admin status.");
                return;
            }
            if (userService.updateUser(user)) {
                ctx.status(200).json(user);
            } else {
                ctx.status(400).result("User update failed");
            }
        } else {
            ctx.status(403).result("Forbidden");
        }
    }

    public void deleteUser(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("id"));
        User currentUser = ctx.attribute("currentUser");
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));

        if (hasAccessToUser(userId, currentUser, isAdmin)) {
            if (userService.deleteUser(userId)) {
                ctx.status(200).result("Deleted");
            } else {
                ctx.status(400).result("User deletion failed");
            }
        } else {
            ctx.status(403).result("Forbidden");
        }
    }
    public void elevateUserToAdmin(Context ctx) {
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));
        int userId = Integer.parseInt(ctx.pathParam("id"));
        User currentUser = ctx.attribute("currentUser");

        if (currentUser != null && isAdmin) {
            if (userService.elevateUserToAdmin(userId, currentUser.getUserId())) {
                ctx.status(200).result("User elevated to admin");
            } else {
                ctx.status(400).result("Failed to elevate user to admin");
            }
        } else {
            ctx.status(403).result("Forbidden");
        }
    }

    public void demoteAdminToUser(Context ctx) {
        boolean isAdmin = Boolean.TRUE.equals(ctx.attribute("isAdmin"));
        int userId = Integer.parseInt(ctx.pathParam("id"));
        User currentUser = ctx.attribute("currentUser");

        if (currentUser != null && isAdmin) {

            if (userService.demoteAdminToUser(userId, currentUser.getUserId())) {
                ctx.status(200).result("Admin demoted to user");
            } else {
                ctx.status(400).result("Failed to demote admin to user");
            }
        } else {
            ctx.status(403).result("Forbidden");
        }
    }

}