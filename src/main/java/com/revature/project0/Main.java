
import com.revature.project0.JWT.AuthMiddleware;
import com.revature.project0.controller.BankAccountController;
import com.revature.project0.controller.SharedAccountController;
import com.revature.project0.controller.TransactionController;
import com.revature.project0.controller.UserController;
import com.revature.project0.menus.LoginMenu;
import com.revature.project0.menus.MainMenu;
import com.revature.project0.menus.MenuUtil;
import com.revature.project0.menus.RegisterMenu;
import com.revature.project0.service.UserService;
import io.javalin.Javalin;



public static void main(String[] args) {

    startJavalinServer();

    runMenu();
}

private static void runMenu() {
    UserService userService = new UserService();
    MenuUtil menuUtil = MenuUtil.getMenuUtil();
    menuUtil.register(new MainMenu());
    menuUtil.register(new RegisterMenu(userService));
    menuUtil.register(new LoginMenu(userService));
    menuUtil.run();
}

private static void startJavalinServer() {
    UserService userService = new UserService();
    UserController userController = new UserController();
    BankAccountController bankAccountController = new BankAccountController();
    TransactionController transactionController = new TransactionController();
    SharedAccountController sharedAccountController = new SharedAccountController();
    AuthMiddleware auth = new AuthMiddleware(userService);


    Javalin app = Javalin.create().start(7000);



    app.before("/users/*", auth);
    app.before("/users", auth); // Cover /users without trailing path
    app.before("/accounts/*", auth);
    app.before("/accounts", auth);
    app.before("/transactions/*", auth);
    app.before("/transactions", auth);
    app.before("/sharedaccounts/*", auth);
    app.before("/sharedaccounts", auth);

    // User routes
    app.post("/users/register", userController::registerUser);
    app.post("/users/login", userController::loginUser);
    app.get("/users/{id}", userController::getUserById);
    app.get("/users", userController::getAllUsers);
    app.put("/users", userController::updateUser);
    app.delete("/users/{id}", userController::deleteUser);
    app.put("/users/elevate/{id}", userController::elevateUserToAdmin);
    app.put("/users/demote/{id}", userController::demoteAdminToUser);

    // BankAccount routes
    app.post("/accounts", bankAccountController::createBankAccount);
    app.get("/accounts", bankAccountController::getAllAccounts);
    app.get("/accounts/{id}", bankAccountController::getBankAccountById);
    app.get("/users/{userId}/accounts", bankAccountController::getBankAccountsByUserId);
    app.put("/accounts", bankAccountController::updateBankAccount);
    app.delete("/accounts/{id}", bankAccountController::deleteBankAccount);
    app.post("/accounts/{id}/deposit", bankAccountController::deposit);
    app.post("/accounts/{id}/withdraw", bankAccountController::withdraw);
    app.post("/accounts/transfer/{sourceId}/to/{destinationId}", bankAccountController::transfer);

    // Transaction routes
    app.get("/accounts/{accountId}/transactions", transactionController::getTransactionsByAccountId);

    // SharedAccount routes
    app.post("/sharedaccounts", sharedAccountController::shareAccount);
    app.get("/sharedaccounts", sharedAccountController::getAllSharedAccounts);
    app.get("/users/{userId}/sharedaccounts", sharedAccountController::getSharedAccountsByUserId);
    app.get("/accounts/{accountId}/sharedaccounts", sharedAccountController::getSharedAccountsByAccountId);
    app.delete("/sharedaccounts/{id}", sharedAccountController::unshareAccount);
}

