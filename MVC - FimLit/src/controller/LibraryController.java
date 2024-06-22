package controller;

import model.Book;
import model.LibraryModel;
import view.LibraryView;

public class LibraryController {
    private final LibraryModel model = new LibraryModel();
    private final LibraryView view = new LibraryView();

    public LibraryController() {}

    public void initModule() {
        view.displayMessage("Welcome to the Library Module!");

        while (true) {
            int choice = view.mainMenu();

            if (choice == 1) {
                dbController();
            } else if (choice == 2) {
                userLibraryController();
            } else {
                view.displayMessage("Thanks for using our services!");
                break;
            }
        }
    }

    public void dbController() {
        while (true) {
            int choice = view.dbMenu();

            if (choice == 1) {
                String ID = view.queryString("Enter new book ID: ");
                if (!model.IDExist(ID)) {
                    String name = view.queryString("Enter new book name: ");
                    String author = view.queryString("Enter new book author: ");
                    String year = view.queryString("Enter new book published year: ");
                    String category = view.queryString("Enter new book category: ");
                    model.addBook(ID, new Book(name, year, author, category));
                    view.displayMessage("Book Successfully Added!");
                } else {
                    view.displayMessage("ID is already registered, try again.");
                }
            } else if (choice == 2) {
                String ID = view.queryString("Enter the book ID: ");
                if (model.IDExist(ID)) {
                    while (true) {
                        int editChoice = view.editBookMenu();

                        if (editChoice == 1) {
                            String updated_name = view.queryString("Enter updated book name: ");
                            model.updateBookName(ID, updated_name);
                            view.displayMessage("Name Successfully Edited!");
                        } else if (editChoice == 2) {
                            String updated_author = view.queryString("Enter updated book author: ");
                            model.updateBookAuthor(ID, updated_author);
                            view.displayMessage("Author Successfully Edited!");
                        } else if (editChoice == 3) {
                            String updated_year = view.queryString("Enter updated book year: ");
                            model.updateBookYear(ID, updated_year);
                            view.displayMessage("Year Successfully Edited!");
                        } else if (editChoice == 4) {
                            String updated_category = view.queryString("Enter updated book category: ");
                            model.updateBookCategory(ID, updated_category);
                            view.displayMessage("Category Successfully Edited!");
                        } else {
                            break;
                        }
                    }
                }
            } else if (choice == 3) {
                String ID = view.queryString("Enter book ID to remove: ");
                if (model.IDExist(ID)) {
                    model.removeBookLibrary(model.getBookNameByID(ID));
                    model.removeBook(ID);
                    view.displayMessage("Book Successfully Removed!");
                }
            } else {
                break;
            }
        }
    }

    public void userLibraryController() {
        while (true) {
            int choice = view.libraryMenu();

            if (choice == 1) {
                String name = view.queryString("Enter book name: ");
                if (model.bookInLibrary(name)) {
                    view.displayMessage("Book is already in library!");
                } else if (!model.bookExist(name)) {
                    view.displayMessage("There is not any book registered with that name: ");
                } else {
                    model.addBookLibrary(name);
                    view.displayMessage("Book Successfully Added to Library!");
                }
            } else if (choice == 2) {
                String name = view.queryString("Enter book name: ");
                if (model.bookInLibrary(name)) {
                    double rate = view.queryBookRating("Enter book rating: ");
                    String comment = "\n" + view.queryString("Enter book comment: ");
                    model.rateBook(name, rate, comment);
                    view.displayMessage("Rate successfully added to book!");
                } else {
                    view.displayMessage("Book is not registered in user library.");
                }
            } else if (choice == 3) {
                String name = view.queryString("Enter book name: ");
                if (model.bookInLibrary(name)) {
                    model.removeBookLibrary(name);
                    view.displayMessage("Book successfully removed from library!");
                } else {
                    view.displayMessage("Book is not registered in user library.");
                }
            } else if (choice == 4) {
                while (true) {
                    int searchChoice = view.searchMenu();

                    if (searchChoice == 1) {
                        view.displayMessage(model.toString());
                    } else if (searchChoice == 2) {
                        String[] authors = new String[model.gatherBooksAuthor().size()];
                        authors = model.gatherBooksAuthor().toArray(authors);
                        String author = view.displayDropDownMenu(authors);
                        view.displayMessage(model.filterBooksByAuthor(author));
                    } else if (searchChoice == 3) {
                        String[] categories = new String[model.gatherBooksCategory().size()];
                        categories = model.gatherBooksCategory().toArray(categories);
                        String category = view.displayDropDownMenu(categories);
                        view.displayMessage(model.filterBooksByCategory(category));
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
