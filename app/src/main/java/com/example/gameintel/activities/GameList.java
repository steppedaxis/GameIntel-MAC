package com.example.gameintel.activities;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import okhttp3.internal.Util;

public class GameList extends AppCompatActivity{

    static final String TAG="GameList";
    private FirebaseFirestore database=FirebaseFirestore.getInstance();
    private CollectionReference gameRef=database.collection("Games");
    private CollectionReference userRef=database.collection("Users");
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseUser currenrtUser;


    private GameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        ActionBar actionBar=getSupportActionBar();
        setTitle("");



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
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }







    //SEARCH MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating menu_menu.xml
        getMenuInflater().inflate(R.menu.menu_main,menu);

        MenuItem item=menu.findItem(R.id.action_search);
        final MenuItem profile=menu.findItem(R.id.action_profile);
        MenuItem login_or_register=menu.findItem(R.id.action_login);


        final FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser==null){
            profile.setVisible(false);
            login_or_register.setVisible(true);
        }
        else{
            profile.setVisible(true);
            final String email=currentUser.getEmail();

            userRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot document:task.getResult()){
                            String userEmail = document.getString("email");
                            if (userEmail.equals(email)){
                                Glide.with(getApplicationContext())
                                        .asBitmap()
                                        .load(document.getString("image"))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                profile.setIcon(new BitmapDrawable(getResources(), changeSize(resource)));
                                            }
                                        });

                            }

                        }
                    }
                }
            });




            login_or_register.setVisible(false);
        }

        //Searchview
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
            //Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,UserPage.class);
            startActivity(intent);
        }

        if (item.getItemId()==R.id.action_login){
            Intent intent=new Intent(this,LoginScreen.class);
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


    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        Bitmap _bmp = Bitmap.createScaledBitmap(output, 400,  400, false);
        return _bmp;
        //return output;
    }

    public Bitmap changeSize(Bitmap image){
        Bitmap resized = Bitmap.createScaledBitmap(image, 500, 500, false);
        return resized;
    }














}

