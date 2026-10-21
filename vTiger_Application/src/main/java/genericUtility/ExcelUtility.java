package genericUtility;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtility {

    public String getDataFromExcel(
            String sheetName, int rowNum, int cellNum)
            throws IOException {

        String filePath =
                "./src/main/resources/Vtiger_Test_Data.xlsx";

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook wb = WorkbookFactory.create(fis)) {

            Sheet sheet = wb.getSheet(sheetName);

            if (sheet == null) {
                throw new IOException(
                    "Excel sheet not found: " + sheetName);
            }

            Row row = sheet.getRow(rowNum);

            if (row == null) {
                return "";
            }

            if (row.getCell(cellNum) == null) {
                return "";
            }

            DataFormatter formatter = new DataFormatter();

            return formatter.formatCellValue(
                    row.getCell(cellNum));
        }
    }
}
