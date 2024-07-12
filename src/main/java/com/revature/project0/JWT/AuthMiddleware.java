package com.revature.project0.JWT;

import com.revature.project0.model.User;
import com.revature.project0.service.UserService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.jsonwebtoken.Claims;


public class AuthMiddleware implements Handler {
    private UserService userService;
    public AuthMiddleware(UserService userService) {
        this.userService = new UserService();
    }

    @Override
    public void handle(Context ctx) throws Exception {
        String token = ctx.cookie("token");

        if (token != null && JwtUtil.validateToken(token)) {
            Claims claims = JwtUtil.decodeToken(token);
            int userId = claims.get("userId", Integer.class);
            boolean isAdmin = claims.get("isAdmin", Boolean.class);
            User currentUser = userService.getUserById(userId);
            if (currentUser != null) {
                ctx.attribute("currentUser", currentUser);
                ctx.attribute("isAdmin", isAdmin);

                return;
            }
        }
        ctx.status(401).result("Unauthorized");
    }
}