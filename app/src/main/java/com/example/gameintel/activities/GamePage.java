package com.example.gameintel.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gameintel.R;
import com.example.gameintel.classes.YouTubeApiConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GamePage extends YouTubeBaseActivity {

    private FirebaseFirestore database;
    private CollectionReference gameRef;
    private String title;
    ImageView image;
    TextView name;
    TextView genres;
    TextView description;
    TextView developer;
    TextView publisher;
    TextView series;
    TextView releaseDate;
    TextView platforms;
    TextView age;


    YouTubePlayerView MyouTubePlayerView;
    YouTubePlayer.OnInitializedListener MonInitializedListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page_2);
        setTitle("");


        MyouTubePlayerView=findViewById(R.id.page_gameTrailer);



        database=FirebaseFirestore.getInstance();
        gameRef=database.collection("Games");
        Intent intent=getIntent();
        title=intent.getStringExtra("title");


        image=findViewById(R.id.page_image);
        name=findViewById(R.id.page_title);
        genres=findViewById(R.id.page_genres);
        description=findViewById(R.id.page_description);
        developer=findViewById(R.id.page_developer);
        publisher=findViewById(R.id.page_publisher);
        series=findViewById(R.id.page_series);
        releaseDate=findViewById(R.id.page_release_date);
        platforms=findViewById(R.id.page_platforms);
        age=findViewById(R.id.page_age);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=mAuth.getCurrentUser();

        if (currentUser==null){
            annoyingMessage();
        }




        getGameDetailes();


    }

    private void getGameDetailes(){
        gameRef = database.collection("Games");

        gameRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        List<String> listGenres = (List<String>) document.get("genre");
                        List<String> listPlatforms = (List<String>) document.get("platforms");


                        if (document.getString("name").equals(title)){

                            Glide.with(image.getContext())
                                    .load(document.getString("image"))
                                    .into(image);

                            name.setText(document.getString("name"));

                            for (String genre : listGenres){
                                genres.append(genre+",");
                            }
                            String fixedGenres=removeLast(genres.getText().toString());
                            genres.setText(fixedGenres);

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

                            age.setText(String.valueOf(document.getLong("age")));

                            final String trailerURL=getNeededPartFromURL(document.getString("trailerURL"));


                            //youtube trailer//
                            MonInitializedListener=new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                    youTubePlayer.cueVideo(trailerURL);


                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                                }
                            };

                            MyouTubePlayerView.initialize(YouTubeApiConfig.getApiKey(),MonInitializedListener);







                            return;


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


    private void annoyingMessage(){

        //=========Alert box to annoy users into creating accounts=======
        AlertDialog.Builder builder=new AlertDialog.Builder(GamePage.this);
        builder.setCancelable(true);
        builder.setTitle("Hello!");
        builder.setMessage("Have you signed up? be sure to, as you will be able to contribute to the library yourself and more cool stuff\nif you have already signed up be sure to log in");

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();


    }


    private String getNeededPartFromURL(String URL){
        String neededPart="";

        for (int i=0;i<URL.length();i++){

            if (URL.charAt(i)=='=' && URL.contains("&")){
                neededPart=URL.substring(i+1,URL.indexOf('&'));
                break;
            }
            else if(URL.charAt(i)=='='){
                neededPart=URL.substring(i+1);
                break;
            }
        }

        return  neededPart;
    }






}
