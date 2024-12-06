package com.sportshop.Service.Iml;

import com.sportshop.Converter.PaymentConverter;
import com.sportshop.ModalDTO.PaymentTypeDTO;
import com.sportshop.Repository.PaymentTypeRepository;
import com.sportshop.Service.PaymentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentTypeServiceIml implements PaymentTypeService {

    @Autowired
    PaymentTypeRepository paymentTypeRepository;

    @Autowired
    PaymentConverter paymentConverter;

    @Override
    public List<PaymentTypeDTO> listPayment() {
        return paymentTypeRepository.findAll().stream().map(paymentConverter::toDTO).collect(Collectors.toList());
    }
}
