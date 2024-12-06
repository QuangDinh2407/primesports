package com.sportshop.Converter;


import com.sportshop.Entity.PaymentTypeEntity;
import com.sportshop.ModalDTO.PaymentTypeDTO;
import org.springframework.stereotype.Component;


@Component
public class PaymentConverter {

    public PaymentTypeDTO toDTO(PaymentTypeEntity paymentTypeEntity){

        return PaymentTypeDTO.builder()
                .name(paymentTypeEntity.getName())
                .build() ;
    }
}
