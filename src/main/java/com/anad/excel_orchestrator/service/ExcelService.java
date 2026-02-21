package com.anad.excel_orchestrator.service;

import com.anad.excel_orchestrator.model.Customer;
import com.anad.excel_orchestrator.repository.CustomerRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ExcelService {
    @Autowired private CustomerRepository repository;
    @Autowired private SimpMessagingTemplate messagingTemplate;

    @Async
    public void processExcel(Path filePath, String taskId) {
        try (Workbook workbook = new XSSFWorkbook(Files.newInputStream(filePath))) {
            Sheet sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getPhysicalNumberOfRows() - 1;

            for (int i = 1; i <= totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Customer c = new Customer();
                c.setName(row.getCell(0).getStringCellValue());
                c.setEmail(row.getCell(1).getStringCellValue());
                repository.save(c);

                int progress = (int) ((double) i / totalRows * 100);
                messagingTemplate.convertAndSend("/topic/progress/" + taskId, progress);

                Thread.sleep(100); // Slow down so you can see the progress bar move!
            }
            Files.deleteIfExists(filePath); // Clean up
        } catch (Exception e) {
            messagingTemplate.convertAndSend("/topic/progress/" + taskId, "Error");
        }
    }
}
