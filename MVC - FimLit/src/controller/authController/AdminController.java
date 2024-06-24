package controller.authController;

import controller.adminPanelController.AdminPanelController;
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
            int choice = view.displayMenu("HOME", "Where you want to go?", new String[] {"Admin Panel", "Settings"});

            if (choice == 1) {
                adminPanelModule();
            } else {
                if (settingsModule())
                    break;
            }
        }
    }

    private void adminPanelModule() {
        AdminPanelController controller = new AdminPanelController();
        controller.initModule();
    }

    private boolean settingsModule() {
        return view.displayConfirmDialog("You want to close session?");
    }
}
