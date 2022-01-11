package com.dsa.secondminimumexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsa.secondminimumexample.API.API;
import com.dsa.secondminimumexample.API.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FollowersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        init();
        ImageButton return_btn = (ImageButton) findViewById(R.id.b_btn);

        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void init(){

        ImageView avatar = (ImageView) findViewById(R.id.imageView);
        TextView userNom = (TextView) findViewById(R.id.userName);
        TextView repositories = (TextView) findViewById(R.id.repos);
        TextView following = (TextView) findViewById(R.id.following);

        SharedPreferences sharedPrefer = getSharedPreferences("userName", Context.MODE_PRIVATE);
        String userName = sharedPrefer.getString("User", null);

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        API gerritAPI = retrofit.create(API.class);
        Call<User> call = gerritAPI.getInfoUser(userName);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), ErrorActivity.class);
                    startActivity(intent);
                }
                User user = response.body();
                String repos = user.getPublic_repos();
                String follow = user.getFollowing();
                Picasso.get().load(user.getAvatar_url()).into(avatar);
                userNom.setText(userName);
                repositories.setText(repos);
                following.setText(follow);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Intent intent = new Intent(getApplicationContext(), ErrorActivity.class);
                startActivity(intent);
            }
        });

        Gson gson2 = new GsonBuilder().setLenient().create();
        Retrofit retrofit2 = new Retrofit.Builder().baseUrl(API.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson2)).build();
        API gerritAPI2 = retrofit2.create(API.class);
        Call<List<User>> call2 = gerritAPI2.getFollowers(userName);
        call2.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call2, Response<List<User>> response) {
                if(!response.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), ErrorActivity.class);
                    startActivity(intent);
                }
                List<User> userList = response.body();
                ListAdapter listAdapter = new ListAdapter(userList, FollowersActivity.this);
                RecyclerView recyclerView = findViewById(R.id.RecyclerViewList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(FollowersActivity.this));
                recyclerView.setAdapter(listAdapter);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Intent intent = new Intent (getApplicationContext(), ErrorActivity.class);
                startActivity(intent);
            }
        });
    }
}