package com.sportshop.Service.Iml;

import com.sportshop.Entity.SizeDetailEntity;
import com.sportshop.Repository.SizeDetailRepository;
import com.sportshop.Service.SizeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SizeDetailServiceIml implements SizeDetailService {
    @Autowired
    private SizeDetailRepository sizeDetailRepository;

    @Override
    public void updateProductSize(String product_id, String size_id, int amount){
        SizeDetailEntity sizeDetail = sizeDetailRepository.findByProductIdAndSizeId(product_id, size_id);
        int updateAmount = sizeDetail.getQuantity();
        sizeDetail.setQuantity(updateAmount- amount);
        sizeDetailRepository.save(sizeDetail);
    }
}
