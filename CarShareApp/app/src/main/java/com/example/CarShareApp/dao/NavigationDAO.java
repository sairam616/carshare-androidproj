package com.example.CarShareApp.dao;


import android.content.Intent;
import android.view.View;
import com.example.CarShareApp.DashboardActivity;

public class NavigationDAO {
    public void callDashboard(View v) {
        Intent intent = new Intent(v.getContext(), DashboardActivity.class);
        v.getContext().startActivity(intent);
    }

}
