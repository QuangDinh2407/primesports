package com.sportshop.Service.Iml;

import com.sportshop.Converter.ShopVoucherConverter;
import com.sportshop.Converter.ShopVoucherDetailConverter;
import com.sportshop.Entity.ShopVoucherDetailEntity;
import com.sportshop.Entity.ShopVoucherEntity;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.ShopVoucherDTO;
import com.sportshop.ModalDTO.ShopVoucherDetailDTO;
import com.sportshop.ModalDTO.UserOrderDTO;
import com.sportshop.Repository.ShopVoucherDetailRepository;
import com.sportshop.Repository.ShopVoucherRepository;
import com.sportshop.Service.ShopVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class ShopVoucherServiceIml implements ShopVoucherService {

    @Autowired
    ShopVoucherRepository shopVoucherRepo;

    @Autowired
    ShopVoucherConverter shopVoucherConverter;

    @Autowired
    ShopVoucherDetailRepository shopVoucherDetailRepo;
    @Autowired
    private ShopVoucherDetailConverter shopVoucherDetailConverter;

    @Override
    public List<ShopVoucherDTO> findAll() {
        // Lấy danh sách các thực thể từ repository
        List<ShopVoucherEntity> lists = shopVoucherRepo.findAll();

        // Trả về danh sách DTO
        return lists.stream().map(shopVoucherConverter::toDTO).collect(Collectors.toList());
    }

    public void updateExpiredVouchers() {
        Date now = new Date(); // Lấy thời gian hiện tại

        // Lấy danh sách tất cả voucher
        List<ShopVoucherEntity> vouchers = shopVoucherRepo.findAll();

        // Duyệt qua danh sách và kiểm tra điều kiện
        for (ShopVoucherEntity voucher : vouchers) {
            if (voucher.getEnded_at().before(now) && !"expired".equalsIgnoreCase(voucher.getStatus())) {
                voucher.setStatus("expired"); // Cập nhật trạng thái thành "expired"
                shopVoucherRepo.save(voucher); // Lưu lại vào cơ sở dữ liệu
            }
        }
    }

    @Override
    public String saveOrUpdateVoucher(ShopVoucherDTO svDTO) {
        try {
            ShopVoucherEntity shopVoucherEntity = shopVoucherRepo.findByCode(svDTO.getCode());

            if (shopVoucherEntity == null) {
                // Nếu không tồn tại, tạo mới
                System.out.println("Voucher không tồn tại. Tạo mới.");
                shopVoucherEntity = new ShopVoucherEntity();
                shopVoucherEntity.setCreated_at(svDTO.getCreated_at()); // Chỉ set khi tạo mới

            } else {
                // Nếu tồn tại, cập nhật thời gian sửa đổi
                System.out.println("Voucher tồn tại. Cập nhật thông tin.");
                shopVoucherEntity.setUpdated_at(svDTO.getUpdated_at());
            }

            // Cập nhật thông tin từ DTO
            shopVoucherEntity.setDiscountAmount(svDTO.getDiscountAmount());
            shopVoucherEntity.setEnded_at(svDTO.getEnded_at());
            shopVoucherEntity.setStarted_at(svDTO.getStarted_at());
            shopVoucherEntity.setCode(svDTO.getCode());
            shopVoucherEntity.setDescription(svDTO.getDescription());
            shopVoucherEntity.setName(svDTO.getName());

            // Xác định trạng thái
            String status = (svDTO.getEnded_at() != null && svDTO.getEnded_at().before(new Date())) ? "expired" : "active";
            shopVoucherEntity.setStatus(status);

            // Lưu entity vào repository
            shopVoucherRepo.save(shopVoucherEntity);

            return "Thay đổi thông tin thành công!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Thay đổi thông tin thất bại!";
        }
    }

    @Override
    public String deleteInfoVoucher(ShopVoucherDTO svDTO) {
        System.out.println(svDTO.getShopVoucher_id());
        try{
            shopVoucherDetailRepo.deleteProductInVoucher(svDTO.getShopVoucher_id());
            ShopVoucherEntity shopVoucherEntity = shopVoucherRepo.findByCode(svDTO.getCode());
            shopVoucherRepo.delete(shopVoucherEntity);
            return "Thay đổi thông tin thành công!";
        }catch(Exception e){
            e.printStackTrace();
            return "Thay đổi thông tin thất bại !";
        }
    }

    @Override
    public Result findByCodeAndProductId(String code, UserOrderDTO userOrderDTO) {
        try {
            ShopVoucherEntity shopVoucherEntity = shopVoucherRepo.findByCode(code);
            if (shopVoucherEntity == null)
            {
                return new Result(false, "Mã giảm giá không hợp lệ!");
            }
            ShopVoucherDTO shopVoucherDTO = shopVoucherConverter.toDTO(shopVoucherEntity);
            boolean isVoucherApplied = userOrderDTO.getUserOrderDetails().stream()
                    .anyMatch(item -> {
                        ShopVoucherDetailEntity shopVoucherDetailEntity = shopVoucherDetailRepo.findShopVoucherDetailByVoucherAndProduct(
                                shopVoucherDTO.getShopVoucher_id(),
                                item.getProduct().getProduct_id()
                        );
                        if (shopVoucherDetailEntity != null) {
                            Date now = new Date(System.currentTimeMillis());
                            Date voucherExpiryDate = shopVoucherDTO.getEnded_at();
                            if (now.before(voucherExpiryDate)) {
                                item.setShopVoucher(shopVoucherDTO);
                                return true;
                            } else {
                                return false;
                            }
                        }
                        return false;
                    });
            // Trả kết quả
            if (isVoucherApplied)
            {
                Float totalAmount = (float) userOrderDTO.getUserOrderDetails().stream()
                        .mapToDouble(item -> {
                            if (item.getShopVoucher() != null) {
                                return (item.getProduct().getPrice() * item.getAmount()) * (1 - item.getShopVoucher().getDiscountAmount() / 100);
                            } else {
                                return item.getProduct().getPrice() * item.getAmount();
                            }
                        })
                        .sum();
                userOrderDTO.setTotal_price(totalAmount);
            }

            return isVoucherApplied
                    ? new Result(true, "Thêm mã giảm giá thành công!")
                    : new Result(false, "Mã giảm giá không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "Mã giảm giá không hợp lệ!");
        }
    }
}