package controller.postController;

import model.postModel.PostModel;
import view.View;

public class PostController {
    private final PostModel model = new PostModel();
    private final View view = new View();
    private final String username;

    public PostController(String username) {
        this.username = username;
    }

    public void initModule() {
        while (true) {
            int choice = view.displayMenu("Post Menu", "What do you want to do?", new String[] {"Add Post", "Display Post", "Edit Post", "Delete Post", "Exit"});

            if (choice == 1) {
                while (true) {
                    String ID = view.queryString("Enter post new ID: ");
                    if (!model.idExist(ID)) {
                        String name = view.queryString("Enter the name");
                        String category = view.queryString("Enter the category: ");
                        String date = view.queryString("Enter the date: ");
                        String author = username;
                        String content = view.queryString("What do you want to post: ");
                        model.agregarPublicacion(ID, name, category, date, author, content);
                        view.displayMessage("Post successfully created!");
                        break;
                    } else {
                        view.displayMessage("ID already registered!");
                    }
                }
            } else if (choice == 2) {
                view.displayMessage(model.mostar());
            } else if (choice == 3) {
                editPostController();
            } else if (choice == 4) {
                String ID = view.queryString("Enter the post ID to delete: ");
                if (model.idExist(ID)) {
                    model.eliminarPublicacion(ID);
                    view.displayMessage("Post successfully deleted.");
                } else {
                    view.displayMessage("Post doesn't exist.");
                }
            } else if (choice == 5) {
                break;
            }
        }
    }

    private void editPostController() {
        while (true) {
            int choice = view.displayMenu("Edit Post Menu", "What do you want to edit: ", new String[] {"Category", "Author", "Content", "Exit"});


            if (choice == 1) {
                String ID = view.queryString("Enter the post ID: ");
                if (model.idExist(ID)) {
                    String newCategory = view.queryString("Enter the new category: ");
                    model.editarCategoria(ID, newCategory);
                    view.displayMessage("Post successfully edited!");
                } else {
                    view.displayMessage("Post doesn't exist.");
                }
            } else if (choice == 2) {
                String ID = view.queryString("Enter the post ID: ");
                if (model.idExist(ID)) {
                    String author = view.queryString("Enter the edited author: ");
                    model.editarAutor(ID, author);
                    view.displayMessage("Post successfully edited!");
                } else {
                    view.displayMessage("Post doesn't exist.");
                }
            } else if (choice == 3) {
                String ID = view.queryString("Enter the post ID: ");
                if (model.idExist(ID)) {
                    String content = view.queryString("Enter the edited content: ");
                    model.editarContenido(ID, content);
                    view.displayMessage("Post successfully edited!");
                } else {
                    view.displayMessage("Post doesn't exist.");
                }
            } else {
                break;
            }
        }
    }
}
