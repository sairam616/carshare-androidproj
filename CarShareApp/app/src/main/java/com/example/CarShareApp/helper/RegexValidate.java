package com.example.CarShareApp.helper;

import com.example.CarShareApp.dao.CallbackValidation;

public class RegexValidate{

    public static final String VALID_FULL_NAME = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_]+$";
    public static final String VALID_EMAIL = "^([a-zA-Z0-9]+[\\._-]??[a-zA-Z0-9]+)+@{1}?([a-zA-Z0-9]+[\\._-]??[a-zA-Z0-9]+)+[\\.com|\\.net|\\.org|\\.vn]$";
    public static final String VALID_PHONE_NUMBER = "([0-9]{10})\\b";
    public static final String VALID_PASSWORD = "^.*(?=.{8,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!*@#%^&+=]).*";


    public static final String MESSAGE_ERROR_CONFIRM_PASSWORD = "Password and confirm password not matching.";
    public static final String MESSAGE_ERROR_FULL_NAME = "Full name excluding numbers and special characters.";
    public static final String MESSAGE_ERROR_EMAIL = "Incorrect email format, eg admin@gmail.com";
    public static final String MESSAGE_ERROR_PHONE_NUMBER = "Phone number malformed e.g. 84832511369.";
    public static final String MESSAGE_ERROR_PASSWORD = "Password must be at least 8 characters including uppercase, lowercase letters, numbers and special characters.";
    public static final String MESSAGE_SHOW_RESET_PASSWORD = "Enter the email associated with your account and we'll send an email with instructions to reset your password.";

    public static final String MESSAGE_SEND_OTP = " is your transaction code (OTP). NOTE: Absolutely DO NOT share with anyone in any way. Contact hotro@rentalcars.vn";
    public static void checkPhoneExist(String input, String regex, CallbackValidation validation){
        if(!input.matches(regex)){
            validation.response(false);
        }
    }
    public static void  checkEmpty(String value,CallbackValidation validation){
        if(!value.isEmpty()){
            validation.response(false);
        }
    }
}
