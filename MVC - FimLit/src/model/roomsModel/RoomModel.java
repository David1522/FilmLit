package model.roomsModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class RoomModel {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final HashMap<String, Room> roomsDB = new HashMap<>();

    public RoomModel() {
        readRoomsDB();
    }

    public void createRoom(String ID, String host, String name, String creationDate, String category) {
        roomsDB.put(ID, new Room(host, name, creationDate, category));
        writeRooms();
    }

    public boolean deleteRoom(String ID, String username) {
        boolean deleted = false;
        if (username.equals(roomsDB.get(ID).getHost())) {
            roomsDB.remove(ID);
            deleted = true;
            writeRooms();
        }
        return deleted;
    }

    public void sendRoomMessage(String name, String username, String date, String content) {
        for (Room room : roomsDB.values()) {
            if (name.equalsIgnoreCase(room.getName())) {
                room.addMessage(username + " | " + date + ": " + content);
            }
        }
        writeRooms();
    }

    public String getRoomMessages(String name) {
        String messages = "";
        for (Room room : roomsDB.values()) {
            if (name.equalsIgnoreCase(room.getName())) {
                messages = room.getMessages();
            }
        }
        return messages;
    }

    public String getAllRooms() {
        return roomsDB.toString();
    }

    public String getRoomInfo(String name) {
        String info = "";
        for (Room room : roomsDB.values()) {
            if (name.equalsIgnoreCase(room.getName())) {
                info = "Host: " + room.getHost() + "Name: " + room.getName() + ", Creation Date: " + room.getCreationDate() + ", Category: " + room.getCategory() + ".";
            }
        }
        return info;
    }

    public ArrayList<String> gatherRoomsCategories() {
        ArrayList<String> categories = new ArrayList<>();
        for (Room room : roomsDB.values()) {
            if (!categories.contains(room.getCategory())) {
                categories.add(room.getCategory());
            }
        }
        return categories;
    }

    public String filterRoomsByCategory(String category) {
        String rooms = "";
        for (Room room : roomsDB.values()) {
            if (category.equals(room.getCategory())) {
                rooms += room.toString();
            }
        }
        return rooms;
    }

    public String filterRoomsByHost(String host) {
        String rooms = "";
        for (Room room : roomsDB.values()) {
            if (host.equals(room.getHost())) {
                rooms += room.toString();
            }
        }
        return rooms;
    }

    public ArrayList<String> gatherRoomsHost() {
        ArrayList<String> hosts = new ArrayList<>();
        for (Room room : roomsDB.values()) {
            if (!hosts.contains(room.getHost())) {
                hosts.add(room.getCategory());
            }
        }
        return hosts;
    }

    public boolean idExist(String ID) {
        boolean exist = false;
        for (String key : roomsDB.keySet()) {
            if (ID.equals(key)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public boolean nameExist (String name) {
        boolean exist = false;
        for (Room room : roomsDB.values()) {
            if (name.equalsIgnoreCase(room.getName())) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public void readRoomsDB() {
        try (FileReader reader = new FileReader("src/db/roomsDB/rooms.json")) {
            Type type = new TypeToken<HashMap<String, Room>> () {}.getType();
            gson.fromJson(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeRooms() {
        try (FileWriter writer = new FileWriter("src/db/roomsDB/rooms.json")) {
            gson.toJson(roomsDB, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
