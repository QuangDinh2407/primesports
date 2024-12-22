package com.sportshop.ModalDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sportshop.Contants.FormatDate;
import com.sportshop.Entity.ProductTypeEntity;
import com.sportshop.Entity.ShopCustomerVoucherEntity;
import com.sportshop.Entity.ShopVoucherDetailEntity;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopVoucherDTO {

    private String shopVoucher_id;

    @NotBlank(message="Vui lòng nhập tên mã!")
    private String name;

    @NotBlank(message="Vui lòng nhập mã giảm!")
    private String code;

    private String description;

    @NotNull(message = "Vui lòng nhập giá tri")
    @Min(value = 1, message = "Giá trị trong khoảng 1 đến 100")
    @Max(value = 100, message = "Giá trị trong khoảng 1 đến 100")
    private float discountAmount;

    @DateTimeFormat(pattern = FormatDate.FM_DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Vui lòng nhập ngày bắt đầu")
    private Date started_at;

    @DateTimeFormat(pattern = FormatDate.FM_DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Vui lòng nhập ngày kết thúc")
    private Date ended_at;

    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date created_at;

    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date updated_at;

    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date deleted_at;

    private String status;

//    private List<ShopVoucherDetailEntity> shopVoucherDetailItems = new ArrayList<ShopVoucherDetailEntity>();
//
//    private List <ShopCustomerVoucherEntity> shopCustomerVoucherItems = new ArrayList<ShopCustomerVoucherEntity>();

    private String productTypeName; // Loại sản phẩm

//    private List<ShopVoucherDetailEntity>findSVDByShopVoucher_id;

    // Kiểm tra ngày kết thúc không nhỏ hơn ngày bắt đầu
    @AssertTrue(message = "Ngày kết thúc không được nhỏ hơn ngày bắt đầu")
    public boolean isEndDateValid() {
        if (started_at == null || ended_at == null) {
            return true; // Để @NotNull xử lý riêng trường hợp null
        }
        return !ended_at.before(started_at);
    }
}
