package com.uab.tsp.util;

import com.uab.tsp.model.Results;
import com.uab.tsp.model.Solution;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GenerateReportUtil {

    private static final List<String> headers = List.of("Iteration", "Try Number", "Cities", "Cost", "Frequency");
    private static final String FILE_PATH = "C:\\temp\\";

    public synchronized static void generateReport(Results results) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Results");

        AtomicInteger rowNum = new AtomicInteger();

        createTable(sheet, rowNum, "Local Best Solutions", results.getLocalBestSolutions());
        rowNum.incrementAndGet();
        createTable(sheet, rowNum, "Global Best Solutions", results.getGlobalBestSolutions());

        try {
            FileOutputStream outputStream = new FileOutputStream(new File(FILE_PATH, generateFileName(results)));
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateFileName(Results results) {
        return results.getName() + "_" + new Date().getTime() + ".xlsx";
    }

    private static void createTable(XSSFSheet sheet, AtomicInteger rowNum, String title, List<Solution> solutions) {
        createTableRow(sheet, rowNum, List.of(title));
        createTableRow(sheet, rowNum, GenerateReportUtil.headers);

        solutions.forEach(solution -> createTableRow(sheet, rowNum,
            List.of(Integer.toString(solution.getIteration()),
                    Integer.toString(solution.getTryNumber()),
                    solution.getStringCityList(),
                    Double.toString(solution.cost().doubleValue()),
                    Integer.toString(solution.getFrequency()))));
    }

    private static void createTableRow(XSSFSheet sheet, AtomicInteger rowNum, List<String> headers) {
        Row row = sheet.createRow(rowNum.getAndIncrement());

        int column = 0;
        for(String header : headers) {
            Cell cell = row.createCell(column++);
            cell.setCellValue(header);
        }
    }
}