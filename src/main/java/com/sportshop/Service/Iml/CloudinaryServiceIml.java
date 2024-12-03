package com.sportshop.Service.Iml;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sportshop.Service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceIml implements CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    public String uploadFileToFolder(MultipartFile file, String folderName) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folderName // Chỉ định folder
                )
        );
        return uploadResult.get("url").toString(); // Trả về URL ảnh
    }
}
