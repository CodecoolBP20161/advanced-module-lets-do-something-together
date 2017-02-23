package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DbHandler {

    private static String DBURL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    public static void setConnection() {
        Properties pro = new Properties();
        FileInputStream in;
        try {
            in = new FileInputStream("./src/main/resources/db.properties");
            pro.load(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
        DBURL = pro.getProperty("DBURL");
        DB_USER = pro.getProperty("DB_USER");
        DB_PASSWORD = pro.getProperty("DB_PASSWORD");
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBURL, DB_USER, DB_PASSWORD);
    }

}
