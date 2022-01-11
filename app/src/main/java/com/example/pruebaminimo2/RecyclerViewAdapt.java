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


    private List<Repos> reposList;
    private Context context;

    public RecyclerViewAdapt(Context ct, List<Repos> repos){
        this.context = ct;

        reposList = repos;
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
        holder.repoName.setText(reposList.get(position).getName());
        holder.language.setText(reposList.get(position).getLanguage());

    }

    @Override
    public int getItemCount() {
        return reposList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        public TextView repoName;
        public TextView language;
        public View layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;

            repoName = (TextView) itemView.findViewById(R.id.followerName);
            language = (TextView) itemView.findViewById(R.id.languageText);
        }
    }
}
