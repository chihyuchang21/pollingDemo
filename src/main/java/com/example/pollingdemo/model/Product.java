package com.example.pollingdemo.model;


import lombok.Data;

@Data
public class Product {
    private Long id;
    private String title;
    private Integer price;
    private Integer stock;
}
