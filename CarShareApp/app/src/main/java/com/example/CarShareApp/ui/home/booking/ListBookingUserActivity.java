package com.example.CarShareApp.ui.home.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CarShareApp.R;
import com.example.CarShareApp.apdapter.user.BookingListUserAdapter;
import com.example.CarShareApp.model.Booking;
import com.example.CarShareApp.ui.home.car.RecyclerCarActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListBookingUserActivity extends AppCompatActivity {
    private RecyclerView recyclerView;//declare variable recyclerView
    private FirestoreRecyclerOptions<Booking> options;//declare variable options
    private FirebaseFirestore fireStore;//declare variable fireStore
    private BookingListUserAdapter adapter;//declare variable adapter
    private FirebaseUser user;//declare variable user
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_booking);
        // get authentication in firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        imgBack = findViewById(R.id.ImageBtnBack);
        fireStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView_booking);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        String userUid = user.getUid();
        Query querybooking = fireStore.collection("bookings").whereEqualTo("userId", userUid);
        options = new FirestoreRecyclerOptions.Builder<Booking>().setQuery(querybooking, Booking.class).build();
        adapter=new BookingListUserAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        backToList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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
