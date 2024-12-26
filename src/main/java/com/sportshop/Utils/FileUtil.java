package com.sportshop.Utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUtil {

    // Cấu hình kích thước file tối đa (mặc định 2MB)
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB in bytes

    // Kiểm tra phần mở rộng file
    public static boolean validateFileExtension(MultipartFile file) {
        List<String> allowedExtensions = List.of("jpg", "jpeg", "png");
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isBlank()) {
            return false;
        }
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return allowedExtensions.contains(fileExtension);
    }

    // Kiểm tra kích thước file
    public static boolean validateFileSize(MultipartFile file) {
        return file.getSize() <= MAX_FILE_SIZE;
    }

    // Lưu file
    public static String saveFile(MultipartFile file, String uploadDir) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        Path filePath = Path.of(uploadDir, fileName);
        Files.write(filePath, file.getBytes());
        return filePath.toString();
    }
}
