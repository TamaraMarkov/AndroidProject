package com.example.mathematics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "Mathematics";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        /*FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(isMain).build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);*/
        mAuth = FirebaseAuth.getInstance();

        Button signInButton = findViewById(R.id.signInButtonID);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userNameText = findViewById(R.id.userNameTextID);
                EditText passwordText = findViewById(R.id.passwordTextID);
                String userName = userNameText.getText().toString();
                if (userName.isEmpty()) {
                    Toast.makeText(MainActivity.this, "user name is empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                String password = passwordText.getText().toString();
                 if (password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "password is empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                signIn(userName, password, true);
            }
        });
        Button signUpButton = findViewById(R.id.signUpButtonID);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
        Button guestButton = findViewById(R.id.guestButtonID);
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                User.setLoggedUser(new User(),true);
                startActivity(intent);
            }
        });
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        String userName = settings.getString("UserName", "");
        String password = settings.getString("Password", "");
        if (! userName.isEmpty() && ! password.isEmpty()) {
            signIn(userName, password, false);
        }
    }
    private void signIn(String userName, String password, boolean updatePreferences) {
        mAuth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            if (updatePreferences) {
                                CheckBox keepSignIn = findViewById(R.id.keepSignInID);
                                if (keepSignIn.isChecked()) {
                                    SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("UserName", userName);
                                    editor.putString("Password", password);
                                    editor.apply();
                                }
                            }
                            FirebaseUser user = mAuth.getCurrentUser();
                            User.getAdminStatus(user);
                            String uid = user.getUid();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference url = database.getInstance().getReference();
                            DatabaseReference usersUrl = url.child("Users");

                            ValueEventListener postListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                                    for (DataSnapshot ds :iter) {
                                        if (ds.getKey().equals(uid)) {
                                            User user = ds.getValue(User.class);
                                            User.setLoggedUser(user,false);

                                            Intent intent = new Intent(getBaseContext(), GameActivity.class);
                                            startActivity(intent);
                                            usersUrl.removeEventListener(this);
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Getting Post failed, log a message
                                    ////Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                                }
                            };
                            usersUrl.addValueEventListener(postListener);
                         }
                        else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
