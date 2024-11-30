 package com.sportshop.Modal;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchProduct {
    private String name;

    @Min(value = 0, message = "Giá trị thấp nhất là 0")
    private Float  minPrice;

    @Min(value = 0, message = "Giá trị thấp nhất là 0")
    private Float maxPrice;

    private List<String> types = new ArrayList<>(); ;
    private Float rating;

    @AssertTrue(message = "Khoảng giá không hợp lệ")
    public boolean isPriceValid() {
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            return false;
        }
        return true;
    }

}
