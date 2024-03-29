package com.example.gameintel.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gameintel.R;
import com.example.gameintel.classes.Game;
import com.example.gameintel.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class addGamePage extends AppCompatActivity {
    static final int DIALOG_ID=0;
    static int REQUESTCODE=1;
    static int PReqCode=1;

    Uri pickedImageUri;
    private String image;

    FirebaseFirestore database;

    EditText name;
    EditText developer;
    EditText publisher;
    EditText description;
    Button release_date_btn;
    EditText series;
    ImageView game_image;
    EditText age;
    EditText trailerURL;

    int Year;
    int Month;
    int Day;


    private StorageReference mStorage;
   Button geners_button;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_page);
        ActionBar actionBar=getSupportActionBar();
        setTitle("");

        database=FirebaseFirestore.getInstance();

        final Calendar calendar=Calendar.getInstance();
        Year=calendar.get(Calendar.YEAR);
        Month=calendar.get(Calendar.MONTH);
        Day=calendar.get(Calendar.DAY_OF_MONTH);

        showDateDialogOnButtonClick();


        name=findViewById(R.id.add_game_name);
        developer=findViewById(R.id.add_game_developer);
        publisher=findViewById(R.id.add_game_publisher);
        description=findViewById(R.id.add_game_description);
        release_date_btn=findViewById(R.id.add_game_release_btn);
        series=findViewById(R.id.add_game_series);
        geners_button=findViewById(R.id.geners_button);
        age=findViewById(R.id.add_game_age);
        trailerURL=findViewById(R.id.add_game_trailerURL);





        game_image=findViewById(R.id.game_image_add_game);





    }

    public void addGameButton(View view) {

        String game_name=name.getText().toString();

        //need to first upload user picture to firebase storage and get url
        //refrences to storage and storage folders
        mStorage = FirebaseStorage.getInstance().getReference().child("game Images");
        final StorageReference imageFilePath= mStorage.child(game_name+" coverArt");
        imageFilePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //image upload is successfully done
                // can now get the image full download url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        image=uri.toString();

                        //**************INPUT FIELDS STRINGS*******************//
                        final String game_name=name.getText().toString();
                        final String game_search=name.getText().toString().toLowerCase();
                        final String game_developer=developer.getText().toString();
                        final String game_publisher=publisher.getText().toString();
                        final String game_description=description.getText().toString();
                        final String game_series=series.getText().toString();
                        final int game_age=Integer.parseInt(age.getText().toString());
                        final String game_trailer_url=trailerURL.getText().toString();
                        final List<String> geners = loadList("genre");
                        final List<String> platforms=loadList("platformslist");




                        CollectionReference gameRef = database.collection("Games");
                        Query query = gameRef.whereEqualTo("search_text", game_name.toLowerCase());
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                                        String dataBaseGameName = documentSnapshot.getString("search_text");

                                        if(dataBaseGameName.equals(game_name.toLowerCase())){
                                            //name.setError("game already in database");
                                            showErrorDialog("game already exists in database");
                                            return;

                                        }
                                    }
                                }

                                if(task.getResult().size() == 0 ){
                                    Game gameinfo=new Game(game_name,game_search,geners,game_developer,game_publisher,game_description,release_date_btn.getText().toString(),game_series,platforms,image,game_age,game_trailer_url);
                                    database.collection("Games").document().set(gameinfo);
                                    Intent intent=new Intent(addGamePage.this,GameList.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                        });




                        //Game gameinfo=new Game(game_name,game_search,geners,game_developer,game_publisher,game_description,release_date_btn.getText().toString(),game_series,platforms,image,game_age,game_trailer_url);



                        //database.collection("Games").document().set(gameinfo);

                        


                    }


                });

            }
        });



    }







    public void addGameImage(View view) {
        game_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT>=22){
                    checkAndRequestForPermissions();
                }
                else{
                    openGallery();
                }

            }
        });
    }


    //the checkAndRequestForPermissions() function handels asking the user for permissions before using his phone's gallery
    private void checkAndRequestForPermissions() {

        if (ContextCompat.checkSelfPermission(addGamePage.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(addGamePage.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(addGamePage.this, "Accept needed permissions,please...", Toast.LENGTH_LONG).show();
            }
            else{
                ActivityCompat.requestPermissions(addGamePage.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        }
        else{
            openGallery();
        }

    }


    //this function opens the users gallery when he presses the user profile imageview in registration page
    private void openGallery() {

        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK && requestCode ==REQUESTCODE && data != null) {

            //user successfuly picked an image
            //we need to save its refernce to a Uri var
            pickedImageUri=data.getData();
            game_image.setImageURI(pickedImageUri);


        }



    }



    public void select_Genres(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose genres");

        // Add a checkbox list
        final String[] genres = {"Simulation", "Fighting", "Adventrue", "Survival", "Racing","Fps","Action","Strategy","Rpg"};
        final List<String> selected_geners=new ArrayList<String>();
        selected_geners.clear();
        final boolean[] checkedItems=new boolean[9];
        Arrays.fill(checkedItems,Boolean.FALSE);
        builder.setMultiChoiceItems(genres, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // The user checked or unchecked a box
                //Toast.makeText(addGamePage.this, "Position: " + which + " Value: " + genres[which] + " State: " + (isChecked ? "checked" : "unchecked"), Toast.LENGTH_LONG).show();
                if (isChecked){
                    selected_geners.add(genres[which]);
                }
                else{
                    selected_geners.remove(genres[which]);
                }

            }
        });


        // Add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                saveList(selected_geners,"genre");


            }
        });

        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }




    public void platforms_select(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose platforms");

        // Add a checkbox list
        final String[] platforms = {"Xbox 360", "Xbox One", "PS4", "PS3", "PC","Nintendo Switch"};
        final List<String> selected_platforms=new ArrayList<String>();
        selected_platforms.clear();
        final boolean[] checkedItems=new boolean[6];
        Arrays.fill(checkedItems,Boolean.FALSE);
        builder.setMultiChoiceItems(platforms, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // The user checked or unchecked a box
                //Toast.makeText(addGamePage.this, "Position: " + which + " Value: " + platforms[which] + " State: " + (isChecked ? "checked" : "unchecked"), Toast.LENGTH_LONG).show();
                if (isChecked){
                    selected_platforms.add(platforms[which]);
                }
                else{
                    selected_platforms.remove(platforms[which]);
                }

            }
        });


        // Add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                saveList(selected_platforms,"platformslist");
            }
        });

        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }






    //=============================DATE PICKER DIALOG DEFINITION====================///
    public void showDateDialogOnButtonClick(){
        release_date_btn=findViewById(R.id.add_game_release_btn);

        release_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if (id==DIALOG_ID){
            return new DatePickerDialog(this,datepickerlistner,Year,Month,Day);
        }else{
            return null;
        }
    }

    private DatePickerDialog.OnDateSetListener datepickerlistner=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Year=year;
            Month=month+1;
            Day=dayOfMonth;
            release_date_btn.setText(Day+"-"+Month+"-"+Year);
        }
    };




    //=============================DATE PICKER DIALOG DEFINITION====================///

    private void saveList(List<String> yourList, String yourKey) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());

        SharedPreferences.Editor prefsEditor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(yourList);

        prefsEditor.putString(yourKey, json);

        prefsEditor.apply();
    }

    private List<String> loadList(String yourKey) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());

        Gson gson = new Gson();
        String json = prefs.getString(yourKey, null);

        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        return gson.fromJson(json, type);
    }


    private void showErrorDialog(String messege) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(messege)
                .setPositiveButton("OK",null)
                .show();
    }




}
