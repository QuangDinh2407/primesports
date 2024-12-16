package com.sportshop.ModalDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotEmpty(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    private Integer quantity;

    @NotNull(message = "Giá tiền không được để trống")
    @Min(value = 0, message = "Giá tiền phải lớn hơn hoặc bằng 0")
    private Float import_price;

    @NotNull(message = "Giá tiền không được để trống")
    @Min(value = 0, message = "Giá tiền phải lớn hơn hoặc bằng 0")
    private Float price;

    private float rating;

    private String description;

    private String status = "ACTIVE";

    private List<String> imagePaths;

    @NotEmpty(message = "Danh sách loại sản phẩm không được để trống")
    private List<String> productTypeIds;

    private Map<String, Integer> sizeQuantities;

    private Date updated_at;

}
