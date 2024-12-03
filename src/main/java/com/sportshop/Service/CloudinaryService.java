package com.sportshop.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    String uploadFileToFolder(MultipartFile file, String folderName) throws IOException;
}
