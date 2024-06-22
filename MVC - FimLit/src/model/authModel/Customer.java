package model.authModel;

import model.libraryModel.Book;

import java.util.HashMap;

public class Customer extends User {
    private final HashMap<String, Book> userLibrary = new HashMap<>();

    public Customer(String username, String email, String password, String type) {
        super(username, email, password, type);
    }

    public HashMap<String, Book> getUserLibrary() {
        return userLibrary;
    }
}
