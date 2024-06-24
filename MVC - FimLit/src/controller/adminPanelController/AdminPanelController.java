package controller.adminPanelController;

import model.adminPanelModel.AdminPanelModel;
import view.View;

public class AdminPanelController {
    private final AdminPanelModel model = new AdminPanelModel();
    private final View view = new View();

    public AdminPanelController () {}

    public void initModule() {
        while (true) {
            int choice = view.displayMenu("Admin Panel Menu", "What do you want to do?", new String[] {"Display Customers", "Delete Customer", "Exit"});

            if (choice == 1) {
                view.displayMessage(model.getCustomers());
            } else if (choice == 2) {
                String ID = view.queryString("Enter customer ID: ");
                if (model.idExist(ID)) {
                    model.deleteCustomer(ID);
                } else {
                    view.displayMessage("No customer found with that ID.");
                }
            } else {
                break;
            }
        }
    }
}
