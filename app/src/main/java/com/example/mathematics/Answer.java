package com.example.mathematics;

import java.io.Serializable;

public class Answer implements Serializable {
    private boolean answer;
    private QuestionType questionType;
    private String exercise;
    private int level;
    private String userAnswer;
    private String correctAnswer;

    public String getUserAnswer() {
        return userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }



    public int getLevel() {
        return level;
    }



    public String getExercise() {
        return exercise;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public boolean isAnswer() {
        return answer;
    }

    public Answer(boolean answer,QuestionType questionType,String exercise,int level,String userAnswer,String correctAnswer) {
        this.answer = answer;
        this.questionType=questionType;
        this.exercise=exercise;
        this.level=level;
        this.userAnswer=userAnswer;
        this.correctAnswer=correctAnswer;
    }


}
