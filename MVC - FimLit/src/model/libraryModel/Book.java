package model.libraryModel;

import java.util.ArrayList;

public class Book {
    private String name;
    private String year;
    private String author;
    private String category;
    private final ArrayList<Double> rating = new ArrayList<>();
    private final ArrayList<String> comments = new ArrayList<>();
    private double meanRate = 0;

    public Book (String name, String year, String author, String category) {
        this.name = name;
        this.year = year;
        this.author = author;
        this.category = category;
    }

    private void calculateMeanRate() {
        double subTotal = 0;
        for (Double rate : rating) {
            subTotal += rate;
        }
        meanRate = (subTotal / rating.size());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void addRating(double rate, String comment) {
        rating.add(rate);
        comments.add(comment);
        calculateMeanRate();
    }

    @Override
    public String toString() {
        return "Title: " + name +
                ", Year: " + year +
                ", Author: " + author +
                ", Category: " + category +
                ", Rating: " + rating +
                ", Comments: " + comments +
                ", meanRate: " + meanRate + ".\n";
    }
}
