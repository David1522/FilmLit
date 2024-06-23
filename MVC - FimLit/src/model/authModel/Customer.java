package model.authModel;

import model.libraryModel.Work;

import java.util.HashMap;

public class Customer extends User {
    private final HashMap<String, Work> userLibrary = new HashMap<>();

    public Customer(String username, String email, String password, String type) {
        super(username, email, password, type);
    }

    public HashMap<String, Work> getUserLibrary() {
        return userLibrary;
    }
}
