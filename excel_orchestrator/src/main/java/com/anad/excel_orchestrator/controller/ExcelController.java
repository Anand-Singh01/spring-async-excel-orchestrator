package com.anad.excel_orchestrator.controller;

import com.anad.excel_orchestrator.service.ExcelService;
import com.anad.excel_orchestrator.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin("*")
public class ExcelController {
    @Autowired private FileStorageService fileStorageService;
    @Autowired private ExcelService excelService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        String taskId = UUID.randomUUID().toString();
        Path path = fileStorageService.save(file);
        excelService.processExcel(path, taskId);
        return ResponseEntity.ok(taskId);
    }
}
