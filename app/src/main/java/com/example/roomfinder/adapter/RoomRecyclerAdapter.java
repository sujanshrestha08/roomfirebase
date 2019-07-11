package com.example.roomfinder.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roomfinder.R;
import com.example.roomfinder.model.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

/**
 * Created by Madhusudan Sapkota on 7/9/2019.
 */
public class RoomRecyclerAdapter extends FirebaseRecyclerAdapter<Room, RoomViewHolder> {

    public RoomRecyclerAdapter(@NonNull FirebaseRecyclerOptions<Room> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RoomViewHolder holder, int position, @NonNull Room model) {
       holder.bind(model);
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_item, viewGroup, false);
        return new RoomViewHolder(view);
    }
}
