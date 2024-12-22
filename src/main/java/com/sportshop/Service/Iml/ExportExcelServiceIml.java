package com.sportshop.Service.Iml;

import com.sportshop.Modal.Result;
import com.sportshop.Service.ExportExcelService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ExportExcelServiceIml implements ExportExcelService {
    @Override
    public Result exportDashboardDataToExcel(
            HttpServletResponse response,
            Map<String, Object> chartData,
            Map<String, Object> orderStatusCounts,
            Map<String, Object> topSellingProducts,
            Double revenueToday,
            Double revenueTotal,
            Double totalImportPrice,
            Double profit
    ) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Dashboard Data");
        int rowNum = 0;

        // Thêm header và dữ liệu từng phần
        rowNum = addChartData(sheet, chartData, rowNum);
        rowNum = addOrderStatusData(sheet, orderStatusCounts, rowNum);
        rowNum = addTopSellingProducts(sheet, topSellingProducts, rowNum);
        addSummaryData(sheet, revenueToday, revenueTotal, totalImportPrice, profit, rowNum);

        // Tự động điều chỉnh kích thước cột
        for (int i = 0; i < 2; i++) {
            sheet.autoSizeColumn(i);
        }

        // Xuất file
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=dashboard_data.xlsx");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            workbook.close();
            return new Result(true, "Xuất file excel thành công");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "Xuất file excel không thành công: " + e.getMessage());
        }

    }

    private int addChartData(Sheet sheet, Map<String, Object> chartData, int rowNum) {
        // Tạo tiêu đề cho Chart Data
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Chart Data");

        // Lấy dữ liệu labels và values
        List<String> labels = (List<String>) chartData.getOrDefault("labels", Collections.emptyList());
        List<Object> values = (List<Object>) chartData.getOrDefault("values", Collections.emptyList());

        // Duyệt qua danh sách labels và values
        for (int i = 0; i < labels.size(); i++) {

            Row row = sheet.createRow(rowNum++);
            // Thêm label vào cột đầu tiên
            row.createCell(0).setCellValue(labels.get(i));

            // Xử lý value để đảm bảo không gặp lỗi ép kiểu
            Object value = (i < values.size()) ? values.get(i) : null; // Kiểm tra nếu index hợp lệ
            if (value instanceof Number) {
                row.createCell(1).setCellValue(((Number) value).doubleValue()); // Số dạng double
            } else if (value instanceof String) {
                row.createCell(1).setCellValue((String) value); // Chuỗi
            } else if (value == null) {
                row.createCell(1).setCellValue(""); // Giá trị null
            } else {
                row.createCell(1).setCellValue(String.valueOf(value)); // Các loại khác chuyển thành chuỗi
            }
        }
        return rowNum; // Trả về số dòng tiếp theo
    }


    private int addOrderStatusData(Sheet sheet, Map<String, Object> orderStatusCounts, int rowNum) {
        // Tạo tiêu đề cho Order Status
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Order Status");

        // Lấy dữ liệu labels và values
        List<String> labels = (List<String>) orderStatusCounts.getOrDefault("labels_sales", Collections.emptyList());
        List<Object> values = (List<Object>) orderStatusCounts.getOrDefault("values_sales", Collections.emptyList());

        // Duyệt qua danh sách labels và values
        for (int i = 0; i < labels.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            // Thêm label vào cột đầu tiên
            row.createCell(0).setCellValue(labels.get(i));

            // Xử lý value để đảm bảo không gặp lỗi ép kiểu
            Object value = (i < values.size()) ? values.get(i) : null; // Kiểm tra nếu index hợp lệ
            if (value instanceof Number) {
                row.createCell(1).setCellValue(((Number) value).doubleValue()); // Số dạng double
            } else if (value instanceof String) {
                row.createCell(1).setCellValue((String) value); // Chuỗi
            } else if (value == null) {
                row.createCell(1).setCellValue(""); // Giá trị null
            } else {
                row.createCell(1).setCellValue(String.valueOf(value)); // Các loại khác chuyển thành chuỗi
            }
        }
        return rowNum; // Trả về số dòng tiếp theo
    }

    private int addTopSellingProducts(Sheet sheet, Map<String, Object> topSellingProducts, int rowNum) {
        // Tạo tiêu đề cho Top Selling Products
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Top Selling Products");

        // Lấy dữ liệu labels và values
        List<String> labels = (List<String>) topSellingProducts.getOrDefault("labels_product", Collections.emptyList());
        List<Object> values = (List<Object>) topSellingProducts.getOrDefault("values_product", Collections.emptyList());

        // Duyệt qua danh sách labels và values
        for (int i = 0; i < labels.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            // Thêm label vào cột đầu tiên
            row.createCell(0).setCellValue(labels.get(i));

            // Xử lý value để đảm bảo không gặp lỗi ép kiểu
            Object value = (i < values.size()) ? values.get(i) : null; // Kiểm tra nếu index hợp lệ
            if (value instanceof Number) {
                row.createCell(1).setCellValue(((Number) value).doubleValue()); // Số dạng double
            } else if (value instanceof String) {
                row.createCell(1).setCellValue((String) value); // Chuỗi
            } else if (value == null) {
                row.createCell(1).setCellValue(""); // Giá trị null
            } else {
                row.createCell(1).setCellValue(String.valueOf(value)); // Các loại khác chuyển thành chuỗi
            }
        }
        return rowNum; // Trả về số dòng tiếp theo
    }


    private void addSummaryData(Sheet sheet, Double revenueToday, Double revenueTotal, Double totalImportPrice, Double profit, int rowNum) {
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Summary");
        addSummaryRow(sheet, "Revenue Today", revenueToday, rowNum++);
        addSummaryRow(sheet, "Total Revenue", revenueTotal, rowNum++);
        addSummaryRow(sheet, "Total Import Price", totalImportPrice, rowNum++);
        addSummaryRow(sheet, "Profit", profit, rowNum);
    }

    private void addSummaryRow(Sheet sheet, String label, Double value, int rowNum) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(value != null ? value : 0);
    }
}
