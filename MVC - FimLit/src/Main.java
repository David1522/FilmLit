import controller.authController.AuthController;

public class Main {
    public static void main(String[] args) {
        AuthController controller = new AuthController();

        controller.initAuth();
    }
}
