package com.sportshop.Modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionCart {
    private String cartId;
    private String action;
    private List<String> productId = new ArrayList<>();
    private List<String> size = new ArrayList<>();
    private List<Integer> amount = new ArrayList<>();
}
