package com.jvworld.socialx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jvworld.socialx.adapter.NewsAdapter;
import com.jvworld.socialx.apiRepo.ApiServices;
import com.jvworld.socialx.apiRepo.RetroInstance;
import com.jvworld.socialx.responseModel.ArticleDataModel;
import com.jvworld.socialx.responseModel.ArticleResponseModel;
import com.jvworld.socialx.screens.SignIn;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextView logout;

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inItUI();

    }

    private void inItUI() {
        mAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logout_btn);
        recyclerView = findViewById(R.id.news_recycle);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        setRecyclerViewValue();


        logout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, SignIn.class));
        });
    }

    private void setRecyclerViewValue() {

        Call<ArticleResponseModel> listCall = RetroInstance.getUserService().getPost("?q=tesla&from=2022-07-06&sortBy=publishedAt&apiKey=d0abce0743704e20b1442b0c277cef49");

        listCall.enqueue(new Callback<ArticleResponseModel>() {
            @Override
            public void onResponse(Call<ArticleResponseModel> call, Response<ArticleResponseModel> response) {
                ArticleResponseModel responseModel = response.body();
                if (responseModel != null){
                    ArrayList<ArticleDataModel> resultList = response.body().getArticleDataModels();
                    newsAdapter = new NewsAdapter(getApplicationContext(),resultList);
                    recyclerView.setAdapter(newsAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArticleResponseModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser==null){
            startActivity(new Intent(this, SignIn.class));
            finish();
        }else {
            String user_email = firebaseUser.getEmail();
            String data  = user_email.substring(0,user_email.length()-10);
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        }

    }
}