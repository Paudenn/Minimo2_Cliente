package com.example.pruebaminimo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pruebaminimo2.models.*;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private GithubAPI APIgit;
    private ProgressBar progressBar;
    private String user;
    private static Retrofit retrofit;
    private RecyclerView recyclerView;
    private TextView repositoriesText;
    private TextView followingText;
    private TextView followersText;
    private android.widget.ImageView ImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.followers);
        ImageView = findViewById(R.id.imageProfile);
        repositoriesText = findViewById(R.id.repos);
        followingText = findViewById(R.id.following);
        followersText = findViewById(R.id.follow);
        @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_APPEND);
        String s1 = sh.getString("name", "");


        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");
        startRetrofit();

        APIgit = retrofit.create(GithubAPI.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        cargarInfo();



    }

    private static void startRetrofit(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //Attaching Interceptor to a client
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.github.com/") //Local host on windows 10.0.2.2 and ip our machine 147.83.7.203
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public void cargarInfo(){
        Log.d("user", user);
        Call<User> call = APIgit.infoUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                final User user = response.body();
                Log.d("following i repos ", String.valueOf(user.getFollowing()) + ", " +String.valueOf(user.getPublic_repos()));
                followingText.setText(String.valueOf(user.getFollowing()));
                followersText.setText(String.valueOf(user.getFollowers()));
                repositoriesText.setText(String.valueOf(user.getPublic_repos()));
                Picasso.with(getApplicationContext()).load(user.getAvatar_url()).into(ImageView);
                showProgress(false);

                cargarRepos();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error Code: " + t.getMessage(), Toast.LENGTH_LONG).show();
                showProgress(false);
            }
        });
    }

    public void cargarRepos(){

        showProgress(true);
        APIgit = retrofit.create(GithubAPI.class);
        Call<List<Repos>> call = APIgit.listaRepos(user);
        call.enqueue(new Callback<List<Repos>>() {
            @Override
            public void onResponse(Call<List<Repos>> call, Response<List<Repos>> response) {

                if(response.isSuccessful()){
                    Log.d("onResponse", "lista ha llegado");
                    List<Repos> listaRepos = response.body();
                    recyclerView = findViewById(R.id.followers);
                    // https://developer.android.com/guide/topics/ui/layout/recyclerview#java + video

                    RecyclerViewAdapt myAdapter = new RecyclerViewAdapt(getApplicationContext(), listaRepos);
                    recyclerView.setAdapter(myAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<List<Repos>> call, Throwable t) {
                Log.d("onFailure", "lista no ha llegado");
            }
        });



    }


    public void showProgress (boolean visible){
        //Sets the visibility/invisibility of progressBar
        if(visible)
            this.progressBar.setVisibility(View.VISIBLE);
        else
            this.progressBar.setVisibility(View.GONE);
    }
}