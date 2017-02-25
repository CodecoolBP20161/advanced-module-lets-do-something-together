import controller.EmailController;
import dao.DbHandler;
import service.EmailService;

import static spark.Spark.*;


public class Server {
    private static EmailController emailController;

    public static void main(String[] args) {
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        port(60227);

        emailController = new EmailController(EmailService.getInstance());
        DbHandler.setConnection();

        get("/", emailController::sendToAddresses);
        get("/sent", emailController::sentAddresses);
    }
}