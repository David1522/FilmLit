package view;

import javax.swing.*;

public class LibraryView extends View {
    public LibraryView() {}

    public double queryBookRating(String text) {
        while (true) {
            double rate = Double.parseDouble(JOptionPane.showInputDialog(null, text, "Input", JOptionPane.QUESTION_MESSAGE));
            if (rate >= 1 && rate <= 5)
                return rate;
            displayMessage("Enter a valid option.");
        }
    }
}
