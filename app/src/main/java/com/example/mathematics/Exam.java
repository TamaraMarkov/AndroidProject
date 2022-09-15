package com.example.mathematics;

import static com.example.mathematics.GameActivity.BEGINNER_USER;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Exam implements Serializable {

    private int totalNumOfQuistions;
    private int indexCurrentQuestion;

    public List<Answer> getAnswers() {
        return answers;
    }

    private List<Answer> answers;


    public Exam(int totalNumOfQuistions) {
        this.totalNumOfQuistions = totalNumOfQuistions;
        this.indexCurrentQuestion=0;
        this.answers=new ArrayList<>();
    }

    public int getTotalNumOfQuistions() {
        return totalNumOfQuistions;
    }

    public Intent getNextIntent(int selectedLevel, Context context){
        Intent intent;
        if(indexCurrentQuestion==totalNumOfQuistions){

            intent = new Intent(context, ExamSummaryActivity.class);
            intent.putExtra("Level", selectedLevel);
        }else {
            Random r = new Random();

           int randomQuestion = r.nextInt(4);
            switch (randomQuestion) {
                case 0:
                    intent = new Intent(context, AddNewQuestionActivity.class);
                    intent.putExtra("Level", selectedLevel == BEGINNER_USER);
                    intent.putExtra("Mode", "Answer");
                    break;
                case 1:
                    intent = new Intent(context, FractionExerciseActivity.class);
                    intent.putExtra("Level", selectedLevel);
                    break;
                case 2:
                    intent = new Intent(context, DecimalFractionExerciseActivity.class);
                    intent.putExtra("Level", selectedLevel);
                    break;
                case 3:
                    intent = new Intent(context, SimpleNumbersExerciseActivity.class);
                    intent.putExtra("Level", selectedLevel);
                    break;
                default:
                    throw new RuntimeException();
            }
        }
        intent.putExtra("exam",this);
        return intent;

    }

    public Intent getNextIntent(int selectedLevel, Context context,boolean result,QuestionType questionType,String exercise,String userAnswer,String correctAnswer){
        answers.add(new Answer(result,questionType,exercise,selectedLevel,userAnswer,correctAnswer));
        indexCurrentQuestion++;
        return getNextIntent(selectedLevel,context);
    }
    public int numberOfCorrectAnswers(){
        int count=0;
        for (Answer answer : answers) {
            if(answer.isAnswer()){
                count++;
            }
        }
        return count;
    }
}
