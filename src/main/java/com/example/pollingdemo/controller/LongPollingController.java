package com.example.pollingdemo.controller;
import com.example.pollingdemo.model.Product;
import com.example.pollingdemo.service.LongPollingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@RestController
public class LongPollingController {

    @Autowired
    LongPollingService longPollingService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/long-polling")
    public DeferredResult<String> longPolling() {
        Long timeoutInMillis = 1L;  //等10秒
        String timeoutResponse = "Request Time Out.";
        DeferredResult<String> deferredResult = new DeferredResult<>(timeoutInMillis, timeoutResponse);

        CompletableFuture.runAsync(() -> {
            long startTime = System.currentTimeMillis();
            boolean hasChanged = false;

            while (!hasChanged && (System.currentTimeMillis() - startTime) < timeoutInMillis) {
                try {
                    // 檢查1號產品
                    hasChanged = checkProductChange(1, 399, 10);
                    if (hasChanged) {
                        deferredResult.setResult("Product Changed!");
                        return;
                    }
                    // 暫停5秒後再檢查
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (Exception e) {
                    deferredResult.setErrorResult("Error checking product change: " + e.getMessage());
                    return;
                }
            }

            if (!hasChanged) {
                deferredResult.setResult(timeoutResponse); // 显式设置超时响应
            }
        });
        return deferredResult;
    }

    private boolean checkProductChange(Integer productId, Integer expectedPrice, Integer expectedStock) {
        String sql = "SELECT price, stock FROM product WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, (rs, rowNum) -> {
            int currentPrice = rs.getInt("price");
            int currentStock = rs.getInt("stock");

            // 檢查當前價格或庫存與預期值是否不同
            return currentPrice != expectedPrice || currentStock != expectedStock;
        });
    }
}

//https://stackoverflow.com/questions/49539453/spring-rest-with-long-polling


    //原stackoverflow回答
//    @GetMapping("/long-polling")
//    public DeferredResult<String> longPolling() {
//        // 設置超時時間為20秒
//        Long timeoutInMillis = 20000L;
//        String timeoutResponse = "Request Time Out.";
//
//        DeferredResult<String> deferredResult = new DeferredResult<>(timeoutInMillis, timeoutResponse);
//
//        CompletableFuture.runAsync(() -> {
//            try {
//                // 模擬等待數據庫變更的過程，這裡使用了10秒的睡眠來模擬
//                // 在實際應用中，你應該在這裡檢查數據庫中的變更
//                TimeUnit.SECONDS.sleep(10);
//
//                // 如果在等待期間數據庫發生變化，就設置結果並返回給客戶端
//                // 如果數據庫沒有變化，這個方法也可以被超時響應替代
//                deferredResult.setResult("Data Changed in Database!");
//            } catch (InterruptedException e) {
//                // 處理異常情況
//                deferredResult.setErrorResult(e);
//            }
//        });
//
//        return deferredResult;
//    }

