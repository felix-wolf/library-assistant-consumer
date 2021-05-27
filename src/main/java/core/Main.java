package core;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class Main {


    public static void main(String[] args) {
        System.out.println("start");
        silenceLoggers();
        // DatabaseHandler handler = new DatabaseHandler();
        // handler.deleteAllMailServerInfo();
        // handler.testOperation();
        try {
            MailInfoConsumer.runConsumer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void silenceLoggers() {
        Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers()).forEach(handler ->
                handler.setLevel(Level.SEVERE)
        );
    }

}
