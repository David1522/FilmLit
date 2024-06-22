package model.authModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import controller.authController.AdminController;
import controller.authController.CustomerController;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;

public class AuthModel {
    private HashMap<String, Customer> customerDB = new HashMap<>();
    private HashMap<String, Admin> adminDB = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public AuthModel() {
        readCustomerDB();
        readAdminDB();
    }

    public boolean validateCustomerLogin(String username, String password) {
        boolean exist = false;

        for (Customer customer : customerDB.values()) {
            if (username.equals(customer.getUsername()) && password.equals(customer.getPassword())) {
                CustomerController customerController = new CustomerController(customer);
                customerController.initAPP();
                exist = true;
                break;
            }
        }
        return exist;
    }

    public boolean validateAdminLogin(String username, String password) {
        boolean exist = false;
        for (Admin admin : adminDB.values()) {
            if (username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
                AdminController adminController = new AdminController(admin);
                adminController.initAPP();
                exist = true;
                break;
            }
        }
        return exist;
    }

    public boolean validateID(String ID) {
        boolean exist = false;
        for (String customerID : customerDB.keySet()) {
            if (ID.equals(customerID)) {
                exist = true;
                break;
            }
        }
        for (String adminID : adminDB.keySet()) {
            if (ID.equals(adminID)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public boolean validateUsername(String username) {
        boolean exist = false;
        for (Customer customer : customerDB.values()) {
            if (username.toLowerCase(Locale.ROOT).equals(customer.getUsername().toLowerCase())) {
                exist = true;
                break;
            }
        }
        for (Admin admin : adminDB.values()) {
            if (username.toLowerCase(Locale.ROOT).equals(admin.getUsername().toLowerCase(Locale.ROOT))) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public boolean validateEmail(String email) {
        boolean exist = false;
        for (Customer customer : customerDB.values()) {
            if (email.equals(customer.getEmail())) {
                exist = true;
                break;
            }
        }
        for (Admin admin : adminDB.values()) {
            if (email.equals(admin.getEmail())) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public void singInCustomer(String ID, String username, String email, String password, String type) {
        customerDB.put(ID, new Customer(username, email, password, type));
        createLibraryUserFile(username);
        writeCustomerDB();
    }

    public void singInAdmin(String ID, String username, String email, String password) {
        adminDB.put(ID, new Admin(username, email, password));
        writeAdminDB();
    }

    public void updatePassword(String email, String newPassword) {
        for (Customer customer : customerDB.values()) {
            if (email.equals(customer.getEmail())) {
                customer.setPassword(newPassword);
                writeCustomerDB();
                break;
            }
        }

        for (Admin admin : adminDB.values()) {
            if (email.equals(admin.getEmail())) {
                admin.setPassword(newPassword);
                writeAdminDB();
                break;
            }
        }
    }

    private void readCustomerDB() {
        try (FileReader reader = new FileReader("src/db/authDB/customer.json")) {
            Type type = new TypeToken<HashMap<String, Customer>> () {}.getType();
            customerDB = gson.fromJson(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeCustomerDB() {
        try (FileWriter writer = new FileWriter("src/db/authDB/customer.json")) {
            gson.toJson(customerDB, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readAdminDB() {
        try (FileReader reader = new FileReader("src/db/authDB/admin.json")) {
            Type type = new TypeToken<HashMap<String, Admin>> () {}.getType();
            adminDB = gson.fromJson(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeAdminDB() {
        try (FileWriter writer = new FileWriter("src/db/authDB/admin.json")) {
            gson.toJson(adminDB, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createLibraryUserFile(String username) {
        File file = new File("src/db/libraryDB/usersLibraries/" + username.toLowerCase(Locale.ROOT) + ".json");
        try {
            if (file.createNewFile()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("{}");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
