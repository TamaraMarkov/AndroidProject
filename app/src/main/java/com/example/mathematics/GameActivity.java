package com.example.mathematics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GameActivity extends ActivityWithMenu {
    static public final int BEGINNER_USER = 1;
    static public final int ADVANCE_USER = 2;

    private void updateUI(){
        updatePointsView();
        User user = User.getLoggedUser();
        if (user != null) {
            ImageView avatar = findViewById(R.id.avatar);
            int avatarNumber = user.getAvatarNumber();
            switch (avatarNumber){
                case 1:
                    avatar.setImageResource(user.isMale() ? R.drawable.m1 : R.drawable.fm1);
                    break;
                case 2:
                    avatar.setImageResource(user.isMale() ? R.drawable.m2 : R.drawable.fm2);
                    break;
                case 3:
                    avatar.setImageResource(user.isMale() ? R.drawable.m3 : R.drawable.fm3);
                    break;
                case 4:
                    avatar.setImageResource(user.isMale() ? R.drawable.m4 : R.drawable.fm4);
                    break;
            }


        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(!User.isIsGuest()) {
            User loggedUser = User.getLoggedUser();
            if(loggedUser==null) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
               User.getAdminStatus(user);
                String uid = user.getUid();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference url = database.getInstance().getReference();
                DatabaseReference usersUrl = url.child("Users");

                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                        for (DataSnapshot ds : iter) {
                            if (ds.getKey().equals(uid)) {
                                User user = ds.getValue(User.class);
                                User.setLoggedUser(user, false);
                                updateUI();

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
                updateUI();
            }
        }
        else{


            updateUI();

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);
        Spinner levelsList = findViewById(R.id.levelsListID);
        String[] levelsStrings = new String[] {
                "Beginner", "Advance"
        };
        ArrayAdapter<String> levelsAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,levelsStrings);
        levelsList.setAdapter(levelsAdapter);

        Spinner kindsList = findViewById(R.id.kindsListID);
        String[] kindsStrings = new String[] {
                "Verbal", "Fraction Exercise", "Decimal Fraction Exercise", "Simple Numbers Exercise","Test"
        };
        ArrayAdapter<String> kinsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, kindsStrings);
        kinsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kindsList.setAdapter(kinsAdapter);
        Button startButton = findViewById(R.id.startButtonID);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedLevel = levelsList.getSelectedItemPosition();

                switch (selectedLevel) {
                    case 0:
                        selectedLevel = BEGINNER_USER;
                        break;
                    case 1:
                        selectedLevel = ADVANCE_USER;
                        break;
                }
                Intent intent;
                switch(kindsList.getSelectedItemPosition()) {
                    case 0:
                        intent = new Intent(getBaseContext(), AddNewQuestionActivity.class);
                        intent.putExtra("Level", selectedLevel==BEGINNER_USER);
                        intent.putExtra("Mode", "Answer");
                        startActivity(intent);
                        break;
                    case 1:
                            intent = new Intent(getBaseContext(), FractionExerciseActivity.class);
                            intent.putExtra("Level", selectedLevel);
                            startActivity(intent);
                        break;
                    case 2:
                            intent = new Intent(getBaseContext(), DecimalFractionExerciseActivity.class);
                            intent.putExtra("Level", selectedLevel);
                            startActivity(intent);
                        break;
                    case 3:
                           intent = new Intent(getBaseContext(), SimpleNumbersExerciseActivity.class);
                           intent.putExtra("Level", selectedLevel);
                           startActivity(intent);
                        break;
                    case 4:
                        Exam exam=new Exam(5);
                        //if(selectedLevel==GameActivity.BEGINNER_USER){ exam=new Exam(5);}
                        if(selectedLevel==GameActivity.ADVANCE_USER){ exam=new Exam(10);}
                        intent = exam.getNextIntent(selectedLevel,getBaseContext());
                        startActivity(intent);
                        break;
                }
            }
        });
        Button addVerbalQuestionButton = findViewById(R.id.addVerbalQuestionButtonID);
        Button editQuestionsButton = findViewById(R.id.editQuestionsButtonID);
        if (User.isIsAdmin()) {

            if(User.isIsGuest())
            {
                addVerbalQuestionButton.setVisibility(View.INVISIBLE);
            }
            addVerbalQuestionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), AddNewQuestionActivity.class);
                    intent.putExtra("Mode", "New");
                    startActivity(intent);
                }
            });


            if(User.isIsGuest())
            {
                editQuestionsButton.setVisibility(View.INVISIBLE);
            }
            editQuestionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), EditQuestionsActivity.class);
                    startActivity(intent);
                }
            });

        }
        else{
            addVerbalQuestionButton.setVisibility(View.INVISIBLE);
            editQuestionsButton.setVisibility(View.INVISIBLE);
        }





    }
}
