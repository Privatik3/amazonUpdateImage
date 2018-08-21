package manager;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskManager {

    private static Logger log = Logger.getLogger("");
    private static CopyOnWriteArrayList<Task> tasks = new CopyOnWriteArrayList<>();

    public static void initTask(String token, HashMap<String, String> parameters) {

        try {
            Task task = new Task(token, parameters);
            tasks.add(task);
        } catch (Exception e) {
            log.info("-------------------------------------------------");
            log.log(Level.SEVERE, "Не удалось инициализировать таск");
            log.log(Level.SEVERE, "Exception: ", e);
            e.printStackTrace();
        }
    }

    public static void doTask() {
        try {
            if (tasks.size() > 0) {
                Task task = tasks.get(0);
                tasks.remove(task);

                task.start();
            }
        } catch (Exception ignored) {}
    }
}
