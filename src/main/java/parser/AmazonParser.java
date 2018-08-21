package parser;

import manager.RequestTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class AmazonParser {

    private static Logger log = Logger.getLogger("");

    public static List<ItemInfo> parseItems(List<RequestTask> tasks) {

        log.info("-------------------------------------------------");
        log.info("Начинаем обработку страниц обьявлений");

        long start = new Date().getTime();

        ArrayList<ItemInfo> result = new ArrayList<>();
        for (RequestTask task : tasks) {
            Document doc = Jsoup.parse(task.getHtml());

            ItemInfo item = new ItemInfo();
            item.setId(task.getId());
            // Здесь гавнярит Александр


            // Здесь уже норм код
            result.add(item);
        }

        log.info("Время затраченое на обработку: " + (new Date().getTime() - start) + " ms");
        return result;
    }
}
