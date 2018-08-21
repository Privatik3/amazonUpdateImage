import manager.TaskManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.*;

public class Main {

    public static void main(String[] args) {

        Logger system = Logger.getLogger("");
        java.util.logging.Handler[] handlers = system.getHandlers();
        system.removeHandler(handlers[0]);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return
                        new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()) + " -> " +
                                record.getMessage() + "\r\n";
            }
        });
        system.addHandler(handler);
        system.setUseParentHandlers(false);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("updateFile", "7010_result.xlsx");

        system.info("Инициализируем новый таск");
        TaskManager.initTask("646421", parameters);
        TaskManager.doTask();
    }
}
