package com.sportshop.ModalDTO;

import com.sportshop.Contants.FormatDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewDTO {
    private String productReview_id;
    private String product_id;
    private String userInfo_id;
    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date create_date;
    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date update_date;
    private String productName; // Nếu cần thông tin sản phẩm
    private Float rating;
    private String comment;

}
