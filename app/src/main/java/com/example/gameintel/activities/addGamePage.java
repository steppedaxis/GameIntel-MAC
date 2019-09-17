package com.example.gameintel.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gameintel.R;
import com.example.gameintel.classes.Game;
import com.example.gameintel.classes.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
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


    CheckBox simulation;
    CheckBox fighting;
    CheckBox adventrue;
    CheckBox survival;
    CheckBox racing;
    CheckBox fps;
    CheckBox action;
    CheckBox strategy;
    CheckBox rpg;
    CheckBox ps4;
    CheckBox pc;
    CheckBox ps3;
    CheckBox xbox360;
    CheckBox xbox_one;
    CheckBox nintendo_switch;

    int Year;
    int Month;
    int Day;


    private StorageReference mStorage;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_page);

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


        simulation=findViewById(R.id.simulation_check);
        fighting=findViewById(R.id.fighting_check);
        adventrue=findViewById(R.id.adventure_check);
        survival=findViewById(R.id.survival_check);
        racing=findViewById(R.id.racing_check);
        fps=findViewById(R.id.fps_check);
        action=findViewById(R.id.action_check);
        strategy=findViewById(R.id.strategy_check);
        rpg=findViewById(R.id.rpg_check);
        ps4=findViewById(R.id.ps4_check);
        pc=findViewById(R.id.pc_check);
        ps3=findViewById(R.id.ps3_check);
        xbox360=findViewById(R.id.xbox360_check);
        xbox_one=findViewById(R.id.xboxone_check);
        nintendo_switch=findViewById(R.id.nintendoswitch_check);

        game_image=findViewById(R.id.game_image_add_game);


    }

    public void addGameButton(View view) {

        /*
        //**************INPUT FIELDS STRINGS*******************

        String game_name=name.getText().toString();
        String game_developer=developer.getText().toString();
        String game_publisher=publisher.getText().toString();
        String game_description=description.getText().toString();
        String game_series=series.getText().toString();


        //*****************GENERS CHECKBOXES START************************
        List<String> geners=new ArrayList<String>();

        List<CheckBox> geners_row1=new ArrayList<CheckBox>();
        geners_row1.add(simulation);
        geners_row1.add(fighting);
        geners_row1.add(adventrue);

        List<CheckBox> geners_row2=new ArrayList<CheckBox>();
        geners_row2.add(survival);
        geners_row2.add(racing);
        geners_row2.add(fps);

        List<CheckBox> geners_row3=new ArrayList<CheckBox>();
        geners_row3.add(action);
        geners_row3.add(strategy);
        geners_row3.add(rpg);



        for (CheckBox item:geners_row1){
            if (item.isChecked()){
                geners.add(item.getText().toString());
            }
        }

        for (CheckBox item:geners_row2){
            if (item.isChecked()){
                geners.add(item.getText().toString());
            }
        }

        for (CheckBox item:geners_row3){
            if (item.isChecked()){
                geners.add(item.getText().toString());
            }
        }

        //*****************GENERS CHECKBOXES END************************



        //*****************PLATFORMS CHECKBOXES START************************
        List<String> platforms=new ArrayList<String>();

        List<CheckBox> platforms_row1=new ArrayList<CheckBox>();
        platforms_row1.add(ps4);
        platforms_row1.add(pc);
        platforms_row1.add(ps3);

        List<CheckBox> platforms_row2=new ArrayList<CheckBox>();
        platforms_row2.add(xbox360);
        platforms_row2.add(xbox_one);
        platforms_row2.add(nintendo_switch);

        for (CheckBox item:platforms_row1){
            if (item.isChecked()){
                platforms.add(item.getText().toString());
            }
        }

        for (CheckBox item:platforms_row2){
            if (item.isChecked()){
                platforms.add(item.getText().toString());
            }
        }

        //*****************PLATFORMS CHECKBOXES END************************
        */


        String game_name=name.getText().toString();

        //need to first upload user picture to firebase storage and get url
        mStorage = FirebaseStorage.getInstance().getReference().child("game Images");
        final StorageReference imageFilePath= mStorage.child(game_name+" coverArt");
        imageFilePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //image upload is successfully done
                //we can now get the image full download url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        image=uri.toString();
                        // Create a new user with a first and last name

                        //**************INPUT FIELDS STRINGS*******************//
                        String game_name=name.getText().toString();
                        String game_developer=developer.getText().toString();
                        String game_publisher=publisher.getText().toString();
                        String game_description=description.getText().toString();
                        String game_series=series.getText().toString();


                        //*****************GENERS CHECKBOXES START************************//
                        List<String> geners=new ArrayList<String>();

                        List<CheckBox> geners_row1=new ArrayList<CheckBox>();
                        geners_row1.add(simulation);
                        geners_row1.add(fighting);
                        geners_row1.add(adventrue);

                        List<CheckBox> geners_row2=new ArrayList<CheckBox>();
                        geners_row2.add(survival);
                        geners_row2.add(racing);
                        geners_row2.add(fps);

                        List<CheckBox> geners_row3=new ArrayList<CheckBox>();
                        geners_row3.add(action);
                        geners_row3.add(strategy);
                        geners_row3.add(rpg);



                        for (CheckBox item:geners_row1){
                            if (item.isChecked()){
                                geners.add(item.getText().toString());
                            }
                        }

                        for (CheckBox item:geners_row2){
                            if (item.isChecked()){
                                geners.add(item.getText().toString());
                            }
                        }

                        for (CheckBox item:geners_row3){
                            if (item.isChecked()){
                                geners.add(item.getText().toString());
                            }
                        }

                        //*****************GENERS CHECKBOXES END************************//



                        //*****************PLATFORMS CHECKBOXES START************************//
                        List<String> platforms=new ArrayList<String>();

                        List<CheckBox> platforms_row1=new ArrayList<CheckBox>();
                        platforms_row1.add(ps4);
                        platforms_row1.add(pc);
                        platforms_row1.add(ps3);

                        List<CheckBox> platforms_row2=new ArrayList<CheckBox>();
                        platforms_row2.add(xbox360);
                        platforms_row2.add(xbox_one);
                        platforms_row2.add(nintendo_switch);

                        for (CheckBox item:platforms_row1){
                            if (item.isChecked()){
                                platforms.add(item.getText().toString());
                            }
                        }

                        for (CheckBox item:platforms_row2){
                            if (item.isChecked()){
                                platforms.add(item.getText().toString());
                            }
                        }

                        //*****************PLATFORMS CHECKBOXES END************************//


                        Game gameDetailes=new Game(game_name,geners.get(0),game_developer,game_publisher,game_description,release_date_btn.getText().toString(),
                                geners,game_series,platforms,image);


                        database.collection("Games").document().set(gameDetailes);


                    }


                });

            }
        });

        Intent intent=new Intent(this,GameList.class);
        startActivity(intent);
        finish();

        /*
        Game gameDetailes=new Game(game_name,geners.get(0),game_developer,game_publisher,game_description,release_date_btn.getText().toString(),
                geners,game_series,platforms,"image");


        database.collection("Games").document().set(gameDetailes);


        Intent intent=new Intent(this,GameList.class);
        startActivity(intent);
        finish();
        */
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
}
