package com.example.CarShareApp.model;

import java.util.Date;

public class Payment {
    private int paymentId;
    private String paymentName;
    private int Status;
    private Date paymentCreatedDate;
    private Date paymentDeletedDate;

    public Payment() {

    }

    public Payment(int paymentId, String paymentName, int status, Date paymentCreatedDate, Date paymentDeletedDate) {
        this.paymentId = paymentId;
        this.paymentName = paymentName;
        Status = status;
        this.paymentCreatedDate = paymentCreatedDate;
        this.paymentDeletedDate = paymentDeletedDate;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public Date getPaymentCreatedDate() {
        return paymentCreatedDate;
    }

    public void setPaymentCreatedDate(Date paymentCreatedDate) {
        this.paymentCreatedDate = paymentCreatedDate;
    }

    public Date getPaymentDeletedDate() {
        return paymentDeletedDate;
    }

    public void setPaymentDeletedDate(Date paymentDeletedDate) {
        this.paymentDeletedDate = paymentDeletedDate;
    }
}
