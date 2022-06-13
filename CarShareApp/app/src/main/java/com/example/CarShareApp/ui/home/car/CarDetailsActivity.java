package com.example.CarShareApp.ui.home.car;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.CarShareApp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class CarDetailsActivity extends AppCompatActivity {
    // initialize variable list
    private ImageView imgBack;
    private ImageView imageView;
    private TextView textViewCarName, textViewPriceCar,
            textViewDescriptionCar, textViewSeatCar;
    private RatingBar ratingBarCar;
    private FirebaseFirestore fireStore;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        init();
        getDetailsCar();
        backToList();
    }

    private void init() {
        imageView = findViewById(R.id.imageViewCarDetails);
        textViewCarName = findViewById(R.id.txtCarNameDetails);
        textViewPriceCar = findViewById(R.id.txtPriceCarDetails);
        ratingBarCar = findViewById(R.id.ratingBar_all);
        textViewDescriptionCar = findViewById(R.id.txtDescriptionDetails);
        textViewSeatCar = findViewById(R.id.txtSeatCarDetails);
        imgBack = findViewById(R.id.ImageBtnBack);
        // Access a Cloud FireStore instance from your Activity
        fireStore = FirebaseFirestore.getInstance();
    }


    private void getDetailsCar() {
        DocumentReference docRef = fireStore.collection("cars").document("car-id");

        String carName = getIntent().getStringExtra("carName");
        float carPrice = getIntent().getFloatExtra("carPrice", -1);
        String carImage = getIntent().getStringExtra("carImage");
        String carSeat = getIntent().getStringExtra("carSeat");
        float carRating = getIntent().getFloatExtra("carRating", -1);
        String carDescription = getIntent().getStringExtra("carDescription");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Picasso.get().load(carImage).into(imageView);
                        textViewCarName.setText(carName);
                        textViewPriceCar.setText("$ " + carPrice + "/ Daily");
                        ratingBarCar.setRating(carRating);
                        textViewSeatCar.setText("Car seat: " + carSeat);
                        textViewDescriptionCar.setText(carDescription);

                    } else {
                        Log.e("Response", "No such document");
                    }
                } else {
                    Log.e("Response", String.valueOf(task.getException()));
                }
            }
        });
    }

    private void backToList() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecyclerCarActivity.class));
                finish();
            }
        });
    }

}
