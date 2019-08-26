package com.example.gameintel.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gameintel.R;
import com.example.gameintel.classes.GameAdapter;
import com.example.gameintel.classes.GamePageAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class GamePage extends AppCompatActivity {

    private FirebaseFirestore database;
    private CollectionReference gameRef;
    private GamePageAdapter adapter;
    private DocumentReference documentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        database=FirebaseFirestore.getInstance();
        gameRef=database.collection("Games");

        setUpRecyclerView();


    }

    private void setUpRecyclerView() {
        Query query = gameRef.orderBy("name",Query.Direction.DESCENDING);

    }
}
