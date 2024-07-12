package com.revature.project0.menus;


import com.revature.project0.model.User;
import com.revature.project0.service.UserService;

import java.util.Scanner;

public class RegisterMenu implements Menu{
    String name = "register";
    MenuUtil menuUtil = MenuUtil.getMenuUtil();
    Scanner sc = menuUtil.getScanner();
    UserService userService;
    public RegisterMenu(UserService userService) {
        this.userService = userService;
    }
    public void render() {
        System.out.println("=======[ Register ]=======");
        System.out.print("Enter a new username: ");
        String username = sc.nextLine();

        System.out.print("Enter a new password: ");
        String password1 = sc.nextLine();

        System.out.print("Re-enter password: ");
        String password2 = sc.nextLine();

        if (password1.equals(password2)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password1);
            // Set other user details here (name, email, etc.)
            if (userService.registerUser(user)) {
                System.out.println("New account created successfully!");
                menuUtil.navigate("login");
            } else {
                System.out.println("Registration failed. Try again.");
                menuUtil.quit();
            }
        } else {
            System.out.println("Passwords don't match!");
            menuUtil.quit();
        }
    }

    public String getName() {
        return this.name;
    }
}
