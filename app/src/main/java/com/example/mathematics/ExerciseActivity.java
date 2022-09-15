package com.example.mathematics;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class ExerciseActivity extends ActivityWithMenu implements View.OnClickListener {
    static protected char MATHMETIC_OPERATORS[] = {'+', '-', '*', '/'};
    protected char selectedOperator;
    protected char equalSign = '=';
    protected int level;
    Button answerButton;
    protected Answer answer;
    Button finishButton;
    Button drawButton;
    protected Exam exam;
    protected TimerHandler timerHandler;
    protected Button learnButton;


//    private ProgressBar progressBar;

    @Override
    protected void onResume() {
        super.onResume();
        if (exam != null) {
            timerHandler.findViews();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        level = getIntent().getIntExtra("Level", 0);
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

    }

    protected void init() {
        Random r = new Random();
        selectedOperator = MATHMETIC_OPERATORS[Math.abs(r.nextInt()) % MATHMETIC_OPERATORS.length];

        answerButton = findViewById(getAnswerButtonID());
        answerButton.setOnClickListener(this);
        finishButton = findViewById(getFinishButtonID());
        finishButton.setOnClickListener(this);
        drawButton = findViewById(getDrawButtonID());
        drawButton.setOnClickListener(this);
        learnButton = findViewById(getLearnButtonID());
        if(exam!=null){learnButton.setVisibility(View.GONE);}
        else {
        learnButton.setOnClickListener(this);
        }
        updatePointsView();

//        progressBar = findViewById(R.id.progressBarN);


    }

    @Override
    public void onClick(View v) {
        if (v == answerButton) {
            handleAnswer();
        } else if (v == finishButton) {
            finish();
        } else if (v == drawButton) {
            Intent intent = getDrawIntent();
            startActivity(intent);
        }else if (v == learnButton) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(getLearnUrl()));

        }

    }
    protected abstract String getLearnUrl();

    protected abstract Intent getDrawIntent();

    protected abstract QuestionType getQuestionType();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(timerHandler!=null){
            timerHandler.stop();
        }
    }

    protected void handleAnswer() {
        Result result;
        //progressBar.setVisibility(View.VISIBLE);
        if (timerHandler!=null && timerHandler.isOver()) {
            result=new Result(false,"",getCorrectAnswer());
            Toast.makeText(this, "Your time is over",
                    Toast.LENGTH_LONG).show();

        }
        else{
            result = privateHandleAnswer();
            if(result==null){return;}
            if (timerHandler != null ) {
                timerHandler.stop();

            }

        }

        answerButton.setEnabled(false);
        finishButton.setEnabled(false);

        if (exam != null) {
            Intent intent = exam.getNextIntent(level, getBaseContext(), result.isAnswer(), getQuestionType(), getExercise(), result.getUserAnswer(), result.getCorrectAnswer());
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

    protected abstract String getCorrectAnswer();


    protected abstract Result privateHandleAnswer();

    protected abstract int getAnswerButtonID();

    protected abstract int getFinishButtonID();

    protected abstract int getDrawButtonID();
    protected abstract int getLearnButtonID();

    protected abstract String getExercise();

    @Override
    public void finish() {
        super.finish();
    }

    protected static class Result {
        private boolean answer;
        private String userAnswer;
        private String correctAnswer;

        public boolean isAnswer() {
            return answer;
        }

        public String getUserAnswer() {
            return userAnswer;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public Result(boolean answer, String userAnswer, String correctAnswer) {
            this.answer = answer;
            this.userAnswer = userAnswer;
            this.correctAnswer = correctAnswer;
        }
    }
}
