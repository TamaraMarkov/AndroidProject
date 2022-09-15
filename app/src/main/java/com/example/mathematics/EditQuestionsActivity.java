package com.example.mathematics;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EditQuestionsActivity extends AppCompatActivity {
    private ArrayList<Question> allQuestions = new ArrayList<>();
    private ArrayAdapter<Question> questionsAdapter;
    private HashMap<Question, String> questionsToKey = new HashMap<>();
    private Button finishButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_questions);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference url = database.getInstance().getReference();
        DatabaseReference questionsUrl = url.child("Questions");
        ListView listAllQuestions = findViewById(R.id.listAllQuestionsID);
        questionsAdapter =
                new ArrayAdapter<Question>(this, android.R.layout.simple_list_item_1, allQuestions);
        listAllQuestions.setAdapter(questionsAdapter);
        listAllQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = position;
                if (index < 0) {
                    Toast.makeText(EditQuestionsActivity.this, "No selected question",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Question questionToEdit = allQuestions.get(index);
                Intent intent = new Intent(getBaseContext(), AddNewQuestionActivity.class);
                intent.putExtra("Mode", "Edit");
                intent.putExtra("Question", questionToEdit);
                intent.putExtra("Key", questionsToKey.get(questionToEdit));
                startActivity(intent);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(EditQuestionsActivity.this);
        listAllQuestions.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int index = position;
                if (index < 0) {
                    Toast.makeText(EditQuestionsActivity.this, "No selected question",
                            Toast.LENGTH_LONG).show();
                    return false;
                }

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you wand to delete this question?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Question questionToDelete = allQuestions.get(index);
                        DatabaseReference mPostReference = questionsUrl
                                .child(questionsToKey.get(questionToDelete));
                        mPostReference.removeValue();
                        questionsToKey.remove(questionToDelete);
                        allQuestions.remove(index);
                        questionsAdapter.clear();
                        questionsAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allQuestions.clear();
                questionsToKey.clear();
                Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                for (DataSnapshot ds :iter) {
                    Question question = ds.getValue(Question.class);
                    questionsToKey.put(question, ds.getKey());
                    allQuestions.add(question);

                }
                questionsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                ////Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        questionsUrl.addValueEventListener(postListener);
          finishButton = findViewById(R.id.finishQuestionsEditButtonID);
          finishButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  finish();
              }
          });
    }
}
