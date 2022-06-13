package com.example.CarShareApp.ui.home.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.CarShareApp.DashboardActivity;
import com.example.CarShareApp.R;
import com.example.CarShareApp.dao.UserDAO;


public class AddUserActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mImage,mPhone;
    Button mRegisterBtn;
    Button mBackBtn;
    ProgressBar progressBar;
    UserDAO userDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        mFullName=(EditText)findViewById(R.id.editFullName);
        mEmail=(EditText)findViewById(R.id.editEmail);
        mPhone=(EditText)findViewById(R.id.editPhoneNumber);
        mImage=(EditText)findViewById(R.id.editImage);

        mBackBtn=(Button)findViewById(R.id.add_back);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }
        });

        mRegisterBtn=(Button)findViewById(R.id.add_submit);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*                final String email = mEmail.getText().toString().trim();
                final String image = mImage.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                final String phone    = mPhone.getText().toString();

                progressBar.setVisibility(View.VISIBLE);

                boolean status = userDAO.addUser(email, image, fullName, phone);
                // register the user in firebase
                if(status){
                    Toast.makeText(AddUserActivity.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                }else {
                    Toast.makeText(AddUserActivity.this, "Add failed", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }*/
            }
        });
    }


}
