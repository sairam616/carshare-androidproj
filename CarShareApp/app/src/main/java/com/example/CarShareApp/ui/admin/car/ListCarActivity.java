package com.example.CarShareApp.ui.admin.car;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CarShareApp.DashboardActivity;
import com.example.CarShareApp.R;
import com.example.CarShareApp.apdapter.CarListAminAdapter;
import com.example.CarShareApp.model.Car;
import com.example.CarShareApp.ui.admin.bill.ListBillActivity;
import com.example.CarShareApp.ui.admin.booking.ListBookingActivity;

import com.example.CarShareApp.ui.admin.statistical.RevenueBrandActivity;
import com.example.CarShareApp.ui.admin.statistical.RevenueYearActivity;
import com.example.CarShareApp.ui.home.user.UsersManagementActivity;
import com.example.CarShareApp.ui.login.EditProfileActivity;
import com.example.CarShareApp.ui.login.LoginActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class ListCarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private FirestoreRecyclerOptions<Car> options;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore fireStore;
    private FloatingActionButton fb;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private View header;
    private TextView textViewName, textViewEmail;
    FirebaseAuth fAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_car_admin);
        fireStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        loadListViewCar("");
        searchByName();
        fb = (FloatingActionButton) findViewById(R.id.f_add_car);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_closed);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        textViewName = header.findViewById(R.id.textViewName);
        textViewEmail = header.findViewById(R.id.textViewEmail);
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        getInfoUserByDrawer();
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateCarActivity.class));
                finish();
            }
        });
    }

    private void searchByName() {
        EditText inputSearch = findViewById(R.id.inputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    loadListViewCar(s.toString());

                } else {
                    loadListViewCar("");
                }
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void loadListViewCar(String searchName) {
        // Query
        // below line is use to get data from Firebase
        // firestore using collection in android.
//        Query query = fireStore.collection("cars").limit(7).orderBy("carName").startAt(text.toUpperCase()).endAt(text+"\uf8ff");
//        options = new FirestoreRecyclerOptions.Builder<Car>().setQuery(query, Car.class).build();
//        adapter = new CarListAminAdapter(options);


        Query query = fireStore.collection("cars").orderBy("carName").startAt(searchName).endAt(searchName + "\uf8ff");
        options = new FirestoreRecyclerOptions.Builder<Car>().setQuery(query, Car.class).build();
        adapter = new CarListAminAdapter(options, ListCarActivity.this);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent intentDashboard = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intentDashboard);
                finish();
                break;
            case R.id.nav_home_month:
                Intent intentHome = new Intent(getApplicationContext(), RevenueYearActivity.class);
                startActivity(intentHome);
                finish();

                break;
            case R.id.nav_home_brand:
                Intent intentProfile = new Intent(getApplicationContext(), RevenueBrandActivity.class);
                startActivity(intentProfile);
                finish();

                break;
            case R.id.nav_users_management:
                Intent intentUsersManagement = new Intent(getApplicationContext(), UsersManagementActivity.class);
                startActivity(intentUsersManagement);
                finish();
                break;
            case R.id.nav_profile:
                Intent profile = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(profile);
                finish();
                break;

            case R.id.nav_booking_management:
                Intent booking = new Intent(getApplicationContext(), ListBookingActivity.class);
                startActivity(booking);
                finish();
                break;
            case R.id.nav_car_management:
                Intent car = new Intent(getApplicationContext(), ListCarActivity.class);
                startActivity(car);
                finish();
                break;
            case R.id.nav_bill_management:
                Intent bill = new Intent(getApplicationContext(), ListBillActivity.class);
                startActivity(bill);
                finish();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    // get information user login
    private void getInfoUserByDrawer() {
        if (user != null) {
            String email = user.getEmail();
            String name = user.getDisplayName();

            textViewName.setText(name);
            textViewEmail.setText(email);

        }
    }
}
