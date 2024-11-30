package com.sportshop.ModalDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTypeDTO {

    private String productType_id;

    private String name;

    private String parent_id;

    private List<String> name_child;
}
