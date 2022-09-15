package com.example.mathematics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class DecimalFractionExerciseActivity extends ActivityWithMenu implements View.OnClickListener {
    static protected char MORE_LESS[] = {'<', '>'};
    RadioGroup radioGroup;
    private char selectedSign;
    private float firstDecimal;
    private float secondDecimal;
    private Button answerButton;
    private Button learnButton;
    private Button finishButton;
    protected Exam exam;

    protected Answer answer;
    private TextView exerciseDecimal;
    protected TimerHandler timerHandler;

    private int level = 0;

    protected void onResume() {
        super.onResume();
        if (exam != null) {
            timerHandler.findViews();
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decimal_fraction_exercise);
        exerciseDecimal = findViewById(R.id.exerciseDecimalID);
        answerButton = findViewById(R.id.answerButtonID2);
        finishButton = findViewById(R.id.finishButtonID2);
        learnButton = findViewById(R.id.learnDecimalButtonID);
        radioGroup = findViewById(R.id.radioAnswer);
        answerButton.setOnClickListener(this);
        finishButton.setOnClickListener(this);
        learnButton.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        level = intent.getIntExtra("Level", 0);
        exam = (Exam) getIntent().getSerializableExtra("exam");
        answer = (Answer) getIntent().getSerializableExtra("answer");
        if (exam != null) {
            timerHandler = new TimerHandler(this, level, new Runnable() {
                @Override
                public void run() {
                    handleAnswer();
                }
            });

        }
        if (answer != null) {
            findViewById(R.id.exerciseLayout).setVisibility(View.GONE);
            findViewById(R.id.resultLayout).setVisibility(View.VISIBLE);
            TextView yourAnswer = findViewById(R.id.yourAnswer);
            String userAnswer = answer.getUserAnswer();

            yourAnswer.setText(userAnswer.isEmpty() ? "Your time was over" : userAnswer);
            TextView correctAnswer = findViewById(R.id.correctAnswer);
            correctAnswer.setText(answer.getCorrectAnswer());
        }


        init();
    }

    protected void init() {

        Random r = new Random();
        int num = Math.abs(r.nextInt(10));
        int bound = 100;
        if (level == GameActivity.ADVANCE_USER) {
            bound = 1000;

        }
        int num1 = Math.abs(r.nextInt(bound));
        int num2 = Math.abs(r.nextInt(bound));
        firstDecimal = num + (float) num1 / bound;
        secondDecimal = num + (float) num2 / bound;
        radioGroup.clearCheck();


        selectedSign = MORE_LESS[Math.abs(r.nextInt()) % MORE_LESS.length];
        exerciseDecimal.setText(answer != null ? answer.getExercise() : getExercise());
        updatePointsView();

    }

    private boolean isRight() {
        if (firstDecimal == secondDecimal) {
            return false;
        } else if (firstDecimal > secondDecimal) {
            return selectedSign == '>';
        } else {
            return selectedSign == '<';
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (timerHandler != null) {
            timerHandler.stop();
        }
    }

    public void onClick(View v) {
        if (v == answerButton) {
            handleAnswer();
        }

        if (v == finishButton) {
            finish();
        }
        if (v == learnButton) {
            if (exam != null) {
                learnButton.setVisibility(View.GONE);
            } else {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(getLearnUrl()));
            }

        }

    }

    private String getLearnUrl() {
        return "https://www.youtube.com/watch?v=nhLZcvfdgqs";
    }

    //    protected void updatePointsView() {
//        if (User.getLoggedUser() != null)
//            setTitle("Mathematics (points: " + User.getLoggedUserPoints() + ")");
//        else
//            setTitle("Mathematics");
//    }
    public void handleAnswer() {

        int checkedId = radioGroup.getCheckedRadioButtonId();
        Boolean isCorrect;

        boolean expected = isRight();
        if (timerHandler != null && timerHandler.isOver()) {
            isCorrect = false;
            Toast.makeText(this, "Your time is over",
                    Toast.LENGTH_LONG).show();
        } else {
            isCorrect = getCorrectAnswer();


            if (isCorrect == null) {
                Toast.makeText(getApplicationContext(), "you must choose an answer", Toast.LENGTH_LONG).show();
                return;

            } else if (isCorrect == true) {
                Toast.makeText(getApplicationContext(), "you are right", Toast.LENGTH_LONG).show();
                clap();
                if (exam == null) {
                    handleAvatar(level);
                }
            } else {
                Toast.makeText(getApplicationContext(), "you are wrong", Toast.LENGTH_LONG).show();

            }
            if (timerHandler != null) {
                timerHandler.stop();
            }
        }

        if (exam != null) {
            String userAnswer = checkedId == -1 ? "" : Boolean.toString(checkedId == R.id.rightID);
            String correctAnswer = Boolean.toString(expected);


            Intent intent = exam.getNextIntent(level, getBaseContext(), isCorrect, QuestionType.DECIMAL_FRACTIONS, getExercise(), userAnswer, correctAnswer);
            startActivity(intent);
            finish();

        } else {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 3 seconds
                    init();
                    answerButton.setEnabled(true);
                    finishButton.setEnabled(true);
                    //progressBar.setVisibility(View.INVISIBLE);
                }
            }, 1000);
        }
    }


    public Boolean getCorrectAnswer() {
        int checkedId = radioGroup.getCheckedRadioButtonId();

        boolean expected = isRight();


        if (checkedId == -1) {
            //no radio button are checked
            return null;
        } else if (checkedId == R.id.rightID) {
            if (expected) {
                return true;
            }
        } else if (checkedId == R.id.wrongID) {
            if (!expected) {
                return true;
            }

        }
        return false;
    }

    protected String getExercise() {
        return String.valueOf(firstDecimal) + " " + selectedSign + " " + String.valueOf(secondDecimal);
    }


}