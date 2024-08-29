package com.zeroteam.gurudistr.models;

public class ProductModel {
    private long id;
    private String name;
    private long optomPrice;
    private long defaultPrice;

    public ProductModel(){ }

    public ProductModel(long id, String name, long optomPrice, long defaultPrice) {
        this.id = id;
        this.name = name;
        this.optomPrice = optomPrice;
        this.defaultPrice = defaultPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOptomPrice() {
        return optomPrice;
    }

    public void setOptomPrice(long optomPrice) {
        this.optomPrice = optomPrice;
    }

    public long getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(long defaultPrice) {
        this.defaultPrice = defaultPrice;
    }
}
