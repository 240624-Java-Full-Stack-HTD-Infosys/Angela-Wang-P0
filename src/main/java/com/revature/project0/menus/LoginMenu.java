package com.revature.project0.menus;


import com.revature.project0.model.User;
import com.revature.project0.service.UserService;

import java.util.Scanner;

public class LoginMenu implements Menu{
    String name = "login";
    MenuUtil menuUtil = MenuUtil.getMenuUtil();
    Scanner sc = menuUtil.getScanner();
    UserService userService;
    public LoginMenu(UserService userService){
        this.userService = userService;
    }

    public void render() {
        System.out.println("=======[ Login ]=======");
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        User user = userService.loginUser(username, password);
        if (user != null) {
            System.out.println("Successful login! Demo over!");
        } else {
            System.out.println("Unable to login, bad username and/or password. Demo over!");
        }

        menuUtil.quit();
    }

    public String getName() {
        return this.name;
    }
}