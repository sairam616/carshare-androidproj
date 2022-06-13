package com.example.CarShareApp.model;

import java.util.Date;

public class Booking {
    public Date bookingPickUpDate;//declare variable bookingPickUpDate
    public Date bookingDropOffDate;//declare variable bookingDropOffDate
    public float bookingTotal;//declare variable bookingTotal
    public int bookingStatus;//declare variable bookingStatus
    public String carId;//declare variable carId
    public String userId;//declare variable userId
    public String bookingId;//declare variable bookingId

    public Booking() {

    }

    public Booking(Date bookingPickUpDate, Date bookingDropOffDate, float bookingTotal, int bookingStatus, String carId, String userId, String bookingId) {
        this.bookingPickUpDate = bookingPickUpDate;
        this.bookingDropOffDate = bookingDropOffDate;
        this.bookingTotal = bookingTotal;
        this.bookingStatus = bookingStatus;
        this.carId=carId;
        this.userId=userId;
        this.bookingId=bookingId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }


    public Date getBookingPickUpDate() {
        return bookingPickUpDate;
    }

    public void setBookingPickUpDate(Date bookingPickUpDate) {
        this.bookingPickUpDate = bookingPickUpDate;
    }


    public Date getBookingDropOffDate() {
        return bookingDropOffDate;
    }

    public void setBookingDropOffDate(Date bookingDropOffDate) {
        this.bookingDropOffDate = bookingDropOffDate;
    }


    public float getBookingTotal() {
        return bookingTotal;
    }

    public void setBookingTotal(float bookingTotal) {
        this.bookingTotal = bookingTotal;
    }


    public int getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(int bookingStatus) {
        this.bookingStatus = bookingStatus;
    }


    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
}
