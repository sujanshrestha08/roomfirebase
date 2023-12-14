package com.example.roomfinder.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.roomfinder.R;
import com.example.roomfinder.model.Room;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Madhusudan Sapkota on 7/9/2019.
 */
class RoomViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView title, type, price;

    public RoomViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.roomImage);
        title = itemView.findViewById(R.id.roomNameTV);
        type = itemView.findViewById(R.id.roomTypeTV);
        price = itemView.findViewById(R.id.roomPrice);
    }

    public void bind(Room room){
        title.setText(room.getName());
        type.setText(String.format("%1$s \n Apartment", room.getType()));
        price.setText(String.format("Rs. %1$s per month", room.getPrice()));

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(room.getImage());
        Glide.with(imageView)
                .load(storageReference)
                .into(imageView);
    }
}
