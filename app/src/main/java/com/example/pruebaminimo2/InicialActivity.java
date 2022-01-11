package com.example.pruebaminimo2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pruebaminimo2.models.*;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InicialActivity extends AppCompatActivity {

    private EditText user;
    private Button exploreButton;
    private GithubAPI APIgit;
    private static Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        user= findViewById(R.id.userEditText);
        exploreButton = findViewById(R.id.buttonExplorar);

        startRetrofit();
        cargarUser();
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

    public void cargarUser(){

        APIgit = retrofit.create(GithubAPI.class);
        exploreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("click", "ok");
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                Call<User> call = APIgit.infoUser(user.getText().toString());
                Log.d("User",user.getText().toString());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Log.d("Info","tenemos respuesta");
                        if(response.isSuccessful()){
                            Log.d("onResponse", "tenemos user");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            myEdit.putString("name", user.getText().toString());
                            myEdit.commit();
                            intent.putExtra("user", user.getText().toString());
                            startActivity(intent);
                        }
                        else{
                            Log.d("Info:", "user not found");
                            Toast.makeText(getApplicationContext(),"User not found: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Error : " + t.getMessage(), Toast.LENGTH_LONG);
                    }

                });
            }
        });
    }


}