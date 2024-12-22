package com.sportshop.Service;

import com.sportshop.Modal.Result;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public interface ExportExcelService {
    Result exportDashboardDataToExcel(
            HttpServletResponse response,
            Map<String, Object> chartData,
            Map<String, Object> orderStatusCounts,
            Map<String, Object> topSellingProducts,
            Double revenueToday,
            Double revenueTotal,
            Double totalImportPrice,
            Double profit
    ) throws IOException;
}
