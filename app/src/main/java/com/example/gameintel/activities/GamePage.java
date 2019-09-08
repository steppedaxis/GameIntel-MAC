package com.example.gameintel.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gameintel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class GamePage extends AppCompatActivity {

    private FirebaseFirestore database;
    private CollectionReference gameRef;
    private String title;
    String rea="rea";
    TextView image2;
    ImageView image;
    TextView name;
    TextView genre;
    TextView subGenres;
    TextView description;
    TextView developer;
    TextView publisher;
    TextView series;
    TextView releaseDate;
    TextView platforms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page_2);

        database=FirebaseFirestore.getInstance();
        gameRef=database.collection("Games");
        Intent intent=getIntent();
        title=intent.getStringExtra("title");


        image=findViewById(R.id.page_image);
        name=findViewById(R.id.page_title);
        genre=findViewById(R.id.page_genre);
        subGenres=findViewById(R.id.page_subgenres);
        description=findViewById(R.id.page_description);
        developer=findViewById(R.id.page_developer);
        publisher=findViewById(R.id.page_publisher);
        series=findViewById(R.id.page_series);
        releaseDate=findViewById(R.id.page_release_date);
        platforms=findViewById(R.id.page_platforms);
        getGameDetailes();
    }

    private void getGameDetailes(){
        gameRef = database.collection("Games");

        gameRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        List<String> listSubGenres = (List<String>) document.get("subGenres");
                        List<String> listPlatforms = (List<String>) document.get("platforms");

                        if (document.getString("name").equals(title)){
                            Glide.with(image.getContext())
                                    .load(document.getString("image"))
                                    .into(image);

                            name.setText(document.getString("name"));

                            genre.setText(document.getString("genre"));

                            for (String sub_genre :listSubGenres){
                                subGenres.append(sub_genre+",");
                            }
                            String fixedSubGenres=removeLast(subGenres.getText().toString());
                            subGenres.setText(fixedSubGenres);

                            description.setText(document.getString("description"));

                            developer.setText(document.getString("developer"));

                            publisher.setText(document.getString("publisher"));

                            series.setText(document.getString("series"));

                            String releasedate=reverseRelease(document.getString("releaseDate"));
                            releaseDate.setText(releasedate);

                            for (String platform:listPlatforms){
                                platforms.append(platform+",");
                            }
                            String fixedPlatforms=removeLast(platforms.getText().toString());
                            platforms.setText(fixedPlatforms);




                        }
                    }
                } else {
                    Log.d("GameIntel", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public String removeLast(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public String reverseRelease(String release){
        String string[]=release.split("-");
        String releasedatenew=" ";
        String year=string[0];
        String month=string[1];
        String day=string[2];

        if (year.length()==1 || year.length()==2){
            releasedatenew=release;
        }
        else{
             releasedatenew=day+"-"+month+"-"+year;
        }


        return  releasedatenew;
    }




}
