package com.example.CarShareApp.ui.home.payment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.CarShareApp.R;
import com.example.CarShareApp.model.Booking;
import com.example.CarShareApp.ui.home.car.RecyclerCarActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class VerifyOTPActivity extends AppCompatActivity {
    // variable initializing
    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    private TextView textViewMobile;
    private ProgressBar progressBar;
    private Button buttonVerify;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    private SharedPreferences sharedPreferences;
    private FirebaseUser user;
    private FirebaseFirestore fStore;
    private float totalMoneyRental = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_otp);

        init();
        setOTPInput();
        setPhoneNumberVerify();
        verifyOTP();

    }

    private void setPhoneNumberVerify() {
        textViewMobile.setText(getIntent().getStringExtra("phoneNumber"));
    }


    private void init() {
        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);
        textViewMobile = findViewById(R.id.textViewMobile);
        progressBar = findViewById(R.id.progressBarVerify);
        buttonVerify = findViewById(R.id.buttonVerify);


        user = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
    }

    private void setOTPInput() {
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void verifyOTP() {
        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputCode1.getText().toString().trim().isEmpty()
                        || inputCode2.getText().toString().trim().isEmpty()
                        || inputCode3.getText().toString().trim().isEmpty()
                        || inputCode4.getText().toString().trim().isEmpty()
                        || inputCode5.getText().toString().trim().isEmpty()
                        || inputCode6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(VerifyOTPActivity.this, "Please enter valid code.", Toast.LENGTH_LONG).show();
                    return;
                }
                String code = inputCode1.getText().toString() + inputCode2.getText().toString() + inputCode3.getText().toString()
                        + inputCode4.getText().toString() + inputCode5.getText().toString() + inputCode6.getText().toString();

                String otp = getIntent().getStringExtra("OTP");

                if (code.equals(otp)) {
                    createBooking();
                } else {
                    Toast.makeText(VerifyOTPActivity.this, "The verification code entered was invalid.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showAlertDialog(int layout) {
        dialogBuilder = new AlertDialog.Builder(VerifyOTPActivity.this);
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

    private void createBooking() {
        if (user != null) {

            sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateFrom = "25/07/2021";
            String dateTo = "30/07/2021";
            String car_id = sharedPreferences.getString("CAR_ID", "2zBqr13LeVO4tnLAtrMn");
            totalMoneyRental = getIntent().getFloatExtra("TotalMoney", -1);
            String userUid = user.getUid();
            Log.e("Total price", String.valueOf(totalMoneyRental));
            Log.e("userUid", userUid);
            Log.e("ID car", car_id);



            Booking booking = null;
            try {
                booking = new Booking(simpleDateFormat.parse(dateFrom), simpleDateFormat.parse(dateTo), totalMoneyRental, 1, car_id, userUid, null);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DocumentReference documentReference = fStore.collection("bookings").document();
            documentReference.set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    String getId = (documentReference.getId());
                    Booking booking = null;
                    try {
                        booking = new Booking(simpleDateFormat.parse(dateFrom), simpleDateFormat.parse(dateTo), totalMoneyRental, 1, car_id, userUid, getId);
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

    private void sendSMS() {
        String massage = "You have successfully paid. Booking time < 2 hours. NOTE: Bring ID to confirm when picking up the car. RTC THANK YOU! ID code: 8648BHJH73728";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("0832511369", null, massage, null, null);
    }
}
