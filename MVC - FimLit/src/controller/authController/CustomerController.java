package controller.authController;

import controller.LibraryController;
import model.authModel.Customer;
import view.View;

import java.util.Locale;

public class CustomerController {
    private final Customer customer;
    private final View view = new View();

    public CustomerController(Customer customer) {
        this.customer = customer;
    }

    public void initAPP() {
        while (true) {
            int choice = view.displayMenu("HOME", "Where you want to go?", new String[] {"Library", "Settings"});

            if (choice == 1) {
                libraryModule();
            } else if (choice == 2) {
                if (settingsModule())
                    break;
            }
        }
    }

    private void libraryModule() {
        LibraryController libraryController = new LibraryController(customer.getUserLibrary(), customer.getUsername().toLowerCase(Locale.ROOT));
        libraryController.initModule();
    }

    private boolean settingsModule() {
        return view.displayConfirmDialog("You want to close session?");
    }
}
