package com.example.mathematics;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ExamSummaryActivity extends ActivityWithMenu{

    protected Exam exam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_summary);
        exam = (Exam)getIntent().getSerializableExtra("exam");
        TextView resultTextView = findViewById(R.id.resultTextView);
        resultTextView.setText(exam.numberOfCorrectAnswers()+"/"+exam.getTotalNumOfQuistions());
        RecyclerView listOfAnswers = findViewById(R.id.listOfAnswers);
        AnswersAdapter answersAdapter = new AnswersAdapter(exam.getAnswers());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listOfAnswers.setLayoutManager(linearLayoutManager);
        listOfAnswers.setAdapter(answersAdapter);
        TextView examPoints = findViewById(R.id.examPointsTextView);

        int level = getIntent().getIntExtra("Level", -1);
        int points=level == GameActivity.BEGINNER_USER ? 5*exam.numberOfCorrectAnswers() : 10*exam.numberOfCorrectAnswers();
        examPoints.setText(String.valueOf(points));

        handleAvatar(level,exam.numberOfCorrectAnswers());


    }
}
