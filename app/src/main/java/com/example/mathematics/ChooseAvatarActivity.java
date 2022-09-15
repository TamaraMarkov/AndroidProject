package com.example.mathematics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChooseAvatarActivity extends AppCompatActivity {

    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private Button confirm;
    private User loggedUser;
    private int chosenAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_avatar);
        loggedUser = User.getLoggedUser();
        if (loggedUser==null){
            finish();
            return;
        }

        image1=findViewById(R.id.image1);
        image2=findViewById(R.id.image2);
        image3=findViewById(R.id.image3);
        image4=findViewById(R.id.image4);

        TextView image2Points=findViewById(R.id.image2Points);
        TextView image3Points=findViewById(R.id.image3Points);
        TextView image4Points=findViewById(R.id.image4Points);

        image2Points.setText("required "+ User.expo(1)+" points");
        image3Points.setText("required "+ User.expo(2)+" points");
        image4Points.setText("required "+ User.expo(3)+" points");
       updatePointsView();


        if(loggedUser.isMale()){
            image1.setImageResource(R.drawable.m1);
            image2.setImageResource(R.drawable.m2);
            image3.setImageResource(R.drawable.m3);
            image4.setImageResource(R.drawable.m4);

        }
        else {
            image1.setImageResource(R.drawable.fm1);
            image2.setImageResource(R.drawable.fm2);
            image3.setImageResource(R.drawable.fm3);
            image4.setImageResource(R.drawable.fm4);
        }
        switch (loggedUser.getAvatarNumber()){
            case 1:
                image1.setAlpha(1.0f);
                break;
            case 2:
                image2.setAlpha(1.0f);
                break;
            case 3:
                image3.setAlpha(1.0f);
                break;
            case 4:
                image4.setAlpha(1.0f);
                break;
        }
        updateLockedImages();
        chosenAvatar=loggedUser.getAvatarNumber();

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image1.setAlpha(1.0f);
                image2.setAlpha(0.5f);
                image3.setAlpha(0.5f);
                image4.setAlpha(0.5f);
                updateLockedImages();
                chosenAvatar=1;
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int points = loggedUser.getPoints();
                if(points<User.expo(1)){
                    Toast.makeText(getApplicationContext(),"you don't have enough points",Toast.LENGTH_LONG).show();
                    return;
                }
                image1.setAlpha(0.5f);
                image2.setAlpha(1.0f);
                image3.setAlpha(0.5f);
                image4.setAlpha(0.5f);
                updateLockedImages();
                chosenAvatar=2;
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int points = loggedUser.getPoints();
                if(points<User.expo(2)){
                    Toast.makeText(getApplicationContext(),"you don't have enough points",Toast.LENGTH_LONG).show();
                    return;
                }
                image1.setAlpha(0.5f);
                image2.setAlpha(0.5f);
                image3.setAlpha(1.0f);
                image4.setAlpha(0.5f);
                updateLockedImages();
                chosenAvatar=3;
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int points = loggedUser.getPoints();
                if(points<User.expo(3)){
                    Toast.makeText(getApplicationContext(),"you don't have enough points",Toast.LENGTH_LONG).show();
                    return;
                }
                image1.setAlpha(0.5f);
                image2.setAlpha(0.5f);
                image3.setAlpha(0.5f);
                image4.setAlpha(1.0f);
                updateLockedImages();
                chosenAvatar=4;
            }
        });
        confirm=findViewById(R.id.confirmationButtonID);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosenAvatar!=loggedUser.getAvatarNumber()) {
                    loggedUser.setAvatarNumber(chosenAvatar);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference url = database.getInstance().getReference();
                    DatabaseReference usersUrl = url.child("Users");
                    DatabaseReference specificUser = usersUrl.child(loggedUser.getUid());
                    // save the user into the DB
                    specificUser.setValue(loggedUser);

                }
                finish();

            }
        });
    }
    private void updateLockedImages(){
        int points = loggedUser.getPoints();
        if(points<User.expo(1)){
            image2.setAlpha(0.2f);
            image3.setAlpha(0.2f);
            image4.setAlpha(0.2f);
        }
        else if(points<User.expo(2)){
            image3.setAlpha(0.2f);
            image4.setAlpha(0.2f);
        }
        else if(points<User.expo(3)){
            image4.setAlpha(0.2f);
        }

    }
    protected void updatePointsView() {
        if (User.getLoggedUser() != null)
            setTitle("Mathematics (points: " + User.getLoggedUserPoints() + ")");
        else
            setTitle("Mathematics");
    }

}