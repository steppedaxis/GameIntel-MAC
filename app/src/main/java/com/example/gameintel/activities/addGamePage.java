package com.example.gameintel.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.gameintel.R;
import com.example.gameintel.classes.Game;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class addGamePage extends AppCompatActivity {
    static final int DIALOG_ID=0;

    FirebaseFirestore database;

    EditText name;
    EditText developer;
    EditText publisher;
    EditText description;
    Button release_date_btn;
    EditText series;


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


    }

    public void addGameButton(View view) {

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
                geners,game_series,platforms,"image");


        database.collection("Games").document().set(gameDetailes);


        Intent intent=new Intent(this,GameList.class);
        startActivity(intent);
        finish();

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
