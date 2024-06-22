package controller.authController;

import model.authModel.Admin;
import view.View;

public class AdminController {
    private Admin admin;
    private View view = new View();

    public AdminController(Admin admin) {
        this.admin = admin;
    }

    public void initAPP() {
        while (true) {
            int choice = view.displayMenu("HOME", "Where you want to go?", new String[] {"Settings"});

            if (choice == 1) {
                if (settingsModule())
                    break;
            }
        }
    }

    private void profileModule() {

    }

    private boolean settingsModule() {
        return view.displayConfirmDialog("You want to close session?");
    }
}
