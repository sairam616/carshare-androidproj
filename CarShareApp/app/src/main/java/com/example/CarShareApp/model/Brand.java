package com.example.CarShareApp.model;

import java.util.Date;

public class Brand {
    private String brandId;
    private String brandName;
    private Date brandCreatedDate;
    private Date brandDeletedDate;

    public Brand(){

    }

    public Brand(String brandId, String brandName, Date brandCreatedDate, Date brandDeletedDate) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.brandCreatedDate = brandCreatedDate;
        this.brandDeletedDate = brandDeletedDate;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Date getBrandCreatedDate() {
        return brandCreatedDate;
    }

    public void setBrandCreatedDate(Date brandCreatedDate) {
        this.brandCreatedDate = brandCreatedDate;
    }

    public Date getBrandDeletedDate() {
        return brandDeletedDate;
    }

    public void setBrandDeletedDate(Date brandDeletedDate) {
        this.brandDeletedDate = brandDeletedDate;
    }
}
