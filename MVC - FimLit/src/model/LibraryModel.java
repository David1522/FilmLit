package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class LibraryModel {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private HashMap<String, Book> booksDb = new HashMap<>();
    private HashMap<String, Book> userLibrary = new HashMap<>();

    public LibraryModel() {
        readBooksDB();
        readUserLibrary();
    }

    public void addBook(String ID, Book newBook) {
        booksDb.put(ID, newBook);
        writeBooksDB();
        writeUserLibrary();
    }

    public void updateBookName(String ID, String updated_name) {
        booksDb.get(ID).setName(updated_name);
        writeBooksDB();
        writeUserLibrary();
    }

    public void updateBookAuthor(String ID, String updated_author) {
        booksDb.get(ID).setAuthor(updated_author);
        writeBooksDB();
        writeUserLibrary();
    }

    public void updateBookYear(String ID, String updated_year) {
        booksDb.get(ID).setYear(updated_year);
        writeBooksDB();
        writeUserLibrary();
    }

    public void updateBookCategory(String ID, String updated_category) {
        booksDb.get(ID).setCategory(updated_category);
        writeBooksDB();
        writeUserLibrary();
    }

    public void removeBook(String ID) {
        booksDb.remove(ID);
        writeBooksDB();
        writeUserLibrary();
    }

    public void addBookLibrary(String name) {
        for (String key : booksDb.keySet()) {
            if (name.equalsIgnoreCase(booksDb.get(key).getName())) {
                userLibrary.put(key, booksDb.get(key));
            }
        }
        writeUserLibrary();
        writeBooksDB();
    }

    public void rateBook(String name, double rate, String comment) {
        for (Book book : userLibrary.values()) {
            if (name.equalsIgnoreCase(book.getName()))
                book.addRating(rate, comment);
        }
        writeUserLibrary();
        writeBooksDB();
    }

    public void removeBookLibrary(String name) {
        for (String key : userLibrary.keySet()) {
            if (name.equalsIgnoreCase(userLibrary.get(key).getName()))
                userLibrary.remove(key);
        }
        writeUserLibrary();
        writeBooksDB();
    }

    public boolean IDExist(String ID) {
        return booksDb.containsKey(ID);
    }

    public boolean bookInLibrary(String name) {
        boolean exist = false;
        for (Book book : userLibrary.values()) {
            if (name.equalsIgnoreCase(book.getName())) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public boolean bookExist(String name) {
        boolean exist = false;
        for (Book book : booksDb.values()) {
            if (name.equalsIgnoreCase(book.getName())) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public ArrayList<String> gatherBooksAuthor() {
        ArrayList<String> authors = new ArrayList<>();

        for (Book book : booksDb.values()) {
            if (!authors.contains(book.getAuthor()))
                authors.add(book.getAuthor());
        }

        return authors;
    }

    public ArrayList<String> gatherBooksCategory() {
        ArrayList<String> categories = new ArrayList<>();

        for (Book book : booksDb.values()) {
            if (!categories.contains(book.getCategory()))
                categories.add(book.getCategory());
        }

        return categories;
    }

    public String filterBooksByAuthor(String author) {
        ArrayList<Book> filteredBooks = new ArrayList<>();

        for (Book book : booksDb.values()) {
            if (author.equalsIgnoreCase(book.getAuthor()))
                filteredBooks.add(book);
        }

        return filteredBooks.toString();
    }

    public String filterBooksByCategory(String category) {
        ArrayList<Book> filteredBooks = new ArrayList<>();

        for (Book book : booksDb.values()) {
            if (category.equalsIgnoreCase(book.getCategory()))
                filteredBooks.add(book);
        }

        return filteredBooks.toString();
    }

    public String getBookNameByID(String ID) {
        return booksDb.get(ID).getName();
    }

    private void readBooksDB() {
        try (FileReader reader = new FileReader("src/db/books.json")) {
            Type type = new TypeToken<HashMap<String, Book>> () {}.getType();
            booksDb = gson.fromJson(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeBooksDB() {
        try (FileWriter writer = new FileWriter("src/db/books.json")) {
            gson.toJson(booksDb, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readUserLibrary() {
        try (FileReader reader = new FileReader("src/db/library.json")) {
            Type type = new TypeToken<HashMap<String, Book>> () {}.getType();
            userLibrary = gson.fromJson(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeUserLibrary() {
        try (FileWriter writer = new FileWriter("src/db/library.json")) {
            gson.toJson(userLibrary, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return booksDb.toString();
    }

    public String getUserLibrary() {
        return userLibrary.toString();
    }
}
