package com.example.gameintel.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gameintel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginScreen extends AppCompatActivity {

    //TODO: declare member vars
    private FirebaseAuth mAuth;
    private TextInputEditText mEmailView;
    private TextInputEditText mPassView;
    private FirebaseFirestore database;
    private ProgressBar spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        ActionBar actionBar=getSupportActionBar();
        setTitle("");

        mEmailView=(TextInputEditText)findViewById(R.id.emailText);
        mPassView=(TextInputEditText)findViewById(R.id.passText);
        spinner=findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);


        database=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
    }


    public void loginButton(View view) {
        spinner.setVisibility(View.VISIBLE);
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
                   Intent intent=new Intent(LoginScreen.this,GameList.class);
                   finish();
                   startActivity(intent);
                }
                // the else if (!task.isSuccessful()) code block handles whether the server was not able to login the user
                else if (!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthInvalidUserException e) {
                        spinner.setVisibility(View.GONE);
                        showErrorDialog("Wrong email address or disabled account, please try to recover your account by tapping on \"Forgot My Password\"");
                        mPassView.requestFocus();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        spinner.setVisibility(View.GONE);
                        showErrorDialog("Wrong password");
                        mPassView.requestFocus();
                    } catch(Exception e) {
                        spinner.setVisibility(View.GONE);
                        Log.e("error", e.getMessage());
                    }
                    return;
                }
                else if (!user.isEmailVerified()){
                    spinner.setVisibility(View.GONE);
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


    public void forgotPassButton(View view) {
         showAddItemDialog(LoginScreen.this);
    }

    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Password Reset")
                .setMessage("please enter your email")
                .setView(taskEditText)
                .setPositiveButton("Send Reset Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String givenEmail = String.valueOf(taskEditText.getText());

                        database.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (DocumentSnapshot document:task.getResult()){
                                        String userEmail=document.getString("email");
                                        if (userEmail.equals(givenEmail)){
                                            mAuth.sendPasswordResetEmail(givenEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(LoginScreen.this, "Rest Password link was sent to your email", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else{
                                            showErrorDialog("provided email does not exist in our systems, please double check the email provided or create an account");
                                        }
                                    }
                                }

                            }
                        });

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}
