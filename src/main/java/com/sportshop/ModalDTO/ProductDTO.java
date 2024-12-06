package com.sportshop.ModalDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private String product_id;

    private String name;

    private int quantity;

    private float price;

    private float rating;

    private String description;

    private String status = "ACTIVE";

    private List<String> imagePaths;

    private List<String> productTypeIds;

    private Map<String, Integer> sizeQuantities;

    private Date updated_at;

}
