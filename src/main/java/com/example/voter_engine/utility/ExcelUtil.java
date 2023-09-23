package com.example.voter_engine.utility;

import com.example.voter_engine.Entity.user;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtil {
    public static List<user> parseExcel(InputStream inputStream) throws IOException {
        List<user> users = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Assuming the columns are in order: Name, Designation, Salary
                String name = row.getCell(0).getStringCellValue();
                String designation = row.getCell(1).getStringCellValue();
                double salary = row.getCell(2).getNumericCellValue();

//                users.add(new user(name, designation, salary));
            }
        }

        return users;
    }
}
