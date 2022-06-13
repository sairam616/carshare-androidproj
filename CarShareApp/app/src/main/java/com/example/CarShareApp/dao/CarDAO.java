package com.example.CarShareApp.dao;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

public class CarDAO {

    public void uploadFile(Uri imgUri, String imgName, Context context,String folder) {
        if (imgUri != null) {

        } else {
            Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

}
