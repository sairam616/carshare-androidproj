package com.example.CarShareApp.ui.admin.car;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.CarShareApp.R;
import com.example.CarShareApp.dao.CarDAO;
import com.example.CarShareApp.model.Car;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class CreateCarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final int PICK_IMAGE_REQUEST = 1;
    private Button btnCreate;
    private TextInputLayout txtCarName, txtCarPrice, txtColor, txtSeat, txtDescription, txtCarLicensePlates;
    private TextView txtCarBrand;
    private ImageView imgCar;
    private Uri imgUri;
    FirebaseFirestore fStore;
    CarDAO carDAO;
    private StorageReference mStorageRef;
    ArrayAdapter<String> adapter;
    String[] brand = {"Choose brand", "Audi", "Toyota", "Ford", "Honda", "Hyundai", "BMW","Vinfast"};
    Spinner spinner;
    String carBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carmanagement_create);
        btnCreate = findViewById(R.id.btnCreateCar);
        txtCarName = findViewById(R.id.txtCarname);
        txtCarPrice = findViewById(R.id.txtCarprice);
        txtCarBrand = findViewById(R.id.txtCarBrand);
        spinner = findViewById(R.id.spinnerBrand);
        txtColor = findViewById(R.id.txtCarColor);
        txtSeat = findViewById(R.id.txtCarSeat);
        txtDescription = findViewById(R.id.txtCarDescription);
        txtCarLicensePlates = findViewById(R.id.txtLicensePlates);
        carDAO = new CarDAO();
        imgCar = findViewById(R.id.imgCar);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brand);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(this);
        imgCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });
        fStore = FirebaseFirestore.getInstance();
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String imgName = "";
                if (imgUri != null) {
                    imgName = System.currentTimeMillis()
                            + "." + getFileExtension(imgUri);
                }
                if (imgUri != null) {
                    mStorageRef = FirebaseStorage.getInstance().getReference("car_images");
                    StorageReference fileReference = mStorageRef.child(imgName);
                    fileReference.putFile(imgUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri downloadUrl) {
                                            Car newCar = new Car();

                                            Log.e("ERR", downloadUrl.toString());
                                            String carName = String.valueOf(txtCarName.getEditText().getText());
                                            float carPrice = Float.parseFloat(String.valueOf(txtCarPrice.getEditText().getText()));
                                            String carColor = String.valueOf(txtColor.getEditText().getText());
                                            String carSet = String.valueOf(txtSeat.getEditText().getText());
                                            String carDescription = String.valueOf(txtDescription.getEditText().getText());
                                            String carLicensePlates = String.valueOf(txtCarLicensePlates.getEditText().getText());
                                            String carBrand = String.valueOf(txtCarBrand.getText());

                                            newCar.setCarName(carName);
                                            newCar.setCarPrice(carPrice);
                                            newCar.setCarColor(carColor);
                                            newCar.setCarImage(downloadUrl.toString());
                                            newCar.setCarSeat(carSet);
                                            newCar.setCarDescription(carDescription);
                                            newCar.setCarLicensePlates(carLicensePlates);
                                            newCar.setCarBrand(carBrand);
                                            newCar.setCarRating(5);
                                            if (validateCar(newCar)) {
                                                DocumentReference documentReference = fStore.collection("cars").document();
                                                documentReference.set(newCar).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(CreateCarActivity.this, "Create Car Successful", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(), ListCarActivity.class));
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });
                                            }
                                            //do something with downloadurl
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(CreateCarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(CreateCarActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imgUri = data.getData();
            imgCar.setBackground(null);
            Picasso.get().load(imgUri).into(imgCar);

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private boolean validateCar(Car car) {
        if (car.getCarName().isEmpty()) {
            txtCarName.setError("Please enter car name.");
            return false;
        }
        if (car.getCarColor().isEmpty()) {
            txtColor.setError("Please enter car color.");
            return false;
        }
        if (car.getCarBrand().isEmpty() || car.getCarBrand().equalsIgnoreCase(brand[0])) {
//            txtCarBrand.setError("Please enter car brand.");
            return false;
        }
        if (car.getCarImage().isEmpty()) {
            Toast.makeText(this, "Please choose car image", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (car.getCarLicensePlates().isEmpty()) {
            txtCarLicensePlates.setError("Please enter license plates.");
            return false;
        }
        if (car.getCarSeat().isEmpty()) {
            txtSeat.setError("Please enter car seat.");
            return false;
        }
        if (car.getCarPrice() == 0) {
            txtCarPrice.setError("Please enter price.");
            return false;
        }
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        carBrand = spinner.getSelectedItem().toString();
        Log.e("herree",carBrand);
        txtCarBrand.setText(carBrand);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
