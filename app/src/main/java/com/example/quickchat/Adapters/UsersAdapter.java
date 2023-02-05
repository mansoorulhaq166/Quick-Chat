package com.example.quickchat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickchat.Activities.ChatActivity;
import com.example.quickchat.Models.Users;
import com.example.quickchat.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    List<Users> usersList;
    Context context;
    final static String url = "http://192.168.43.231/ChatAppApis/images/";
    public UsersAdapter(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_iterm_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int adapterPosition = holder.getAdapterPosition();
        Users users = usersList.get(adapterPosition);

        holder.name.setText(users.getName());
        holder.status.setText(users.getStatus());

        Picasso.get().load(url+users.getProfile_pic()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("userName",users.getName());
                intent.putExtra("userImage",url+users.getProfile_pic());
                intent.putExtra("userId",users.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView name, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.user_name);
            status = itemView.findViewById(R.id.user_status);

        }
    }
}
