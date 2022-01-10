package com.example.pruebaminimo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pruebaminimo2.models.*;

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
                repositoriesText.setText(String.valueOf(user.getPublic_repos()));
                Picasso.with(getApplicationContext()).load(user.getAvatar_url()).into(ImageView);
                showProgress(false);

                cargarFollowers();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error Code: " + t.getMessage(), Toast.LENGTH_LONG).show();
                showProgress(false);
            }
        });
    }

    public void cargarFollowers(){
        showProgress(true);
        APIgit = retrofit.create(GithubAPI.class);
        Call<List<User>> call = APIgit.listaFollowers(user);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if(response.isSuccessful()){
                    Log.d("onResponse", "lsita ha llegado");
                    List<User> listaUsers = response.body();
                    recyclerView = findViewById(R.id.followers);
                    // https://developer.android.com/guide/topics/ui/layout/recyclerview#java + video

                    RecyclerViewAdapt myAdapter = new RecyclerViewAdapt(getApplicationContext(), listaUsers);
                    recyclerView.setAdapter(myAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

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