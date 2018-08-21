package excel;

import org.apache.poi.xssf.usermodel.*;
import parser.Item;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ExcelManager {

    private static Logger log = Logger.getLogger(ExcelManager.class.getName());

    public static List<Item> loadAsinList(String pathToList) {

        List<Item> result = new ArrayList<>();

        try {
            XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream( new File("res/" + pathToList)));
            XSSFSheet myExcelSheet = myExcelBook.getSheet("Worksheet");
            for (Integer i = 1 ; myExcelSheet.getLastRowNum() >= i ; i++) {
                XSSFRow row = myExcelSheet.getRow(i);

                String val = row.getCell(207).getStringCellValue();

                int position = 1;
                for (String url : val.split("\n")) {
                    Item item = new Item();
                    String asin = url.substring(url.lastIndexOf("/") + 1);

                    item.setId(asin);
                    item.setRow(i);
                    item.setPosition(position++);
                    item.setUrl(url);

                    result.add(item);
                }
            }
        } catch (Exception e) {
            System.err.println("Не удалось загрузить листинг");
            e.printStackTrace();
        }

        return result;
    }
}
