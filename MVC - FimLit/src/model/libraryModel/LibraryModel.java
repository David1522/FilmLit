package model.libraryModel;

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
    private HashMap<String, Work> worksDb = new HashMap<>();
    private HashMap<String, Work> userLibrary;
    private final String userLibraryFile;

    public LibraryModel(HashMap<String, Work> userLibrary, String username) {
        this.userLibrary = userLibrary;
        this.userLibraryFile = "src/db/libraryDB/usersLibraries/" + username + "_library.json";
        readWorksDB();
        readUserLibrary();
    }

    public void addWork(String ID, Work newWork) {
        worksDb.put(ID, newWork);
        writeWorksDB();
        writeUserLibrary();
    }

    public void updateWorkName(String ID, String updated_name) {
        worksDb.get(ID).setName(updated_name);
        writeWorksDB();
        writeUserLibrary();
    }

    public void updateWorkAuthor(String ID, String updated_author) {
        worksDb.get(ID).setAuthor(updated_author);
        writeWorksDB();
        writeUserLibrary();
    }

    public void updateWorkYear(String ID, String updated_year) {
        worksDb.get(ID).setYear(updated_year);
        writeWorksDB();
        writeUserLibrary();
    }

    public void updateWorkCategory(String ID, String updated_category) {
        worksDb.get(ID).setCategory(updated_category);
        writeWorksDB();
        writeUserLibrary();
    }

    public void removeWork(String ID) {
        worksDb.remove(ID);
        writeWorksDB();
        writeUserLibrary();
    }

    public void addWorkLibrary(String name) {
        for (String key : worksDb.keySet()) {
            if (name.equalsIgnoreCase(worksDb.get(key).getName())) {
                userLibrary.put(key, worksDb.get(key));
            }
        }
        writeUserLibrary();
        writeWorksDB();
    }

    public void rateWork(String name, double rate, String comment) {
        for (Work work : userLibrary.values()) {
            if (name.equalsIgnoreCase(work.getName()))
                work.addRating(rate, comment);
        }
        writeUserLibrary();
        writeWorksDB();
    }

    public void removeWorkLibrary(String name) {
        String workKey = "";
        for (String key : userLibrary.keySet()) {
            if (name.equalsIgnoreCase(userLibrary.get(key).getName()))
                workKey = key;
        }
        userLibrary.remove(workKey);
        writeUserLibrary();
        writeWorksDB();
    }

    public boolean IDExist(String ID) {
        return worksDb.containsKey(ID);
    }

    public boolean workInLibrary(String name) {
        boolean exist = false;
        for (Work work : userLibrary.values()) {
            if (name.equalsIgnoreCase(work.getName())) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public boolean workExist(String name) {
        boolean exist = false;
        for (Work work : worksDb.values()) {
            if (name.equalsIgnoreCase(work.getName())) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public ArrayList<String> gatherWorksAuthor() {
        ArrayList<String> authors = new ArrayList<>();

        for (Work work : worksDb.values()) {
            if (!authors.contains(work.getAuthor()))
                authors.add(work.getAuthor());
        }

        return authors;
    }

    public ArrayList<String> gatherWorksCategory() {
        ArrayList<String> categories = new ArrayList<>();

        for (Work work : worksDb.values()) {
            if (!categories.contains(work.getCategory()))
                categories.add(work.getCategory());
        }

        return categories;
    }

    public String filterWorksByAuthor(String author) {
        ArrayList<Work> filteredWorks = new ArrayList<>();

        for (Work work : worksDb.values()) {
            if (author.equalsIgnoreCase(work.getAuthor()))
                filteredWorks.add(work);
        }

        return filteredWorks.toString();
    }

    public String filterWorksByCategory(String category) {
        ArrayList<Work> filteredWorks = new ArrayList<>();

        for (Work work : worksDb.values()) {
            if (category.equalsIgnoreCase(work.getCategory()))
                filteredWorks.add(work);
        }

        return filteredWorks.toString();
    }

    public String getWorkNameByID(String ID) {
        return worksDb.get(ID).getName();
    }

    private void readWorksDB() {
        try (FileReader reader = new FileReader("src/db/libraryDB/works.json")) {
            Type type = new TypeToken<HashMap<String, Work>> () {}.getType();
            worksDb = gson.fromJson(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeWorksDB() {
        try (FileWriter writer = new FileWriter("src/db/libraryDB/works.json")) {
            gson.toJson(worksDb, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readUserLibrary() {
        try (FileReader reader = new FileReader(userLibraryFile)) {
            Type type = new TypeToken<HashMap<String, Work>> () {}.getType();
            userLibrary = gson.fromJson(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeUserLibrary() {
        try (FileWriter writer = new FileWriter(userLibraryFile)) {
            gson.toJson(userLibrary, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return worksDb.toString();
    }

    public String getUserLibrary() {
        return userLibrary.toString();
    }
}
