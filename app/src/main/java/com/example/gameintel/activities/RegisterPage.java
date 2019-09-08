package com.example.gameintel.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gameintel.R;
import com.example.gameintel.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class RegisterPage extends AppCompatActivity {

    static final int DIALOG_ID=0;
    public static final String userPrefs="userDetails";
    public static final String USER_NAME_KEY = "username";
    public static final String NAME_KEY = "name";
    public static final String BIRTH_DATE_KEY = "birthdate";


    private EditText mUserNameView;
    private EditText mNameView;
    private EditText mPasswordView;
    private EditText mEmailView;
    private TextView mBirthDateView;
    private Button dateButton;
    int Year;
    int Month;
    int Day;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        final Calendar calendar=Calendar.getInstance();
        Year=calendar.get(Calendar.YEAR);
        Month=calendar.get(Calendar.MONTH);
        Day=calendar.get(Calendar.DAY_OF_MONTH);

        showDateDialogOnButtonClick();
        mUserNameView=findViewById(R.id.registerUserName);
        mNameView=findViewById(R.id.registerName);
        mPasswordView=findViewById(R.id.registerPassword);
        mEmailView=findViewById(R.id.registerEmail);
        mBirthDateView=findViewById(R.id.registerBirthDate);

        mAuth=FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();
    }




    //submitButton() is an onclick for the submit button
    public void submitButton(View view) {
        final String givenUserName = mUserNameView.getText().toString();

        CollectionReference usersRef = mFirestore.collection("Users");
        Query query = usersRef.whereEqualTo("userName", givenUserName);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        String dataBaseUser = documentSnapshot.getString("userName");

                        if(dataBaseUser.equals(givenUserName)){
                            mUserNameView.setError("UserName Taken");
                            return;
                        }
                    }
                }

                if(task.getResult().size() == 0 ){
                    attemptRegister();
                }
            }
        });
    }



    //attemptRegister() handels the user registration in the system
    public void attemptRegister(){

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String UserName=mUserNameView.getText().toString();
        String Name=mNameView.getText().toString();
        String BirthYear=mBirthDateView.getText().toString();

        //cancel var will be set as true only if one of the following "if" blocks will set it as true
        //this could happen due to an invalid password,invlaid mail,etc....
         boolean cancel = false;
        // View focusView = null means that there will be no focus on a field for now, it will only focus on a field that has invalid input from the user
         View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError("Password invalid or empty");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email, if the user entered one.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Email empty");
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError("Email invalid");
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(UserName)){
            mUserNameView.setError("User Name empty");
            focusView = mUserNameView;
            cancel = true;
        }

        // Check if the user has not entered his name.
        if (TextUtils.isEmpty(Name)){
            mNameView.setError("Name empty");
            focusView = mNameView;
            cancel = true;
        }

        // Check if the user has not entered his birth year.
        if (TextUtils.isEmpty(BirthYear)){
            mBirthDateView.setError("Birth Date empty");
            focusView = mBirthDateView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            createFirebaseUser();

        }


    }



    private boolean isPasswordValid(String password) {
        //this code block checks both conditions and returns true if they both happen, else returns false if one of them or both does not happen
        if (password.length()>4){
            return true;
        }else{
            return false;
        }
    }


    private boolean isEmailValid(String email) {
        //the method recieves an string(email) as a parameter
        //and it checks using the return email.contains("@") if the string it got contains an @ sign
        //if it does contain an @ sign, it returns true, else it returns false
        // You can add more checking logic here.
        return email.contains("@");
    }









    // TODO: Create a Firebase user
    //the createFirebaseUser method will create a firebase user, by taking his password and email
    private void createFirebaseUser(){

        //getting the users email
        String email=mEmailView.getText().toString();
        //getting the users password
        String password=mPasswordView.getText().toString();
        String cryptpass=new String();
        for (int i=0;i<password.length();i++) {   //encryption for the password
            char ch;
            int num;
            if (Character.isLetter(password.charAt(i))) {
                ch = password.charAt(i);
                if (Character.isUpperCase(ch)) {
                    ch += 2;
                    if (ch > 'Z') {
                        ch = 'A';
                    }
                }else{
                    ch+=2;
                    if(ch>'z') {
                        ch = 'a';
                    }
                }
                cryptpass+=ch;
            }
            else if(Character.isDigit(password.charAt(i))) {
                num=password.charAt(i)-'0';
                if (num < 8)
                    num+=2;
                else if(num==8)
                    num=1;
                else if(num==9)
                    num=2;
                cryptpass+=(char)(num+'0');
            }
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            // the onComplete method handels whether the server successfuly created the user
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("GameIntel","user creation complete "+task.isSuccessful());
                Toast.makeText(RegisterPage.this, "Registration attempt successful", Toast.LENGTH_SHORT).show();
                final FirebaseUser user=mAuth.getCurrentUser();
                user.sendEmailVerification();
                Intent intent = new Intent(RegisterPage.this,LoginScreen.class);
                finish();
                startActivity(intent);
                // the oif (!task.isSuccessful()) code block handels whether the server has not created the user
                if (!task.isSuccessful()){
                    Log.d("GameIntel","user creation failed "+task.isSuccessful());
                    //calling the  showErrorDialog() method to show a dialog box when the regestration is unsuccessful
                    showErrorDialog("Registration attempt failed");
                }
            }
        });

        WriteUserToFirestore();



    }



    private void showErrorDialog(String messege){
        //syntax goes as follows:
        //new AlertDialog.Builder(this)
        //        .setTitle("Oops") -> title of the dialog box
        //        .setMessage(messege) -> what messege will appear on the dialog box
        //        .setPositiveButton(android.R.string.ok,null) -> set an ok button
        //        .setIcon(android.R.drawable.ic_dialog_alert) -> set an icon at the top of the dialog box
        //        .show(); //.show() calls the dialog box to show up
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(messege)
                .setPositiveButton("OK",null)
                .show();
    }


    private void WriteUserToFirestore(){
        // Create a new user with a first and last name
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String userName=mUserNameView.getText().toString();
        String Name=mNameView.getText().toString();
        //int Age=Integer.parseInt(mAgeView.getText().toString());
        String BirthYear=mBirthDateView.getText().toString();
        int Age=AgeCalculator(BirthYear);
        User userdetails=new User(userName,Name,email,Age,BirthYear);

// Add a new document with a generated ID
        mFirestore.collection("Users").document().set(userdetails);

    }









    //=============================DATE PICKER DIALOG DEFINITION====================///
    public void showDateDialogOnButtonClick(){
        dateButton=findViewById(R.id.registerDateButton);

        dateButton.setOnClickListener(new View.OnClickListener() {
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
            mBirthDateView.setText(Day+"/"+Month+"/"+Year);
        }
    };


    //=============================DATE PICKER DIALOG DEFINITION====================///









    public int AgeCalculator(String Birthyear) {
        Calendar today = Calendar.getInstance();
        String[] PartsOfDate=Birthyear.split("/");//split the date to days,months,years
        int currentDay=today.get(Calendar.DAY_OF_MONTH);
        int currentMonth=today.get(Calendar.MONTH);
        int currentYear=today.get(Calendar.YEAR);

        int Day=Integer.parseInt(PartsOfDate[0]);
        int Month=Integer.parseInt(PartsOfDate[1]);
        int Year=Integer.parseInt(PartsOfDate[2]);
        int Age=today.get(Calendar.YEAR) - Year;//get the age of the user if today is is birthday
        if(currentMonth+1<Month){
            Age--;//if birthday month is lower then today's month
        }
        else if(currentDay<Day){
            Age--;//if birthday day is lower then today's day
        }

        return Age;
    }








}
