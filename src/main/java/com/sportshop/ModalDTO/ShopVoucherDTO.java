package com.sportshop.ModalDTO;

import com.sportshop.Contants.FormatDate;
import com.sportshop.Entity.ProductTypeEntity;
import com.sportshop.Entity.ShopCustomerVoucherEntity;
import com.sportshop.Entity.ShopVoucherDetailEntity;
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

    private String name;

    private String code;

    private String description;

    private float discountAmount;

    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date started_at;

    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date ended_at;

    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date created_at;

    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date updated_at;

    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date deleted_at;

    private String status;

    private List<ShopVoucherDetailEntity> shopVoucherDetailItems = new ArrayList<ShopVoucherDetailEntity>();

    private List <ShopCustomerVoucherEntity> shopCustomerVoucherItems = new ArrayList<ShopCustomerVoucherEntity>();

    private String productTypeName; // Loại sản phẩm

    private List<ShopVoucherDetailEntity>findSVDByShopVoucher_id;

}