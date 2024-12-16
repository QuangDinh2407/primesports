package com.sportshop.Service;

import java.util.Map;

public interface VNPayService {

    String createPaymentUrl(Float amount) throws Exception;

    boolean validateReturn(Map<String, String> params);
}
