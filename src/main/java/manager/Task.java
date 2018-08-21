package manager;

import db.DBHandler;
import excel.ExcelManager;
import parser.AmazonParser;
import parser.Item;
import parser.ItemInfo;
import utility.RequestManager;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class Task {

    private static Logger log = Logger.getLogger("");
    private static Boolean DEBUG_MODE = false;

    private ArrayList<RequestTask> reqTasks = new ArrayList<>();
    private List<Item> resultList;

    public Task(String token, HashMap<String, String> param) {

        log.info("-------------------------------------------------");
        log.info("Загружаем список asins с Excel файла");
        resultList = ExcelManager.loadAsinList(param.get("updateFile"));
    }

    public void start() {

        try {
            long startTime = new Date().getTime();

            log.info("-------------------------------------------------");
            log.info("Формируем список запросов на выгрузку страниц");
            HashSet<RequestTask> uniqueTasks = new HashSet<>();
            for (Item item : resultList)
                uniqueTasks.add(new RequestTask(item.getId(), item.getUrl(), ReqTaskType.ITEM));

            reqTasks.addAll(uniqueTasks);
            uniqueTasks.clear();

            List<RequestTask> itemsHtml = RequestManager.execute(reqTasks, DEBUG_MODE);
            reqTasks.clear();

            List<ItemInfo> itemInfo = AmazonParser.parseItems(itemsHtml);
            itemsHtml.clear();

            log.info("-------------------------------------------------");
            log.info("Дополняем результат информацией полечунной со страниц");
            for (ItemInfo info : itemInfo) {
                String infoID = info.getId();

                Optional<Item> first = resultList.stream().filter(itm -> itm.getId().equals(infoID)).findFirst();
                first.ifPresent(ad -> ad.addItemInfo(info));
            }

            log.info("-------------------------------------------------");
            log.info("Формируем список запросов на выгрузку изображений");
            for (ItemInfo info : itemInfo)
                reqTasks.add(new RequestTask(info.getId(), info.getImageUrl(), ReqTaskType.IMAGE));

            int endTime = (int) (new Date().getTime() - (startTime));
            log.info("-------------------------------------------------");
            log.info("ПОЛНОЕ ВРЕМЯ ВЫПОЛНЕНИЯ: " + endTime + " ms");
            log.info("-------------------------------------------------");

            RequestManager.closeClient();
            DBHandler.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
