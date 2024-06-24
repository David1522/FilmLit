package model.roomsModel;

import java.util.ArrayList;

public class Room {
    private final String host;
    private final String name;
    private final String creationDate;
    private final String category;
    private final ArrayList<String> messages = new ArrayList<>();

    public Room(String host, String name, String creationDate, String category) {
        this.host = host;
        this.name = name;
        this.creationDate = creationDate;
        this.category = category;
    }

    public String getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getCategory() {
        return category;
    }

    public String getMessages() {
        return messages.toString();
    }

    public void addMessage (String message) {
        messages.add(message);
    }

    @Override
    public String toString() {
        return "Host: " + host +
                ", Name: " + name +
                ", CreationDate: " + creationDate + ", ";
    }
}
