package com.example.progull;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

class FindFriendViewHolder extends RecyclerView.ViewHolder {

    CircleImageView profileImage;
    TextView username,clas;



    public FindFriendViewHolder(@NonNull View itemView) {
        super(itemView);

        profileImage = itemView.findViewById(R.id.profileImage);
        username = itemView.findViewById(R.id.username);
        clas = itemView.findViewById(R.id.clas);

    }
}
