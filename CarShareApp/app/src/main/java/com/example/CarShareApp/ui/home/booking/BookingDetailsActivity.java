package com.example.CarShareApp.ui.home.booking;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.CarShareApp.R;
import com.example.CarShareApp.model.Booking;
import com.example.CarShareApp.ui.home.car.RecyclerCarActivity;
import com.example.CarShareApp.ui.home.payment.ChooseTimeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BookingDetailsActivity extends AppCompatActivity {

    // variable initializing
    private Button buttonChangeTime, buttonVerifyCode, buttonAddBooking;
    private EditText editTextVoucher;
    private TextView textViewTime, textViewPriceCar, textViewDiscount, textViewInsurance, textViewTotalMoney, textViewDate;
    private ImageView imgBack;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCE_TOTAL_BOOKING= "myPrefsBooking";
    private FirebaseUser user;
    private FirebaseFirestore fStore;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    private float totalMoneyRental = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        ActivityCompat.requestPermissions(BookingDetailsActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);


        init();
        changeTime();
        addBooking();
        backToList();
        totalMoney();
        setDateFromChooseTime();
        setDateFromChooseDate();
    }
    private void setDateFromChooseTime(){
        String timeFrom = getIntent().getStringExtra("timeFrom");
        String timeTo = getIntent().getStringExtra("timeTo");
        textViewTime.setText("Time: "+timeFrom+" AM"+ " -> " + timeTo+" PM");
    }


    private void setDateFromChooseDate(){
        String dateFrom = getIntent().getStringExtra("dateFrom");
        String dateTo = getIntent().getStringExtra("dateTo");
        textViewDate.setText(dateFrom+ "   ->   " + dateTo);
    }


    private void init(){
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_TOTAL_BOOKING, Context.MODE_PRIVATE);

        buttonChangeTime = findViewById(R.id.buttonChangeTime);
        buttonVerifyCode = findViewById(R.id.buttonVerifyCode);
        buttonAddBooking = findViewById(R.id.buttonAddBooking);
        // get authentication in firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        editTextVoucher = findViewById(R.id.editTextVoucher);
        textViewDate = findViewById(R.id.textViewDate);
        textViewTime = findViewById(R.id.textViewTime);
        textViewPriceCar = findViewById(R.id.textViewPriceCar);
        textViewDiscount = findViewById(R.id.textViewDiscount);
        textViewInsurance = findViewById(R.id.textViewInsurance);
        textViewTotalMoney = findViewById(R.id.textViewTotalMoney);
        imgBack = findViewById(R.id.ImageBtnBack);
    }

    private void changeTime(){
        buttonChangeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChooseTimeActivity.class));
                finish();
            }
        });
    }


    private void totalMoney(){
        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        int totalDay = getIntent().getIntExtra("TotalDay",-1);

        float car_price = sharedPreferences.getFloat("CAR_PRICE", -1);

        float total = car_price * totalDay + 50 - car_price * 10/100;
        totalMoneyRental = total;

        textViewPriceCar.setText("$ "+ (int) car_price);
        textViewTotalMoney.setText("$ "+total);
    }


    private void addBooking(){
        buttonAddBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBooking();
            }
        });
    }


    private void backToList(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChooseTimeActivity.class));
                finish();
            }
        });
    }


    private void createBooking(){
        if (user != null) {

            sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateFrom = getIntent().getStringExtra("dateFrom");
            String dateTo = getIntent().getStringExtra("dateTo");
            String car_id = sharedPreferences.getString("CAR_ID", "2zBqr13LeVO4tnLAtrMn");

            String userUid = user.getUid();
            Booking booking = null;
            try {
                booking = new Booking(simpleDateFormat.parse(dateFrom), simpleDateFormat.parse(dateTo), totalMoneyRental, 1,car_id, userUid,null);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DocumentReference documentReference =  fStore.collection("bookings").document();
            documentReference.set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    String getId= (documentReference.getId());
                    Booking booking = null;
                    try {
                        booking = new Booking(simpleDateFormat.parse(dateFrom), simpleDateFormat.parse(dateTo), totalMoneyRental, 1,car_id, userUid,getId);
                        showAlertDialog(R.layout.dialog_success);
                        sendSMS();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.e("ID:", getId);
                    fStore.collection("bookings").document(getId).set(booking);
                }
            });
        }
    }


    private void showAlertDialog(int layout){
       dialogBuilder = new AlertDialog.Builder(BookingDetailsActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        Button dialogButton = layoutView.findViewById(R.id.btnDialog);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                startActivity(new Intent(getApplicationContext(), RecyclerCarActivity.class));
                finish();
            }
        });
    }

    private void sendSMS(){
        String timeFrom = getIntent().getStringExtra("timeFrom");
        String dateFrom = getIntent().getStringExtra("dateFrom");
        String massage = "L???CH ?????T: V??o l??c "+timeFrom +" - "+dateFrom+ ". Chi nh??nh C???n Th??. Th???i gian gi??? ?????t xe < 2h. L??U ??: Mang theo CMND ????? x??c nh???n khi ?????n l???y xe. RTC xin C???M ??N!";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("0832511369",null,massage, null, null);
    }
}
