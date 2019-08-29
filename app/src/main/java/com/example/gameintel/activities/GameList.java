package com.example.gameintel.activities;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gameintel.R;
import com.example.gameintel.classes.Game;
import com.example.gameintel.classes.GameAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ContentHandler;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class GameList extends AppCompatActivity{

    static final String TAG="GameList";
    private FirebaseFirestore database=FirebaseFirestore.getInstance();
    private CollectionReference gameRef=database.collection("Games");
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private ImageButton profileButton;
    private ImageButton loginButton;


    private GameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        profileButton=findViewById(R.id.profileButton);
        loginButton=findViewById(R.id.loginButton_list);




        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        Query query = gameRef.orderBy("name",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Game> options = new FirestoreRecyclerOptions.Builder<Game>()
                .setQuery(query,Game.class)
                .build();


        adapter=new GameAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new GameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, String name, int position) {
            }
        });


    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();

        FirebaseUser currentUser=mAuth.getCurrentUser();

        //user not logged in
        if (currentUser==null){
            profileButton.setVisibility(View.GONE);
        }
        else{
            profileButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);

        }

    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }


    public void profileButton(View view) {
            Intent intent=new Intent(this,UserPage.class);
            startActivity(intent);
    }

    public void loginScreen(View view) {
        Intent intent=new Intent(this,LoginScreen.class);
        startActivity(intent);
    }
}

