package model.postModel;

public class Post {
    private String name;
    private String category;
    private String publishedDate;
    private String author;
    private String content;


    public Post(String name, String category, String publishedDate, String author, String content) {
        this.name = name;
        this.category = category;
        this.publishedDate = publishedDate;
        this.author = author;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                ", Category: " + category +
                ", Published Date: " + publishedDate +
                ", Author: " + author +
                ", Content: " + content + '.';
    }
}
