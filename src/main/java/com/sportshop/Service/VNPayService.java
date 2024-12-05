package com.sportshop.Service;

import java.util.Map;

public interface VNPayService {

    String createPaymentUrl(String amount) throws Exception;

    boolean validateReturn(Map<String, String> params);
}
