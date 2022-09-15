package com.example.mathematics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class SimpleNumbersExerciseActivity extends ExerciseActivity {

    private int MAX_DEC = 5;
    private int firstNumber;
    private int secondNumber;
    private TextView exercise;
    private EditText answerNumber;
    private  int rightAnswer = 0;


    private int level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_numbers_exercise);
        answerNumber = findViewById(R.id.answerNumberID);
        exercise = findViewById(R.id.textExersiceN);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        level = intent.getIntExtra("Level", 0);
        if (level == GameActivity.BEGINNER_USER) {
            MAX_DEC = 50;
        } else if (level == GameActivity.ADVANCE_USER) {
            MAX_DEC = 100;

        } else {
            finish();
            return;
        }
        if(answer!=null){

            findViewById(R.id.exerciseLayout).setVisibility(View.GONE);
            findViewById(R.id.resultLayout).setVisibility(View.VISIBLE);
            TextView yourAnswer = findViewById(R.id.yourAnswer);
            String userAnswer = answer.getUserAnswer();
            yourAnswer.setText(userAnswer.isEmpty()?"Your time was over":userAnswer);
            TextView correctAnswer = findViewById(R.id.correctAnswer);
            correctAnswer.setText(answer.getCorrectAnswer());
        }


        init();

    }

    protected void init() {
        super.init();
        Random r = new Random();
        int randomFirstNumber = Math.abs(r.nextInt() % MAX_DEC) + 1;
        firstNumber = randomFirstNumber;
        int randomSecondNumber = Math.abs(r.nextInt() % MAX_DEC);
        secondNumber = randomSecondNumber;
        if (selectedOperator == '/') {
            while ((randomSecondNumber == 0) || (randomFirstNumber % randomSecondNumber != 0) || randomSecondNumber>10) {
                randomSecondNumber = Math.abs(r.nextInt() % MAX_DEC);
            }
            secondNumber = randomSecondNumber;
        } else if (selectedOperator == '-') {
            while (randomSecondNumber > randomFirstNumber) {
                randomFirstNumber = Math.abs(r.nextInt() % MAX_DEC);
                firstNumber = randomFirstNumber;
                randomSecondNumber = Math.abs(r.nextInt() % MAX_DEC);
                secondNumber = randomSecondNumber;
            }
        }
        exercise.setText(getExercise() + equalSign);

        answerNumber.setText("");

    }

    @Override
    protected String getLearnUrl() {
        switch (selectedOperator) {
            case '+':
                if (firstNumber > 10 && secondNumber > 10) {
                    if (firstNumber % 10 + secondNumber % 10 >= 10) {
                        return "https://www.youtube.com/watch?v=icyEBL9bzAc";
                    } else {
                        return "https://www.youtube.com/watch?v=bcOK7pGri1c";
                    }

                }
                return "https://www.youtube.com/watch?v=VScM8Z8Jls0";


            case '-':
                if (firstNumber > 10 && secondNumber > 10) {
                    if (firstNumber % 10 - secondNumber % 10 <0) {
                        return "https://www.youtube.com/watch?v=pv8URIRgCdo";
                    } else {
                        return "https://www.youtube.com/watch?v=n0HDh8PP8sQ";
                    }

                }
                return "https://www.youtube.com/watch?v=YLPbduEc4sA&t=25s";


            case '*':
                if ((firstNumber > 10 && secondNumber <= 10)||(firstNumber <= 10 && secondNumber > 10)) {
                    return "https://www.youtube.com/watch?v=k3JRTxFZZIs";
                }
                else if(firstNumber>10&&secondNumber>10){
                    return "https://www.youtube.com/watch?v=PZjIT9CH6bM";
                }
                return "https://www.youtube.com/watch?v=h-CC6jDDahQ&t=24s";
            case '/':
                if(rightAnswer<=10&&secondNumber<=10)
                {
                    return "https://www.youtube.com/watch?v=pQ-0alFKnT4";
                }
                else if(rightAnswer>10&&secondNumber<=10)
                {
                    return "https://www.youtube.com/watch?v=zuaFvGnNDgE";
                }

            default:
                return "";
        }


    }

    @Override
    protected Intent getDrawIntent() {
        Intent intent = new Intent(this, DrawActivity.class);
        intent.putExtra("exercise", getExercise());
        return intent;
    }

    @Override
    protected QuestionType getQuestionType() {
        return QuestionType.WHOLE_NUMBERS;
    }

    @Override
    protected String getCorrectAnswer() {

        switch (selectedOperator) {
            case '+':
                rightAnswer = firstNumber + secondNumber;
                break;
            case '-':
                rightAnswer = firstNumber - secondNumber;
                break;
            case '*':
                rightAnswer = firstNumber * secondNumber;
                break;
            case '/':
                rightAnswer = firstNumber / secondNumber;
                break;
        }
        return String.valueOf(rightAnswer);
    }

    @Override
    protected Result privateHandleAnswer() {
        int answer = 0;
        try {
            answer = new Integer(answerNumber.getText().toString());
        } catch (Exception e) {
            Toast.makeText(SimpleNumbersExerciseActivity.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
            return null;
        }

        int rightAnswer = Integer.valueOf(getCorrectAnswer());
        if (rightAnswer == answer) {
            clap();
            Toast.makeText(this, "Right answer",
                    Toast.LENGTH_LONG).show();
            if(exam==null) {
               handleAvatar(level);
            }
            return new Result(true,Integer.toString(answer),Integer.toString(rightAnswer));

        } else {
            String message = "Bad answer. The right answer is: " + rightAnswer;
            Toast.makeText(this, message,
                    Toast.LENGTH_LONG).show();

        }
        return new Result(false,Integer.toString(answer),Integer.toString(rightAnswer));
    }

    @Override
    protected int getAnswerButtonID() {
        return R.id.answerButtonIDN;
    }

    @Override
    protected int getFinishButtonID() {
        return R.id.finishButtonIDN;
    }

    @Override
    protected int getDrawButtonID() {
        return R.id.drawButtonID;
    }

    @Override
    protected int getLearnButtonID() {
        return R.id.learnButtonID;
    }

    @Override
    protected String getExercise() {
        if (answer != null) {
            return answer.getExercise();
        }
        return String.valueOf(firstNumber) + " " + selectedOperator + " " + String.valueOf(secondNumber);

    }
}