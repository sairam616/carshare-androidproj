package com.example.CarShareApp.model;

import java.util.Date;

public class Bill {
private String bookingId;//declare variable bookingId
private String billId;//declare variable billId
private Date billCreateddate;//declare variable billCreateddate
private Date billUpdateddate;//declare variable billUpdateddate
private int billStatus;//declare variable billStatus
public float bookingTotal;//declare variable bookingTotal

    public Bill() {

    }


    public Bill(String bookingId, String billId, Date billCreateddate, Date billUpdateddate, int billStatus,float bookingTotal) {
        this.bookingId = bookingId;
        this.billId = billId;
        this.billCreateddate = billCreateddate;
        this.billUpdateddate = billUpdateddate;
        this.billStatus = billStatus;
        this.bookingTotal=bookingTotal;
    }



    public float getBookingTotal() {
        return bookingTotal;
    }


    public void setBookingTotal(float bookingTotal) {
        this.bookingTotal = bookingTotal;
    }


    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }


    public String getBillId() {
        return billId;
    }


    public void setBillId(String billId) {
        this.billId = billId;
    }


    public Date getBillCreatedDate() {
        return billCreateddate;
    }


    public void setBillCreatedDate(Date billCreateddate) {
        this.billCreateddate = billCreateddate;
    }


    public Date getBillUpdatedDate() {
        return billUpdateddate;
    }


    public void setBillUpdatedDate(Date billUpdatedDate) {
        this.billUpdateddate = billUpdateddate;
    }


    public int getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(int billStatus) {
        this.billStatus = billStatus;
    }
}
