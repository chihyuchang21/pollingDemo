package com.example.pollingdemo.controller.dispose;
import com.example.pollingdemo.service.LongPollingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortPollingController {

    @Autowired
    LongPollingService longPollingService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/short-polling")
    public String shortPolling() {
        boolean hasChanged = checkProductChange(1, 399, 10);
        if (hasChanged) {
            return "Product Changed!";
        } else {
            return "No Change"; // 立即返回狀態
        }
    }

    private boolean checkProductChange(Integer productId, Integer expectedPrice, Integer expectedStock) {
        String sql = "SELECT price, stock FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, (rs, rowNum) -> {
            int currentPrice = rs.getInt("price");
            int currentStock = rs.getInt("stock");
            // 檢查是否與預期值不同
            return currentPrice != expectedPrice || currentStock != expectedStock;
        });
    }
}
