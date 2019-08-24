package com.example.gameintel.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gameintel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreen extends AppCompatActivity {

    //TODO: declare member vars
    private FirebaseAuth mAuth;
    private TextInputEditText mEmailView;
    private TextInputEditText mPassView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mEmailView=(TextInputEditText)findViewById(R.id.emailText);
        mPassView=(TextInputEditText)findViewById(R.id.passText);




        mAuth=FirebaseAuth.getInstance();
    }


    public void loginButton(View view) {
        attemptLogin();
    }

    public void registerButton(View view) {
        Intent intent=new Intent(LoginScreen.this,RegisterPage.class);
        startActivity(intent);
    }




    public void attemptLogin() {
        String email=mEmailView.getText().toString();
        String password=mPassView.getText().toString();
        String cryptpass=new String();
        for (int i=0;i<password.length();i++) {   //encryption for the password
            char ch;
            int num;
            if (Character.isLetter(password.charAt(i))) {
                ch = password.charAt(i);
                if (Character.isUpperCase(ch)) {
                    ch += 2;
                    if (ch > 'Z')
                        ch='A';
                }
                else {
                    ch+=2;
                    if(ch>'z')
                        ch='a';
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

        //check if the email or password fields are empty
        if (email.isEmpty() || password.isEmpty()){
            //if the email or password fields are empty, then exit the attemptLogin() method
            Toast.makeText(this, "user or password field are empty", Toast.LENGTH_SHORT).show();
            return;
        }else{
            //else, show a toast messege
            Toast.makeText(this, "login in progress", Toast.LENGTH_SHORT).show();
        }


        // TODO: Use FirebaseAuth to sign in with email & password
        //the  mAuth.signInWithEmailAndPassword()  logs in a firebase user
        //syntax: mAuth.signInWithEmailAndPassword(user_email,user_password);
        //since signInWithEmailAndPassword method returns a task type, we need to add a listener to it, so we can define what happens if the server successfuly logs in a user
        //syntax for this: .addOnCompleteListener(this(which activity), new OnCompleteListener<AuthResult>(auto completes)
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                final FirebaseUser user=mAuth.getCurrentUser();

                if (task.isSuccessful() && user.isEmailVerified()){
                   Log.d("GameIntel","login successful: "+task.isSuccessful());
                   //declare an intent to go to the MainChatActivity class file if the login attempt was successful
                   Intent intent=new Intent(LoginScreen.this,UserPage.class);
                   finish();
                   startActivity(intent);
                }
                // the else if (!task.isSuccessful()) code block handles whether the server was not able to login the user
                else if (!task.isSuccessful()) {
                    Log.d("GameIntel", "could not sign in: " + task.getException());
                    showErrorDialog("could not sign in: " + task.getException());
                    return;
                }
                else if (!user.isEmailVerified()){
                    showErrorDialog("Your mail is not verified, please verify it to log in");
                    user.sendEmailVerification();
                    Toast.makeText(LoginScreen.this,"A verification email was sent to your email",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void showErrorDialog(String messege) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(messege)
                .setPositiveButton("OK",null)
                .show();
    }



}
