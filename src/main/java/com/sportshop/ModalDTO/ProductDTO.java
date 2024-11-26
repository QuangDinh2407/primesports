package com.sportshop.ModalDTO;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String name;

    private int quantity;

    private float price;

    private float rating;

    private String description;

    private String status = "ACTIVE";

    private List<String> imagePaths;

    private List<String> productTypeIds;
}
