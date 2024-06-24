package model.adminPanelModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.authModel.Customer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class AdminPanelModel {
    private HashMap<String, Customer> customersDB = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public AdminPanelModel() {
        readCustomerDB();
    }

    public String getCustomers() {
        return customersDB.toString();
    }

    public boolean idExist(String ID) {
        return (customersDB.containsKey(ID));
    }

    public void deleteCustomer(String ID) {
        customersDB.remove(ID);
        writeCustomerDB();
    }

    private void readCustomerDB() {
        try (FileReader reader = new FileReader("src/db/authDB/customer.json")) {
            Type type = new TypeToken<HashMap<String, Customer>> () {}.getType();
            customersDB = gson.fromJson(reader, type);
            System.out.println("Customers Uploaded");
            System.out.println(customersDB.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeCustomerDB() {
        try (FileWriter writer = new FileWriter("src/db/authDB/customer.json")) {
            gson.toJson(customersDB, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
