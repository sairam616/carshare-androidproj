package com.example.CarShareApp.apdapter.admin;

import android.widget.ArrayAdapter;

import com.example.CarShareApp.R;
import com.example.CarShareApp.model.User;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class UsersListApdapter extends ArrayAdapter<User> {
    public UsersListApdapter(@NonNull Context context, ArrayList<User> dataModalArrayList) {
        super(context, 0, dataModalArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // below line is use to inflate the
        // layout for our item of list view.
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
        }

        // our modal class.
        User dataModal = getItem(position);

        // initializing our UI components of list view item.
        TextView textFullName = listitemView.findViewById(R.id.textFullName);
        TextView textRoleName = listitemView.findViewById(R.id.textRoleName);

        textFullName.setText(dataModal.getUserEmail());
        textRoleName.setText(dataModal.getRoleName());

        return listitemView;
    }
}
