package com.example.CarShareApp.dao;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDAO {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    boolean response;

    public boolean isStatusLogin() {
        return response;
    }

    public void setStatusLogin(boolean statusLogin) {
        this.response = statusLogin;
    }
}
