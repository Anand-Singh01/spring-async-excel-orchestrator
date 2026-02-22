package com.anad.excel_orchestrator.service;

import com.anad.excel_orchestrator.model.Customer;
import com.anad.excel_orchestrator.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
    public void processExcel(Path path, String taskId) {
        try {
            // 1. Give React 2 seconds to finish the subscription
            // after it receives the taskId


            // 2. Open the file
            Workbook workbook = WorkbookFactory.create(path.toFile());
            Sheet sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getLastRowNum();

            for (int i = 1; i <= totalRows; i++) {
                // ... your row processing logic ...

                // 3. Calculate and send
                int progress = (int) ((double) i / totalRows * 100);

                // LOG THIS so you can see if the backend is actually working
                System.out.println("Task " + taskId + " progress: " + progress + "%");

                messagingTemplate.convertAndSend("/topic/progress/" + taskId, progress);

                // 4. Artificial delay so the bar doesn't jump from 0 to 100 in 1ms
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
