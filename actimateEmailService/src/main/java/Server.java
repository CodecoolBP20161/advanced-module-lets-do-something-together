import config.SenderPropertiesConfig;
import service.EmailService;

import static spark.Spark.exception;
import static spark.Spark.port;


public class Server {
    private static EmailService emailService;

    public static void main(String[] args) {
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        port(60227);

        SenderPropertiesConfig.writeConfig();

        emailService = EmailService.getInstance();
    }
}
