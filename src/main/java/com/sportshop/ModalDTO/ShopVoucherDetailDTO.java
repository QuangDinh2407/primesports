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
public class ShopVoucherDetailDTO {

    private String shopVoucherDetail_id;

    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date created_at;

    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date updated_at;

    private String productName;

    private String shopVoucherName;
}