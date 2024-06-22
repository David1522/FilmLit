package view;

import javax.swing.*;

public class LibraryView {
    public LibraryView() {}

    public int mainMenu() {
        while (true) {
            int choice = Integer.parseInt(JOptionPane.showInputDialog(null, "Where you want to go?\n1. Books DataBase\n2. User Library\n3. Exit", "Main Menu", JOptionPane.QUESTION_MESSAGE));
            if (choice >= 1 && choice <= 3)
                return choice;
            displayMessage("Enter a valid option.");
        }
    }

    public int dbMenu() {
        while (true) {
            int choice = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to do?\n1. Add Book\n2. Edit Book\n3. Remove Book\n4. Exit", "Data Base Menu", JOptionPane.QUESTION_MESSAGE));
            if (choice >= 1 && choice <= 4)
                return choice;
            displayMessage("Enter a valid option.");
        }
    }

    public int editBookMenu() {
        while (true) {
            int choice = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to edit?\n1. Name\n2. Author\n3. Year\n4. Category\n5. Exit", "Edit Book Menu", JOptionPane.QUESTION_MESSAGE));
            if (choice >= 1 && choice <= 5)
                return choice;
            displayMessage("Enter a valid option.");
        }
    }

    public int libraryMenu() {
        while (true) {
            int choice = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to do?\n1. Add Book\n2. Rate Book\n3. Remove Book\n4. Search Books\n5. Show Library\n6. Exit", "Library Menu", JOptionPane.QUESTION_MESSAGE));
            if (choice >= 1 && choice <= 6)
                return choice;
            displayMessage("Enter a valid option.");
        }
    }

    public int searchMenu() {
        while (true) {
            int choice = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to search?\n1. All Books\n2. Search by Author\n3. Search by Category\n4. Exit", "Search Menu", JOptionPane.QUESTION_MESSAGE));
            if (choice >= 1 && choice <= 5)
                return choice;
            displayMessage("Enter a valid option.");
        }
    }

    public String queryString(String text) {
        return JOptionPane.showInputDialog(null, text, "Input", JOptionPane.QUESTION_MESSAGE);
    }

    public double queryBookRating(String text) {
        while (true) {
            double rate = Double.parseDouble(JOptionPane.showInputDialog(null, text, "Input", JOptionPane.QUESTION_MESSAGE));
            if (rate >= 1 && rate <= 5)
                return rate;
            displayMessage("Enter a valid option.");
        }
    }

    public void displayMessage(String text) {
        JOptionPane.showMessageDialog(null, text);
    }

    public String displayDropDownMenu(String[] options) {
        return (String) JOptionPane.showInputDialog(null, "Choose an option", "Author Choice:", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }
}
