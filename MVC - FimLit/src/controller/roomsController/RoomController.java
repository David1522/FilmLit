package controller.roomsController;

import model.roomsModel.RoomModel;
import view.View;

public class RoomController {
    private final RoomModel model = new RoomModel();
    private final View view = new View();
    private final String username;
    private final String customerType;

    public RoomController(String customerType, String username) {
        this.username = username;
        this.customerType = customerType;
    }

    public void initModule() {
        if (customerType.equals("PREMIUM") || customerType.equals("AUTHOR")) {
            while (true) {
                int choice = view.displayMenu("Rooms Menu", "What do you want to do?", new String[] {"Create Room", "Search Rooms", "Enter Room", "Remove Room", "Exit"});

                if (choice == 1) {
                    createRoom();
                } else if (choice == 2) {
                    searchRooms();
                } else if (choice == 3) {
                    String name = view.queryString("name");
                    if (model.nameExist(name)) {
                        roomInteraction(name);
                    } else {
                        view.displayMessage("No room registered with that name;");
                    }
                } else if (choice == 4) {
                    String ID = view.queryString("Enter room ID: ");
                    if (model.idExist(ID)) {
                        if (model.deleteRoom(ID, username)) {
                            view.displayMessage("Room successfully deleted");
                        } else {
                            view.displayMessage("Opps! Seems like you're not the host of this room.");
                        }
                    } else {
                        view.displayMessage("No room registered with that ID.");
                    }
                } else {
                    break;
                }
            }
        } else {
            while (true) {
                int choice = view.displayMenu("Rooms Menu", "What do you want to do?: ", new String[] {"Enter Room", "Search Room", "Exit"});

                if (choice == 1) {
                    String name = view.queryString("name");
                    if (model.nameExist(name)) {
                        roomInteraction(name);
                    } else {
                        view.displayMessage("No room registered with that name;");
                    }
                } else if (choice == 2) {
                    searchRooms();
                } else {
                    break;
                }
            }
        }
    }

    public void searchRooms() {
        while (true) {
            int choice = view.displayMenu("Search Rooms Menu", "How do you want to search rooms: ", new String[] {"Display All", "Search by Category", "Search by Author", "Exit"});

            if (choice == 1) {
                view.displayMessage(model.getAllRooms());
            } else if (choice == 2) {
                String[] categories = new String[model.gatherRoomsCategories().size()];
                String categoryFilter = view.displayDropDownMenu("Category Filter: ", "Choose a category to filter rooms: ", categories);
                view.displayMessage(model.filterRoomsByCategory(categoryFilter));
            } else if (choice == 3) {
                String[] hosts = new String[model.gatherRoomsHost().size()];
                String hostFilter = view.displayDropDownMenu("Host Filter", "Choose a host to filter rooms: ", hosts);
                view.displayMessage(model.filterRoomsByHost(hostFilter));
            } else {
                break;
            }
        }
    }

    public void createRoom() {
        while (true) {
            String ID = view.queryString("Enter new room ID: ");
            if (!model.idExist(ID)) {
                String name = view.queryString("Enter new room name: ");
                if (!model.nameExist(name)) {
                    String creationDate = view.queryString("Enter room creation date: ");
                    String category = view.queryString("Enter room category: ").toUpperCase();
                    model.createRoom(ID, username, name, creationDate, category);
                    view.displayMessage("Room successfully created!");
                    break;
                } else {
                    view.displayMessage("Name is already use, try with another different.");
                }
            } else {
                view.displayMessage("ID already in registered.");
            }
        }
    }

    public void roomInteraction(String name) {
        while (true) {
            int choice = view.displayMenu("Room Menu", "What do you want to do in this room?: ", new String[] {"Send Message", "Check Messages", "Room Info", "Exit"});

            if (choice == 1) {
                String date = view.queryString("Enter message date: ");
                String message = view.queryString("Enter message content: ");
                model.sendRoomMessage(name, username, date, message);
                view.displayMessage("Message successfully published.");
            } else if (choice == 2) {
                view.displayMessage(model.getRoomMessages(name));
            } else if (choice == 3) {
                view.displayMessage(model.getRoomInfo(name));
            } else {
                break;
            }
        }
    }
}
