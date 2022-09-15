package com.example.mathematics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AddNewQuestionActivity extends ActivityWithMenu {
    private String mode;
    private boolean isBeginner;
    private ArrayList<Question> allQuestions = new ArrayList<>();
    private Question selectedQuestion;
    private TextView newQuestionText;
    private TextInputEditText answer1Text;
    private TextInputEditText answer2Text;
    private TextInputEditText answer3Text;
    private TextInputEditText answer4Text;
    private Button drawButton;
    protected TimerHandler timerHandler;
    private  Button saveNewQuestionButton;
    private Exam exam;
    protected Answer answer;
    private View lastClickedAnswer = null;
    private int lastToggleButton = -1;

    private MaterialButtonToggleGroup toggleGroup;

    protected void onResume() {
        super.onResume();
        if (exam != null) {
            timerHandler.findViews();
        }


    }

    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            lastClickedAnswer = view;
            clearAnswerBackground();
            int color = getResources().getColor(R.color.purple_200, getTheme());
            view.setBackgroundColor(color);
        }
    };

    private void clearAnswerBackground() {
        answer1Text.setBackgroundColor(Color.LTGRAY);
        answer2Text.setBackgroundColor(Color.LTGRAY);
        answer3Text.setBackgroundColor(Color.LTGRAY);
        answer4Text.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_question2);
        mode = getIntent().getStringExtra("Mode");
        isBeginner = getIntent().getBooleanExtra("Level", true);
        exam = (Exam) getIntent().getSerializableExtra("exam");
        if (exam != null) {
            timerHandler = new TimerHandler(this, isBeginner ? GameActivity.BEGINNER_USER : GameActivity.ADVANCE_USER, new Runnable() {
                @Override
                public void run() {
                    handleAnswer();
                }
            });


        }
        Button learnButton = findViewById(R.id.learnVerbalButtonID);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference url = database.getReference();
        DatabaseReference questionsUrl = url.child("Questions");
        drawButton = findViewById(R.id.drawQuestionButtonID);

        updatePointsView();


        newQuestionText = findViewById(R.id.newQuestionTextID);
        answer1Text = findViewById(R.id.answer1TextID);
        answer2Text = findViewById(R.id.answer2TextID);
        answer3Text = findViewById(R.id.answer3TextID);
        answer4Text = findViewById(R.id.answer4TextID);

        toggleGroup = findViewById(R.id.toggleButton);
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId == R.id.buttonAdvanced && isChecked) {
                    toggleGroup.uncheck(R.id.buttonBeginner);
                    lastToggleButton = R.id.buttonAdvanced;
                } else if (checkedId == R.id.buttonBeginner && isChecked) {
                    toggleGroup.uncheck(R.id.buttonAdvanced);
                    lastToggleButton = R.id.buttonBeginner;

                }
            }

        });


        if (isNewMode()) {
            learnButton.setVisibility(View.GONE);
            saveNewQuestionButton = findViewById(R.id.saveNewQuestionButtonID);
            saveNewQuestionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newQuestionStr = newQuestionText.getText().toString().trim();
                    if (newQuestionStr.isEmpty()) {
                        Toast.makeText(AddNewQuestionActivity.this, "Question is empty",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    String answer1Str = answer1Text.getText().toString().trim();
                    if (answer1Str.isEmpty()) {
                        Toast.makeText(AddNewQuestionActivity.this, "Answer 1 is empty",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    String answer2Str = answer2Text.getText().toString().trim();
                    if (answer2Str.isEmpty()) {
                        Toast.makeText(AddNewQuestionActivity.this, "Answer 2 is empty",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    String answer3Str = answer3Text.getText().toString().trim();
                    if (answer3Str.isEmpty()) {
                        Toast.makeText(AddNewQuestionActivity.this, "Answer 3 is empty",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    String answer4Str = answer4Text.getText().toString().trim();
                    if (answer4Str.isEmpty()) {
                        Toast.makeText(AddNewQuestionActivity.this, "Answer 4 is empty",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    boolean isBeginner = (lastToggleButton == R.id.buttonBeginner);
                    if (lastToggleButton != R.id.buttonAdvanced && !isBeginner) {
                        Toast.makeText(AddNewQuestionActivity.this, "Level is not selected",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    Question question = new Question();
                    question.setQuestion(newQuestionStr);
                    List<String> answers = new ArrayList<>();
                    answers.add(answer1Str);
                    answers.add(answer2Str);
                    answers.add(answer3Str);
                    answers.add(answer4Str);
                    question.setLevel(isBeginner ? 1 : 2);
                    question.setAnswers(answers);
                    // save to firebase
                    questionsUrl.push().setValue(question);
                    newQuestionText.setText("");
                    answer1Text.setText("");
                    answer2Text.setText("");
                    answer3Text.setText("");
                    answer4Text.setText("");
                    toggleGroup.clearChecked();

                }
            });
            drawButton.setVisibility(View.GONE);

        } else if (isEditMode()) {
            learnButton.setVisibility(View.GONE);
            findViewById(R.id.buttonAdvanced).setEnabled(false);
            findViewById(R.id.buttonBeginner).setEnabled(false);
            selectedQuestion = (Question) getIntent().getSerializableExtra("Question");
            newQuestionText.setText(selectedQuestion.getQuestion());
            answer1Text.setText(selectedQuestion.getAnswers().get(0));
            answer2Text.setText(selectedQuestion.getAnswers().get(1));
            answer3Text.setText(selectedQuestion.getAnswers().get(2));
            answer4Text.setText(selectedQuestion.getAnswers().get(3));
            toggleGroup.check(selectedQuestion.getLevel() == 1 ? R.id.buttonBeginner : R.id.buttonAdvanced);
            saveNewQuestionButton = findViewById(R.id.saveNewQuestionButtonID);
            saveNewQuestionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // take the "Key" from the intent
                    // and use it for saving into the Firebase
                    // the list of the questions in the
                    // previous window is being updated
                    selectedQuestion.setQuestion(newQuestionText.getText().toString());
                    List<String> answers = new ArrayList<>();
                    answers.add(answer1Text.getText().toString());
                    answers.add(answer2Text.getText().toString());
                    answers.add(answer3Text.getText().toString());
                    answers.add(answer4Text.getText().toString());
                    selectedQuestion.setAnswers(answers);
                    String key = getIntent().getStringExtra("Key");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference url = database.getInstance().getReference();
                    DatabaseReference questionsUrl = url.child("Questions");
                    Map<String, Object> data = new HashMap<>();
                    data.put(key, selectedQuestion);
                    questionsUrl.updateChildren(data);
                    finish();
                }
            });
            drawButton.setVisibility(View.GONE);
        } else {
            answer = (Answer) getIntent().getSerializableExtra("answer");
            if (answer != null) {
                newQuestionText.setText(answer.getExercise());
                findViewById(R.id.exerciseLayout).setVisibility(View.GONE);
                findViewById(R.id.resultLayout).setVisibility(View.VISIBLE);
                TextView yourAnswer = findViewById(R.id.yourAnswer);
                String userAnswer = answer.getUserAnswer();
                yourAnswer.setText(userAnswer.isEmpty() ? "Your time was over" : userAnswer);
                TextView correctAnswer = findViewById(R.id.correctAnswer);
                correctAnswer.setText(answer.getCorrectAnswer());
                ((TextInputLayout) findViewById(R.id.newQuestionTextLayout)).setHint("");
                return;
            }

            toggleGroup.setVisibility(View.INVISIBLE);

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    allQuestions.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Question question = ds.getValue(Question.class);
                        if (isBeginner && question.getLevel() == 1) {
                            allQuestions.add(question);

                        } else if (!isBeginner && question.getLevel() == 2) {
                            allQuestions.add(question);

                        }
                    }
                    setRandomQuestion();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    ////Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            };
            questionsUrl.addListenerForSingleValueEvent(postListener);
            ((TextInputLayout) findViewById(R.id.newQuestionTextLayout)).setHint("");

            // disable the texts
            newQuestionText.setKeyListener(null);
            prepareAnswerView(answer1Text);
            prepareAnswerView(answer2Text);
            prepareAnswerView(answer3Text);
            prepareAnswerView(answer4Text);
            clearAnswerBackground();

            saveNewQuestionButton = findViewById(R.id.saveNewQuestionButtonID);
            saveNewQuestionButton.setText("ANSWER");
            saveNewQuestionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleAnswer();
                }
            });
            drawButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AddNewQuestionActivity.this, DrawActivity.class);
                    intent.putExtra("exercise", selectedQuestion.getQuestion());
                    startActivity(intent);
                }
            });
            if (exam != null) {
                learnButton.setVisibility(View.GONE);
            } else {
                learnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AddNewQuestionActivity.this, LearnListActivity.class);

                        startActivity(intent);

                    }
                });
            }

        }
        Button finishAddNewQuestionButton = findViewById(R.id.finishAddNewQuestionButtonID);
        finishAddNewQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void handleAnswer() {
        boolean isRight;
        String selectedAnswer;
        String correctAnswer = selectedQuestion.getAnswers().get(0);
        if (timerHandler != null && timerHandler.isOver()) {
            selectedAnswer = "";
            isRight = false;
            Toast.makeText(this, "Your time is over",
                    Toast.LENGTH_LONG).show();
        } else {
            if (lastClickedAnswer == null) {
                Toast.makeText(getApplicationContext(), "you must choose an answer", Toast.LENGTH_LONG).show();
                return;
            }
            selectedAnswer = ((EditText) lastClickedAnswer).getText().toString();
            isRight = correctAnswer.equals(selectedAnswer);
            if (isRight) {
                lastClickedAnswer.setBackgroundColor(Color.GREEN);
                if (exam == null) {
                    handleAvatar(isBeginner ? GameActivity.BEGINNER_USER : GameActivity.ADVANCE_USER);
                }
            } else {
                lastClickedAnswer.setBackgroundColor(Color.RED);
            }
            if (timerHandler != null) {
                timerHandler.stop();
            }

            saveNewQuestionButton.setEnabled(false);
        }
        if (exam != null) {

            Intent intent = exam.getNextIntent(isBeginner ? 1 : 2, getBaseContext(), isRight, QuestionType.VERBAL_QUESTIONS, selectedQuestion.getQuestion(), selectedAnswer, correctAnswer);
            startActivity(intent);
            finish();

        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setRandomQuestion();
                    saveNewQuestionButton.setEnabled(true);
                }
            }, 900);
        }

    }

    public void onBackPressed() {
        super.onBackPressed();
        if (timerHandler != null) {
            timerHandler.stop();
        }
    }

    private void prepareAnswerView(TextView view) {
        view.setOnClickListener(listener);
        view.setClickable(true);
        view.setFocusable(false);
        ViewParent grandParent = view.getParent().getParent();
        if (grandParent instanceof TextInputLayout) {
            ((TextInputLayout) grandParent).setHint("");
        }
    }

    private void setRandomQuestion() {
        if (allQuestions.isEmpty()) {
            return;
        }
        Random r = new Random();
        int index = Math.abs(r.nextInt()) % allQuestions.size();
        selectedQuestion = allQuestions.get(index);

        newQuestionText.setText(selectedQuestion.getQuestion());
        List<String> answers = new ArrayList<>(selectedQuestion.getAnswers());
        Collections.shuffle(answers);
        answer1Text.setText(answers.get(0));
        answer2Text.setText(answers.get(1));
        answer3Text.setText(answers.get(2));
        answer4Text.setText(answers.get(3));
        clearAnswerBackground();
        lastClickedAnswer = null;
    }
//    protected void updatePointsView() {
//        if (User.getLoggedUser() != null)
//            setTitle("Mathematics (points: " + User.getLoggedUserPoints() + ")");
//        else
//            setTitle("Mathematics");
//    }


//    private boolean isAnswerMode() {
//        return mode.equals("Answer");
//    }

    private boolean isNewMode() {
        return mode.equals("New");
    }

    private boolean isEditMode() {
        return mode.equals("Edit");
    }
}
