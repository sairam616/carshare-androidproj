package com.example.CarShareApp.apdapter.admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CarShareApp.R;
import com.example.CarShareApp.model.Car;
import com.example.CarShareApp.ui.home.car.CarDetailsActivity;
import com.example.CarShareApp.ui.home.payment.ChooseTimeActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;


public class CarListAdapter extends FirestoreRecyclerAdapter<Car, CarListAdapter.CarViewHolder>{

    private SharedPreferences sharedpreferences;
    private static final String SHARED_PREFERENCE_PRICE = "myPrefs";



    public CarListAdapter(@NonNull FirestoreRecyclerOptions<Car> options) {

        super(options);
    }


    @NonNull
    @NotNull
    @Override
    public CarListAdapter.CarViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_car_homepage,parent,false);

        return new CarListAdapter.CarViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull CarListAdapter.CarViewHolder holder, int position, @NonNull Car model) {

        if(model.getCarStatus() == 1){

            holder.textViewNameCar.setText(model.getCarName());
            holder.textViewPrice.setText("$ " + model.getCarPrice() + " / Daily");
            holder.ratingBar.setRating(model.getCarRating());
            Picasso.get().load(model.getCarImage())
                    .error(R.drawable.user)
                    .into(holder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(holder.itemView.getContext(), CarDetailsActivity.class);
                    intent.putExtra("carName", model.getCarName());
                    intent.putExtra("carPrice", model.getCarPrice());
                    intent.putExtra("carRating", model.getCarRating());
                    intent.putExtra("carImage", model.getCarImage());
                    intent.putExtra("carSeat", model.getCarSeat());
                    intent.putExtra("carDescription", model.getCarDescription());
                    holder.itemView.getContext().startActivity(intent);
                }

            });
            holder.buttonRental.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedpreferences = holder.itemView.getContext().getSharedPreferences(SHARED_PREFERENCE_PRICE, Context.MODE_PRIVATE);
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                    Intent intent = new Intent(holder.itemView.getContext(), ChooseTimeActivity.class);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putFloat("CAR_PRICE", model.getCarPrice());
                    editor.commit();
                    editor.putString("CAR_ID", snapshot.getId());
                    editor.commit();

                    intent.putExtra("carIDDocument", snapshot.getId());
                    Log.e("carIDDocument", snapshot.getId());
                    holder.itemView.getContext().startActivity(intent);
                }
            });

        }else{
            holder.textViewNameCar.setText(model.getCarName());
            holder.textViewPrice.setText("$ " + model.getCarPrice() + " / Daily");
            holder.ratingBar.setRating(model.getCarRating());
            Picasso.get().load(model.getCarImage())
                    .error(R.drawable.user)
                    .into(holder.imageView);
            holder.buttonRental.setEnabled(false);
            holder.buttonRental.setText("Busy");
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    sharedpreferences = holder.itemView.getContext().getSharedPreferences(SHARED_PREFERENCE_PRICE, Context.MODE_PRIVATE);
                    Intent intent = new Intent(holder.itemView.getContext(), CarDetailsActivity.class);
                    intent.putExtra("carName", model.getCarName());
                    intent.putExtra("carPrice", model.getCarPrice());
                    intent.putExtra("carRating", model.getCarRating());
                    intent.putExtra("carImage", model.getCarImage());
                    intent.putExtra("carSeat", model.getCarSeat());
                    intent.putExtra("carDescription", model.getCarDescription());
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    class CarViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewNameCar;
        public TextView textViewPrice;
        public ImageView imageView;
        public RatingBar ratingBar;
        private Button buttonRental;

        public CarViewHolder(@NonNull View itemView)
        {
            super(itemView);
            // initializing our UI components of list view item.
            textViewNameCar = itemView.findViewById(R.id.textViewNameCar);
            textViewPrice = itemView.findViewById(R.id.txtPriceTotal);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imageView = itemView.findViewById(R.id.imageButtonCar);
            buttonRental = itemView.findViewById(R.id.buttonRental);

        }
    }
}
