package config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class SenderPropertiesConfig {
    private static final String FILE_PATH = "./src/main/resources/sender.properties";
    private static String SENDER_ADDRESS = "SENDER_ADDRESS";
    private static String SENDER_PWD = "SENDER_PWD";

    public static void writeConfig() {
        Properties props = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream(FILE_PATH);
            props.setProperty(SENDER_ADDRESS, "actimate.app@gmail.com");
            props.setProperty(SENDER_PWD, "codecool");
            props.store(output, null);
        } catch (IOException io) {
            io.getMessage();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.getMessage();
                }
            }
        }
    }

    public static Map<String, String> readConfig() {
        Properties props = new Properties();
        FileInputStream in;
        try {
            in = new FileInputStream(FILE_PATH);
            props.load(in);
        } catch (IOException e) {
            e.getMessage();
        }
        return new HashMap<String, String>() {{
            put(SENDER_ADDRESS, props.getProperty(SENDER_ADDRESS));
            put(SENDER_PWD, props.getProperty(SENDER_PWD));
        }};
    }
}
