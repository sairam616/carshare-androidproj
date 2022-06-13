package com.example.CarShareApp.apdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CarShareApp.R;
import com.example.CarShareApp.model.Car;
import com.example.CarShareApp.ui.admin.car.UpdateCarActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class CarListAminAdapter extends FirestoreRecyclerAdapter<Car, CarListAminAdapter.CarAdminViewHolder> {
    // initializing our UI components of list view item.
    public TextView textViewNameCar,txtLicensePlates;
    public TextView textViewPrice;
    public ImageView imageView;
    MenuBuilder menuBuilder;
    Context context;
    FirebaseFirestore fStore;

    public CarListAminAdapter(@NonNull FirestoreRecyclerOptions<Car> options, Context context) {

        super(options);
        this.context = context;

    }


    @SuppressLint("RestrictedApi")
    @Override
    protected void onBindViewHolder(@NonNull @NotNull CarListAminAdapter.CarAdminViewHolder holder, int position, @NonNull @NotNull Car model) {
        holder.textViewNameCar.setText(model.getCarName());
        holder.textViewPrice.setText("$ " + model.getCarPrice() + " / Daily");
        holder.ratingBar.setRating(model.getCarRating());

        // load image from URL in our Image VIew.
        Picasso.get().load(model.getCarImage())
                .error(R.drawable.user)
                .into(holder.imageView);
        menuBuilder = new MenuBuilder(context);
        MenuInflater inflater = new MenuInflater(context);
        inflater.inflate(R.menu.poupup_menu, menuBuilder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuPopupHelper optionMenu = new MenuPopupHelper(context, menuBuilder, v);

                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);

                        switch (item.getItemId()) {
                            case R.id.one:
                                Intent intent = new Intent(context, UpdateCarActivity.class);
                                intent.putExtra("carObject", model);
                                intent.putExtra("carId", snapshot.getId());
                                context.startActivity(intent);
                                break;
                            case R.id.three:
                                fStore = FirebaseFirestore.getInstance();

                                fStore.collection("car").document(snapshot.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
//                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                                break;
                            default:
                                return false;
                        }
                        return false;
                    }

                    @Override
                    public void onMenuModeChange(@NonNull MenuBuilder menu) {

                    }
                });
                optionMenu.show();

            }
        });
    }

    @NonNull
    @NotNull
    @Override
    public CarAdminViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_car, parent, false);

        return new CarListAminAdapter.CarAdminViewHolder(view);
    }

    public class CarAdminViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNameCar;
        public TextView textViewPrice;
        public ImageView imageView;
        public RatingBar ratingBar;

        public CarAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our UI components of list view item.
            textViewNameCar = itemView.findViewById(R.id.textViewNameCar);
            textViewPrice = itemView.findViewById(R.id.txtPriceTotal);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imageView = itemView.findViewById(R.id.imageButtonCar);
        }
    }
}
