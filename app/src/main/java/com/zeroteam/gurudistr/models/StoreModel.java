package com.zeroteam.gurudistr.models;

import java.io.Serializable;

public class StoreModel implements Serializable {
    private String id;
    private String name;
    private String lastTradeStatus;
    private Boolean izOptom;
    private Long lastSaleId;


    public StoreModel( ) { }

    public StoreModel(String id, String name, String lastTradeStatus, Boolean izOptom) {
        this.id = id;
        this.name = name;
        this.lastTradeStatus = lastTradeStatus;
        this.izOptom = izOptom;
    }

    public Long getLastSaleId() {
        return lastSaleId;
    }

    public void setLastSaleId(Long lastSaleId) {
        this.lastSaleId = lastSaleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastTradeStatus() {
        return lastTradeStatus;
    }

    public void setLastTradeStatus(String lastTradeStatus) {
        this.lastTradeStatus = lastTradeStatus;
    }

    public Boolean getIzOptom() {
        return izOptom;
    }

    public void setIzOptom(Boolean izOptom) {
        this.izOptom = izOptom;
    }
}
