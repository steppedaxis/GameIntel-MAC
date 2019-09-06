package com.example.gameintel.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gameintel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class UserPage extends AppCompatActivity {

    private static final int IMAGE_REQUEST=1;


    ImageButton mUserPic;
    TextView mUserName;
    TextView mName;
    TextView mUserEmail;
    TextView mUserAge;
    TextView mBirthDate;
    TextView verifyedEmail;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore mFireStore;
    StorageReference mStorageReference;
    private Uri imageURI;
    private StorageTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        mUserName=findViewById(R.id.userUserName);
        mName=findViewById(R.id.user_Name);
        mUserEmail=findViewById(R.id.userEmail);
        mUserAge=findViewById(R.id.userAge);
        mBirthDate=findViewById(R.id.userBirthDate);
        verifyedEmail=findViewById(R.id.userVerifyEmail);


        firebaseAuth=FirebaseAuth.getInstance();
        mFireStore=FirebaseFirestore.getInstance();
        mStorageReference= FirebaseStorage.getInstance().getReference("uploads");
    }



    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseUser user=firebaseAuth.getCurrentUser();
        getUserDetailes();
        if (user.isEmailVerified()){
            verifyedEmail.setText("Email verified");
        }else{
            verifyedEmail.setText("Email not verified(Click to verify)");
            verifyedEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(UserPage.this,"Verification email sent",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        mUserEmail.setText("Email: "+user.getEmail());
    }

    private void getUserDetailes(){
        final FirebaseUser user=firebaseAuth.getCurrentUser();

        CollectionReference allUsersRef = mFireStore.collection("Users");
        final String email=user.getEmail();

        allUsersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String userEmail = document.getString("email");
                        if (userEmail.equals(email)) {
                            mUserName.setText(document.getString("userName"));
                           mName.setText("Name: "+document.getString("name"));
                           mUserAge.setText("Age: "+document.getLong("age"));
                           mBirthDate.setText("Birth Date: "+document.getString("birthdateYear"));
                            return;
                        }
                    }
                } else {
                    Log.d("GameIntel", "Error getting documents: ", task.getException());
                }
            }
        });
    }


    public void resetPasswordBtn(View view) {
        final FirebaseUser user=firebaseAuth.getCurrentUser();

        String email=user.getEmail();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    firebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(UserPage.this,LoginScreen.class);
                    startActivity(intent);
                    Toast.makeText(UserPage.this, "Rest Password link was sent to your email", Toast.LENGTH_SHORT).show();
                }
                else if (!task.isSuccessful()){
                    showResetPassDialog("Rest Password link was not sent "+task.getException());
                }
            }
        });
    }



    private void showResetPassDialog(String messege){
        //syntax goes as follows:
        //new AlertDialog.Builder(this)
        //        .setTitle("Oops") -> title of the dialog box
        //        .setMessage(messege) -> what messege will appear on the dialog box
        //        .setPositiveButton(android.R.string.ok,null) -> set an ok button
        //        .setIcon(android.R.drawable.ic_dialog_alert) -> set an icon at the top of the dialog box
        //        .show(); //.show() calls the dialog box to show up
        new AlertDialog.Builder(this)
                .setTitle("Reset Password")
                .setMessage(messege)
                .setPositiveButton("OK",null)
                .show();
    }


    public void logOutButton(View view) {
        firebaseAuth.getInstance().signOut();
        Intent intent=new Intent(this,GameList.class);
        finish();
        startActivity(intent);


    }

    public void addGameButton(View view) {
        Intent intent=new Intent(this,addGamePage.class);
        
        startActivity(intent);

    }
}
