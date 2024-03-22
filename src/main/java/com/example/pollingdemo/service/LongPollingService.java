package com.example.pollingdemo.service;

import com.example.pollingdemo.model.Product;
import org.springframework.stereotype.Component;

@Component
public class LongPollingService {
    public Product checkDatabaseForChanges() {
        // 模擬數據庫變更檢查
        // 在實際應用中，這裡可以是一個數據庫查詢，檢查是否有符合特定條件的變更
        // 為了示例，我們假設總是有一個產品發生了變更
        Product product = new Product();
        product.setId(1L);
        product.setTitle("Example Product");
        product.setPrice(100);
        product.setStock(20);
        return product;
    }
}