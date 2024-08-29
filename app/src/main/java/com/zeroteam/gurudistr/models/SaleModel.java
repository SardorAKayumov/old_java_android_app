package com.zeroteam.gurudistr.models;

import java.util.Map;

public class SaleModel {
    private long id;
    private long unixtime;
    private Map<String, String> productNames;
    private Map<String, Long> productPrices;
    private Map<String, Long> productQuantities;
    private Map<String, Boolean> productIzPaid;
    private Boolean wasDebt;
    private Boolean izAllPaid;

    public SaleModel() { }

    public SaleModel(long id, long unixtime, Map<String, String> productNames, Map<String, Long> productPrices, Map<String, Long> productQuantities, Map<String, Boolean> productIzPaid, Boolean wasDebt, Boolean izAllPaid) {
        this.id = id;
        this.unixtime = unixtime;
        this.productNames = productNames;
        this.productPrices = productPrices;
        this.productQuantities = productQuantities;
        this.productIzPaid = productIzPaid;
        this.wasDebt = wasDebt;
        this.izAllPaid = izAllPaid;
    }

    public long getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(long unixtime) {
        this.unixtime = unixtime;
    }

    public Boolean getWasDebt() {
        return wasDebt;
    }

    public void setWasDebt(Boolean wasDebt) {
        this.wasDebt = wasDebt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, String> getProductNames() {
        return productNames;
    }

    public void setProductNames(Map<String, String> productNames) {
        this.productNames = productNames;
    }

    public Map<String, Long> getProductPrices() {
        return productPrices;
    }

    public void setProductPrices(Map<String, Long> productPrices) {
        this.productPrices = productPrices;
    }

    public Map<String, Long> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(Map<String, Long> productQuantities) {
        this.productQuantities = productQuantities;
    }

    public Map<String, Boolean> getProductIzPaid() {
        return productIzPaid;
    }

    public void setProductIzPaid(Map<String, Boolean> productIzPaid) {
        this.productIzPaid = productIzPaid;
    }

    public Boolean getIzAllPaid() {
        return izAllPaid;
    }

    public void setIzAllPaid(Boolean izAllPaid) {
        this.izAllPaid = izAllPaid;
    }
}
