package com.example.pruebaminimo2;

import com.example.pruebaminimo2.models.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapt extends RecyclerView.Adapter<RecyclerViewAdapt.MyViewHolder> {

    private List<User> listaUsers;
    private Context context;

    public RecyclerViewAdapt(Context ct, List<User> users){
        this.context = ct;
        listaUsers = users;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.follower, parent, false);
        MyViewHolder v = new MyViewHolder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.usuario.setText(listaUsers.get(position).getLogin());
        Picasso.with(context).load(listaUsers.get(position).getAvatar_url()).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return listaUsers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView avatar;
        public TextView usuario;
        public View layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            avatar = (ImageView) itemView.findViewById(R.id.imageFollower);
            usuario = (TextView) itemView.findViewById(R.id.followerName);
        }
    }
}
