package com.sportshop.Service.Iml;

import com.sportshop.Converter.SizeConverter;
import com.sportshop.Entity.SizeEntity;
import com.sportshop.ModalDTO.SizeDTO;
import com.sportshop.Repository.SizeRepository;
import com.sportshop.Service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SizeServiceIml implements SizeService {
    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private SizeConverter sizeConverter;

    @Transactional
    @Override

    public List<SizeDTO> showSizesProduct() {
        List<SizeEntity> sizeEntityList = sizeRepository.findAll();
        return sizeEntityList.stream()
                .map(sizeConverter::toDTO)
                .collect(Collectors.toList());
    }

}
