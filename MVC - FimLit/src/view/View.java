package view;

import javax.swing.*;

public class View {

    public int displayMenu(String title, String startingMessage, String[] options) {
        StringBuilder message = new StringBuilder(startingMessage);

        for (int i = 0; i < options.length; i++) {
            message.append("\n").append(i + 1).append(". ").append(options[i]);
        }

        while (true) {
            int choice = Integer.parseInt(JOptionPane.showInputDialog(null, message.toString(), title, JOptionPane.QUESTION_MESSAGE));
            if (choice >= 1 && choice <= (options.length + 1))
                return choice;
            displayMessage("Enter a valid option");
        }
    }

    public boolean displayConfirmDialog(String message) {
        int response = JOptionPane.showConfirmDialog(null, message, "Confirmation", JOptionPane.YES_NO_OPTION);
        return (response == JOptionPane.YES_OPTION);
    }

    public void displayMessage(String text) {
        JOptionPane.showMessageDialog(null, text);
    }

    public String displayDropDownMenu(String[] options) {
        return (String) JOptionPane.showInputDialog(null, "Choose an option", "Author Choice:", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    public String queryString(String text) {
        return JOptionPane.showInputDialog(null, text, "Input", JOptionPane.QUESTION_MESSAGE);
    }
}
