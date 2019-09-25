package com.example.gameintel.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gameintel.R;
import com.example.gameintel.classes.Game;
import com.example.gameintel.classes.GameAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
    private FirebaseUser currenrtUser;


    private GameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        profileButton=findViewById(R.id.profileButton);
        loginButton=findViewById(R.id.loginButton_list);

        currenrtUser=mAuth.getCurrentUser();

        if (currenrtUser==null){
            annoyingMessage();
        }


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
        finish();
        startActivity(intent);
    }





    //SEARCH MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating menu_menu.xml
        getMenuInflater().inflate(R.menu.menu_main,menu);
        //Searchview
        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //called when user presses search button
                //function call with string entered in searchView as parameter
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //called as and when a user types even a single lettter
                searchData(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handels other menu called item clicks here
        if (item.getItemId()==R.id.action_profile){
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,UserPage.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }




    private void searchData(String s){

        Query query = database.collection("Games").orderBy("search_text").startAt(s).endAt(s+"\uf8ff");

        FirestoreRecyclerOptions<Game> options = new FirestoreRecyclerOptions.Builder<Game>()
                .setQuery(query,Game.class)
                .setLifecycleOwner(this)
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


    private void annoyingMessage(){

        //=========Alert box to annoy users into creating accounts=======
        AlertDialog.Builder builder=new AlertDialog.Builder(GameList.this);
        builder.setCancelable(true);
        builder.setTitle("Hello!");
        builder.setMessage("Have you signed up? be sure to, as you will be able to contribute to the library yourself and more cool stuff\nif you have already signed up be sure to log in");

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }














}

