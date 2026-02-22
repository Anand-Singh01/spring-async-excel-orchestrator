package com.anad.excel_orchestrator.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path root = Paths.get("uploads");

    public Path save(MultipartFile file) throws Exception {
        if(Files.notExists(root)) Files.createDirectories(root);
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path target = root.resolve(fileName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }
}
