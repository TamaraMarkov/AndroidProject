package com.example.mathematics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends ActivityWithMenu {
    private FirebaseAuth mAuth;
    private Boolean isMale=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        User.getAdminStatus(user);
        Button signUpWindowButton = findViewById(R.id.signUpWindowButtonID);
        signUpWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameText = findViewById(R.id.editTextTextPersonName);
                String name = nameText.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Name is empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                EditText mailText = findViewById(R.id.editTextTextEmailAddress);
                String mail = mailText.getText().toString().trim();
                if (mail.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Mail is empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                EditText passwordText = findViewById(R.id.editTextTextPassword);
                String password = passwordText.getText().toString().trim();
                if (password.length() < 2) {
                    Toast.makeText(SignUpActivity.this, "Password is too short",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                EditText phoneText = findViewById(R.id.editTextPhone);
                String phone = phoneText.getText().toString().trim();
                if (phone.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Phone is empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(isMale==null){
                    Toast.makeText(SignUpActivity.this, "You have to choose an avatar",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                User newUser = new User();
                newUser.setFullName(name);
                newUser.setMail(mail);
                newUser.setPhone(phone);
                newUser.setMale(isMale);
                newUser.setAvatarNumber(1);
                mAuth.createUserWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    User.getAdminStatus(user);
                                    String uid = user.getUid();
                                    newUser.setUid(uid);

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference url = database.getInstance().getReference();
                                    DatabaseReference usersUrl = url.child("Users");
                                    DatabaseReference specificUser = usersUrl.child(uid);
                                    // save the user into the DB
                                    specificUser.setValue(newUser);
                                    User.setLoggedUser(newUser,false);
                                    updatePointsView();

                                    CheckBox keepSignIn = findViewById(R.id.keepSignInID2);
                                    if (keepSignIn.isChecked()) {
                                        SharedPreferences settings = getApplicationContext().getSharedPreferences(MainActivity.PREFS_NAME, 0);
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putString("UserName", mail);
                                        editor.putString("Password", password);
                                        editor.apply();
                                    }
                                    Intent intent = new Intent(getBaseContext(), GameActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        ImageView imageMale= findViewById(R.id.imageMale);
        ImageView imageFemale= findViewById(R.id.imageFemale);
        imageMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageMale.setAlpha(1.0f);
                imageFemale.setAlpha(0.5f);
                isMale=true;
            }
        });
        imageFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageMale.setAlpha(0.5f);
                imageFemale.setAlpha(1.0f);
                isMale=false;
            }
        });
    }
}
