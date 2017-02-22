import static spark.Spark.*;


public class Server {

    public static void main(String[] args) {
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        port(60227);
    }
}
