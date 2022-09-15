package com.example.mathematics;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private String question;
    private List<String> answers;
    private int level;


    public Question() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public int getLevel() {
        return level;
    }

//    public void setRightAnswer(int rightAnswer) {
//        this.rightAnswer = rightAnswer;
//    }
    public  void setLevel(int level){
        this.level=level;
    }

    @Override
    public String toString() {
        return "Question: " + getQuestion();
    }
}
