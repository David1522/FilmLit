package controller;

import model.libraryModel.LibraryModel;
import model.libraryModel.Work;
import view.LibraryView;

import java.util.HashMap;

public class LibraryController {
    private final LibraryModel model;
    private final LibraryView view = new LibraryView();

    public LibraryController(HashMap<String, Work> userLibrary, String username) {
        this.model = new LibraryModel(userLibrary, username);
    }

    public void initModule() {
        view.displayMessage("Welcome to the Library Module!");

        while (true) {
            int choice = view.displayMenu("Library Menu", "Where you want to go?", new String[] {"Works DataBase", "User Library", "Exit"});

            if (choice == 1) {
                dbController();
            } else if (choice == 2) {
                userLibraryController();
            } else {
                break;
            }
        }
    }

    public void dbController() {
        while (true) {
            int choice = view.displayMenu("Data Base Menu", "What do you want to do?", new String[] {"Add Work", "Edit Work", "Remove Work", "Exit"});

            if (choice == 1) {
                String ID = view.queryString("Enter new work ID: ");
                if (!model.IDExist(ID)) {
                    String name = view.queryString("Enter new work name: ");
                    String author = view.queryString("Enter new work author: ");
                    String year = view.queryString("Enter new work published year: ");
                    String category = view.queryString("Enter new work category: ");
                    model.addWork(ID, new Work(name, year, author, category));
                    view.displayMessage("Work Successfully Added!");
                } else {
                    view.displayMessage("ID is already registered, try again.");
                }
            } else if (choice == 2) {
                String ID = view.queryString("Enter the work ID: ");
                if (model.IDExist(ID)) {
                    while (true) {
                        int editChoice = view.displayMenu("Edit Work Menu", "What do you want to edit?", new String[] {"Name", "Author", "Year", "Category", "Exit"});

                        if (editChoice == 1) {
                            String updated_name = view.queryString("Enter updated work name: ");
                            model.updateWorkName(ID, updated_name);
                            view.displayMessage("Name Successfully Edited!");
                        } else if (editChoice == 2) {
                            String updated_author = view.queryString("Enter updated work author: ");
                            model.updateWorkAuthor(ID, updated_author);
                            view.displayMessage("Author Successfully Edited!");
                        } else if (editChoice == 3) {
                            String updated_year = view.queryString("Enter updated work year: ");
                            model.updateWorkYear(ID, updated_year);
                            view.displayMessage("Year Successfully Edited!");
                        } else if (editChoice == 4) {
                            String updated_category = view.queryString("Enter updated work category: ");
                            model.updateWorkCategory(ID, updated_category);
                            view.displayMessage("Category Successfully Edited!");
                        } else {
                            break;
                        }
                    }
                }
            } else if (choice == 3) {
                String ID = view.queryString("Enter work ID to remove: ");
                if (model.IDExist(ID)) {
                    model.removeWorkLibrary(model.getWorkNameByID(ID));
                    model.removeWork(ID);
                    view.displayMessage("Work Successfully Removed!");
                }
            } else {
                break;
            }
        }
    }

    public void userLibraryController() {
        while (true) {
            int choice = view.displayMenu("Library Menu", "What do you want to do?", new String[] {"Add Work", "Rate Work", "Remove Work", "Search Work", "Display Library", "Exit"});

            if (choice == 1) {
                String name = view.queryString("Enter work name: ");
                if (model.workInLibrary(name)) {
                    view.displayMessage("Work is already in library!");
                } else if (!model.workExist(name)) {
                    view.displayMessage("There is not any work registered with that name: ");
                } else {
                    model.addWorkLibrary(name);
                    view.displayMessage("Work Successfully Added to Library!");
                }
            } else if (choice == 2) {
                String name = view.queryString("Enter work name: ");
                if (model.workInLibrary(name)) {
                    double rate = view.queryWorkRating("Enter work rating: ");
                    String comment = "\n" + view.queryString("Enter work comment: ");
                    model.rateWork(name, rate, comment);
                    view.displayMessage("Rate successfully added to work!");
                } else {
                    view.displayMessage("Work is not registered in user library.");
                }
            } else if (choice == 3) {
                String name = view.queryString("Enter work name: ");
                if (model.workInLibrary(name)) {
                    model.removeWorkLibrary(name);
                    view.displayMessage("Work successfully removed from library!");
                } else {
                    view.displayMessage("Work is not registered in user library.");
                }
            } else if (choice == 4) {
                while (true) {
                    int searchChoice = view.displayMenu("Search Menu", "What do you want to search?", new String[] {"All Works", "Search by Author", "Search by Category", "Exit"});

                    if (searchChoice == 1) {
                        view.displayMessage(model.toString());
                    } else if (searchChoice == 2) {
                        String[] authors = new String[model.gatherWorksAuthor().size()];
                        authors = model.gatherWorksAuthor().toArray(authors);
                        String author = view.displayDropDownMenu(authors);
                        view.displayMessage(model.filterWorksByAuthor(author));
                    } else if (searchChoice == 3) {
                        String[] categories = new String[model.gatherWorksCategory().size()];
                        categories = model.gatherWorksCategory().toArray(categories);
                        String category = view.displayDropDownMenu(categories);
                        view.displayMessage(model.filterWorksByCategory(category));
                    } else {
                        break;
                    }
                }
            } else if (choice == 5) {
                view.displayMessage(model.getUserLibrary());
            } else {
                break;
            }
        }
    }
}
