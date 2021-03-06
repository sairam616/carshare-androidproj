package com.example.CarShareApp.ui.home.payment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CardForm;
import com.braintreepayments.cardform.view.SupportedCardTypesView;
import com.example.CarShareApp.R;
import com.example.CarShareApp.helper.RegexValidate;
import com.example.CarShareApp.ui.home.car.RecyclerCarActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class CreditCardActivity extends AppCompatActivity implements OnCardFormSubmitListener, CardEditText.OnCardTypeChangedListener{
    // variable initializing
    private CardForm cardForm;
    private Button buttonPayment;
    private TextView textViewPriceTotal;
    private ImageView imgBack;
    private FirebaseAuth auth;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCE_OTP= "myPrefsOTP";
    private float totalMoneyRental = 0;
    private SupportedCardTypesView mSupportedCardTypesView;

    private static final CardType[] SUPPORTED_CARD_TYPES = { CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
            CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNIONPAY,
            CardType.HIPER, CardType.HIPERCARD };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_form);

        init();
        backToChoosePayment();
        totalMoney();
        validateCardForm();
        payment();
    }

    private void init(){
        mSupportedCardTypesView = findViewById(R.id.supported_card_types);
        mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);
        cardForm = findViewById(R.id.card_form);

        textViewPriceTotal = findViewById(R.id.txtPriceTotal);
        buttonPayment = findViewById(R.id.btnBuy);
        auth = FirebaseAuth.getInstance();
        auth.setLanguageCode("fr");
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_OTP, Context.MODE_PRIVATE);
        auth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);

    }

    private void validateCardForm(){
        cardForm.cardRequired(true)
                .maskCardNumber(true)
                .maskCvv(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .mobileNumberExplanation("Make sure SMS is supported.")
                .actionLabel("Purchase")
                .setup(this);

        cardForm.setOnCardFormSubmitListener(this);
        cardForm.setOnCardTypeChangedListener(this);

        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        cardForm.getPostalCodeEditText().setInputType(InputType.TYPE_CLASS_NUMBER);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    private void totalMoney(){
        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        int totalDay = getIntent().getIntExtra("TotalDay",-1);

        float car_price = sharedPreferences.getFloat("CAR_PRICE", -1);

        float total = car_price * totalDay + 50 - car_price * 10/100;

        totalMoneyRental = total;
        textViewPriceTotal.setText("$ "+total);
    }

    private void sendOTP(){

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+84"+cardForm.getMobileNumber()) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS)// Timeout and unit
                .setActivity(this)// Activity (for callback binding)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        Intent intent = new Intent(CreditCardActivity.this, VerifyOTPActivity.class);
                        intent.putExtra("phoneNumber",cardForm.getMobileNumber());
                        intent.putExtra("verifyId", verificationId);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // Sign in with the credential
                        // ...
                        signWithCredit(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // ...
                        Log.w("TAG", "onVerificationFailed", e);

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                        }
                        Log.e("sdt",cardForm.getMobileNumber());
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signWithCredit(PhoneAuthCredential phoneAuthCredential){
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(CreditCardActivity.this, VerifyOTPActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(CreditCardActivity.this, "Verification failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void payment(){
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardForm.isValid()) {
                    int length = 6;
                    String otp = String.valueOf(OTP(length));
                    String massage = otp+ RegexValidate.MESSAGE_SEND_OTP;
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("RENTAL",null, massage , null, null);

                    Intent intent = new Intent(CreditCardActivity.this, VerifyOTPActivity.class);
                    intent.putExtra("phoneNumber",cardForm.getMobileNumber());
                    intent.putExtra("OTP", otp);
                    intent.putExtra("TotalMoney",totalMoneyRental);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CreditCardActivity.this, "Please complete the form.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void backToChoosePayment(){

        imgBack = findViewById(R.id.ImageBtnBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecyclerCarActivity.class));
                finish();
            }
        });
    }

    static char[] OTP(int len)
    {
        // Using numeric values
        String numbers = "0123456789";

        // Using random method
        Random rndm_method = new Random();

        char[] otp = new char[len];

        for (int i = 0; i < len; i++)
        {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i] =
                    numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return otp;
    }

    @Override
    public void onCardFormSubmit() {
            //payment();
    }

    @Override
    public void onCardTypeChanged(CardType cardType) {
        if (cardType == CardType.EMPTY) {
            mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);
        } else {
            mSupportedCardTypesView.setSelected(cardType);
        }
    }
}
