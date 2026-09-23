package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    public static List<Event> readEvents(String filePath)
            throws IOException {

        List<Event> events = new ArrayList<>();

        FileInputStream file =
                new FileInputStream(filePath);

        Workbook workbook =
                new XSSFWorkbook(file);

        Sheet sheet =
                workbook.getSheetAt(0);

        for (Row row : sheet) {

            if (row.getRowNum() == 0) {
                continue;
            }

            if (row.getCell(0) == null) {
                continue;
            }


            // A = Event Name
            String eventName =
                    getStringValue(row.getCell(0));


            // D = Organization
            String organization =
                    getStringValue(row.getCell(3));


            // E = Event Location
            String location =
                    getStringValue(row.getCell(4));


            // F = Indoor/Outdoor
            String setting =
                    getStringValue(row.getCell(5));


            // G = Sport
            String sportText =
                    getStringValue(row.getCell(6));

            boolean sport =
                    sportText.equalsIgnoreCase("Yes");


            // H = Music
            String musicText =
                    getStringValue(row.getCell(7));

            boolean music =
                    musicText.equalsIgnoreCase("Yes");


            // I = Start year
            int startYear =
                    getIntValue(row.getCell(8));


            // J = End year
            int endYear =
                    getIntValue(row.getCell(9));


            // K = Start month
            int startMonth =
                    getIntValue(row.getCell(10));


            // L = End month
            int endMonth =
                    getIntValue(row.getCell(11));


            // M = Start day
            int startDay =
                    getIntValue(row.getCell(12));


            // N = End day
            int endDay =
                    getIntValue(row.getCell(13));


            // O = Duration
            int duration =
                    getIntValue(row.getCell(14));

            Event event = new Event(
                    eventName,
                    organization,
                    location,
                    setting,
                    sport,
                    music,
                    startYear,
                    endYear,
                    startMonth,
                    endMonth,
                    startDay,
                    endDay,
                    duration
            );

            events.add(event);
        }

        workbook.close();

        file.close();

        return events;
    }

    private static String getStringValue(Cell cell) {

        if (cell == null) {
            return "";
        }

        return cell.toString().trim();
    }

    private static int getIntValue(Cell cell) {

        if (cell == null) {
            return 0;
        }

        if (cell.getCellType() == CellType.NUMERIC) {

            return (int) cell.getNumericCellValue();
        }


        try {

            return Integer.parseInt(
                    cell.toString().trim()
            );

        } catch (NumberFormatException e) {

            return 0;
        }
    }
}