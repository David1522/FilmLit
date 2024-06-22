package controller.authController;

import view.View;
import model.authModel.AuthModel;

public class AuthController {
    private final View view = new View();
    private final AuthModel model = new AuthModel();

    public AuthController() {}

    public void initAuth() {
        while (true) {
            int choice = view.displayMenu("Welcome to FimLit!", "You must log in to continue.", new String[] {"Log In", "Sing In", "Admin", "Exit"});

            if (choice == 1) {
                customerLogIn();
            } else if (choice == 2) {
                singIn(0);
            } else if (choice == 3) {
                adminAuth();
            } else {
                view.displayMessage("Thanks for using our services! ~ Developers Team");
                break;
            }
        }
    }

    public void customerLogIn() {
        String username = view.queryString("Username: ");
        String password = view.queryString("Password: ");
        if (!model.validateCustomerLogin(username, password)) {
            view.displayMessage("Username or Password Incorrect.");
            if (view.displayConfirmDialog("Did you forget your password?"))
                passwordRecovery();
        }
    }

    public void adminLogIn() {
        String username = view.queryString("Username: ");
        String password = view.queryString("Password: ");
        if (!model.validateAdminLogin(username, password)) {
            view.displayMessage("Username or Password Incorrect.");
            if (view.displayConfirmDialog("Did you forget your password?"))
                passwordRecovery();
        }
    }

    public void singIn(int userType) {
        while (true) {
            String ID = view.queryString("ID: ");
            String username = view.queryString("Username: ");
            String email = view.queryString("Email: ");
            String password = view.queryString("Password: ");

            if (model.validateID(ID)) {
                view.displayMessage("ID already used.");
            } else if (model.validateUsername(username)) {
                view.displayMessage("Username is already in use.");
            } else if (model.validateEmail(email)) {
                view.displayMessage("Email already registered.");
            } else {
                if (userType == 0) {
                    String type = view.displayDropDownMenu(new String[] {"BASE", "PREMIUM", "AUTHOR"});
                    model.singInCustomer(ID, username, email, password, type);
                    view.displayMessage("User successfully created!");
                } else {
                    model.singInAdmin(ID, username, email, password);
                    view.displayMessage("Admin successfully added!");
                }
                break;
            }
        }
    }

    public void adminAuth() {
        while (true) {
            int choice = view.displayMenu("Admin log In!", "What do you want to do?", new String[] {"Log In", "Sing In", "Exit"});

            if (choice == 1) {
                adminLogIn();
            } else if (choice == 2) {
                singIn(1);
            } else {
                break;
            }
        }
    }

    public void passwordRecovery() {
        String email = view.queryString("Enter your email: ");
        if (model.validateEmail(email)) {
            String newPassword = view.queryString("New Password: ");
            model.updatePassword(email, newPassword);
            view.displayMessage("Password Successfully Updated!");
        } else {
            view.displayMessage("Email is not registered.");
        }
    }
}
