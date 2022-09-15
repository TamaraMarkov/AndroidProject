package com.example.mathematics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class FractionExerciseActivity extends ExerciseActivity {
    private TextView firstOperandIntPart;
    private TextView firstOperandNominatorPart;
    private TextView firstOperandDenominatorPart;
    private TextView secondOperandIntPart;
    private TextView secondOperandNominatorPart;
    private TextView secondOperandDenominatorPart;
    private EditText answerIntPart;
    private EditText answerNominatorPart;
    private EditText answerDenominatorPart;
    private TextView operator;
    int MAX = 5;
    private int level = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fraction_exercise);
        answerIntPart = findViewById(R.id.fractionAnswerIntID);
        answerNominatorPart = findViewById(R.id.fractionAnswerNominatorID);
        answerDenominatorPart = findViewById(R.id.fractionAnswerDenominatorID);
        firstOperandIntPart = findViewById(R.id.fractionFirstIntID);
        firstOperandNominatorPart = findViewById(R.id.fractionFirstNominatorID);
        firstOperandDenominatorPart = findViewById(R.id.fractionFirstDenominatorID);
        secondOperandIntPart = findViewById(R.id.fractionSecondIntID);
        secondOperandNominatorPart = findViewById(R.id.fractionSecondNominatorID);
        secondOperandDenominatorPart = findViewById(R.id.fractionSecondDenominatorID);
        operator = findViewById(R.id.fractionOperatorID);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        level = intent.getIntExtra("Level", 0);
        if (level == GameActivity.ADVANCE_USER) {
            MAX = 10;
        } else {
            MATHMETIC_OPERATORS = new char[]{'+', '-'};
        }
        if (answer != null) {
            findViewById(R.id.exerciseFractionsLayout).setVisibility(View.GONE);
            findViewById(R.id.resultFractionLayout).setVisibility(View.VISIBLE);
            TextView yourAnswerInt = findViewById(R.id.fractionYourAnswerIntID);
            TextView yourAnswerNominator = findViewById(R.id.fractionYourAnswerNominatorID);
            TextView yourAnswerDenominator = findViewById(R.id.fractionYourAnswerDenominatorID);
            String userAnswer = answer.getUserAnswer();
            if (!userAnswer.isEmpty()) {
                FractionNumber userFractionAnswer = FractionNumber.parse(userAnswer);
                yourAnswerInt.setText(Integer.toString(userFractionAnswer.getInteger()));
                yourAnswerNominator.setText(Integer.toString(userFractionAnswer.getNumerator()));
                yourAnswerDenominator.setText(Integer.toString(userFractionAnswer.getDenominator()));
            }else{
                findViewById(R.id.answerLayout).setVisibility(View.GONE);
                findViewById(R.id.timeIsOverAnswer).setVisibility(View.VISIBLE);


            }
            TextView correctAnswerInt = findViewById(R.id.fractionCorrectAnswerIntID);
            TextView correctAnswerNominator = findViewById(R.id.fractionCorrectAnswerNominatorID);
            TextView correctAnswerDenominator = findViewById(R.id.fractionCorrectAnswerDenominatorID);
            FractionNumber correctFractionAnswer = FractionNumber.parse(answer.getCorrectAnswer());
            correctAnswerInt.setText(Integer.toString(correctFractionAnswer.getInteger()));
            correctAnswerNominator.setText(Integer.toString(correctFractionAnswer.getNumerator()));
            correctAnswerDenominator.setText(Integer.toString(correctFractionAnswer.getDenominator()));
        }

        init();
    }

    private int getNominator(Random r, int denominator) {

        int Nominator = 0;
        while (Nominator == 0) {
            Nominator = Math.abs(r.nextInt()) % denominator;
        }
        return Nominator;

    }

    protected void init() {
        super.init();
        Random r = new Random();
        answerIntPart.setText("");
        answerNominatorPart.setText("");
        answerDenominatorPart.setText("");
        FractionNumber firstFraction = null;
        FractionNumber secondFraction = null;

        if (answer != null) {
            String exercise = answer.getExercise();
            String[] a = exercise.split("#");
            firstFraction = FractionNumber.parse(a[0]);
            secondFraction = FractionNumber.parse(a[2]);
            selectedOperator = (a[1]).charAt(0);

        }
        if (operator != null) {
            operator.setText(String.valueOf(selectedOperator));
        }

        int firstOperandInt = Math.abs(r.nextInt()) % MAX;
        if (firstOperandInt != 0)
            firstOperandIntPart.setText(String.valueOf(firstOperandInt));
        else
            firstOperandIntPart.setText("");
        int denominator = Math.abs(r.nextInt()) % MAX;
        // prevent 0 in the denominator
        while (denominator == 0 || denominator == 1)
            denominator = Math.abs(r.nextInt()) % MAX;

        firstOperandDenominatorPart.setText(String.valueOf(denominator));
        // make sure that the denominator is less than the denominator
        //int nominator=Math.abs(r.nextInt()) % denominator;
        int firstNominator = getNominator(r, denominator);

        firstOperandNominatorPart.setText(String.valueOf(firstNominator));

        int secondOperandInt = Math.abs(r.nextInt()) % MAX;
        if (secondOperandInt != 0)
            secondOperandIntPart.setText(String.valueOf(secondOperandInt));
        else
            secondOperandIntPart.setText("");
        if (level == GameActivity.ADVANCE_USER) {
            denominator = Math.abs(r.nextInt()) % MAX;
            // prevent 0 in the denominator
            while (denominator == 0 || denominator == 1)
                denominator = Math.abs(r.nextInt()) % MAX;
        }

        secondOperandDenominatorPart.setText(String.valueOf(denominator));
        // make sure that the denominator is less than the denominator
        int secondNominator = getNominator(r, denominator);

        secondOperandNominatorPart.setText(String.valueOf(secondNominator));

        if (selectedOperator == '-') {
            while (secondOperandInt >= firstOperandInt) {
                firstOperandInt = Math.abs(r.nextInt()) % MAX;
                firstOperandIntPart.setText(String.valueOf(firstOperandInt));
                secondOperandInt = Math.abs(r.nextInt()) % MAX;
                secondOperandIntPart.setText(String.valueOf(secondOperandInt));
            }
        }
        if (firstFraction != null) {
            firstOperandIntPart.setText(String.valueOf(firstFraction.getInteger()));
            firstOperandNominatorPart.setText(String.valueOf(firstFraction.getNumerator()));
            firstOperandDenominatorPart.setText(String.valueOf(firstFraction.getDenominator()));
        }
        if (secondFraction != null) {
            secondOperandIntPart.setText(String.valueOf(secondFraction.getInteger()));
            secondOperandNominatorPart.setText(String.valueOf(secondFraction.getNumerator()));
            secondOperandDenominatorPart.setText(String.valueOf(secondFraction.getDenominator()));
        }
    }

    @Override
    protected String getLearnUrl() {
        switch (selectedOperator) {
            case '+':
                if (level == GameActivity.ADVANCE_USER)
                {
                    return "https://www.youtube.com/watch?v=jSd9PMlozQk";
                }
                else
                {
                    return "https://www.youtube.com/watch?v=VHNjaNkLmWs";
                }

            case '-':
                if (level == GameActivity.ADVANCE_USER)
                {
                    return "https://www.youtube.com/watch?v=ORyhjp3-HYI";
                }
                else
                {
                    return "https://www.youtube.com/watch?v=pDrAQEYWxm0";
                }
            case '*':
                return "https://www.youtube.com/watch?v=DSIO0no1c4A";

            case '/':
                return "https://www.youtube.com/watch?v=SVp4z0txygI";



            default:
                return "";
        }


    }

    @Override
    protected Result privateHandleAnswer() {
        int answerInt = 0;
        int answerNominator = 0;
        int answerDenominator = 0;
        try {
            answerInt = getValue(answerIntPart);
            answerNominator = getValue(answerNominatorPart);
            answerDenominator = getValue(answerDenominatorPart);
        } catch (Exception e) {
            Toast.makeText(FractionExerciseActivity.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
            return null;
        }
        if (answerDenominator == 0) {
            Toast.makeText(FractionExerciseActivity.this, "Denominator cannot be zero",
                    Toast.LENGTH_LONG).show();
            return null;
        }
        FractionNumber answerAttempt = new FractionNumber(answerInt, answerNominator, answerDenominator);
        FractionNumber correctAnswer = FractionNumber.parse(getCorrectAnswer());
        Log.d("fraction", "user answer: " + answerAttempt.toString() + ", correct answer: " + correctAnswer.toString());

        if (correctAnswer.compareTo(answerAttempt) == 0) {

            Toast.makeText(FractionExerciseActivity.this, "Right answer",
                    Toast.LENGTH_LONG).show();
            clap();
            if(exam==null){
            handleAvatar(level);
            }
            return new Result(true, answerAttempt.toString(), correctAnswer.toString());
        } else {
            String message = "Bad answer. The right answer is: " + correctAnswer.toString();
            Toast.makeText(FractionExerciseActivity.this, message,
                    Toast.LENGTH_LONG).show();
        }
        return new Result(false, answerAttempt.toString(), correctAnswer.toString());
    }

    private int getValue(TextView text) {
        String valueStr = text.getText().toString();
        if (valueStr.isEmpty())
            return 0;
        return Integer.parseInt(valueStr);
    }

    private int GDC(int a, int b) {
        int gcd = 1;
        for (int i = 1; i <= a && i <= b; ++i) {
            // Checks if i is factor of both integers
            if (a % i == 0 && b % i == 0)
                gcd = i;
        }
        return gcd;
    }

    @Override
    protected Intent getDrawIntent() {
        Intent intent = new Intent(this, DrawFractionActivity.class);
        intent.putExtra("firstOperandIntPart", firstOperandIntPart.getText().toString());
        intent.putExtra("secondOperandIntPart", secondOperandIntPart.getText().toString());
        intent.putExtra("firstOperandNominatorPart", firstOperandNominatorPart.getText().toString());
        intent.putExtra("secondOperandNominatorPart", secondOperandNominatorPart.getText().toString());
        intent.putExtra("firstOperandDenominatorPart", firstOperandDenominatorPart.getText().toString());
        intent.putExtra("secondOperandDenominatorPart", secondOperandDenominatorPart.getText().toString());
        intent.putExtra("operator", operator.getText().toString());

        return intent;
    }

    @Override
    protected QuestionType getQuestionType() {
        return QuestionType.FRACTIONS;
    }

    @Override
    protected String getCorrectAnswer() {
        int firstOperandInt = getValue(firstOperandIntPart);
        int firstOperandNominator = getValue(firstOperandNominatorPart);
        int firstOperandDenominator = getValue(firstOperandDenominatorPart);
        int secondOperandInt = getValue(secondOperandIntPart);
        int secondOperandNominator = getValue(secondOperandNominatorPart);
        int secondOperandDenominator = getValue(secondOperandDenominatorPart);

        int rightAnswerInt = 0;
        int rightAnswerNominator = 0;
        int rightAnswerDenominator = 0;

        switch (selectedOperator) {
            case '+':
                rightAnswerInt = firstOperandInt + secondOperandInt;
                rightAnswerDenominator = firstOperandDenominator * secondOperandDenominator;
                rightAnswerNominator = firstOperandNominator * secondOperandDenominator +
                        secondOperandNominator * firstOperandDenominator;
                break;
            case '-':
                //rightAnswerInt = firstOperandInt - secondOperandInt;
                firstOperandNominator = firstOperandNominator + firstOperandDenominator * firstOperandInt;
                secondOperandNominator = secondOperandNominator + secondOperandDenominator * secondOperandInt;
                rightAnswerDenominator = firstOperandDenominator * secondOperandDenominator;
                rightAnswerNominator = firstOperandNominator * secondOperandDenominator -
                        secondOperandNominator * firstOperandDenominator;
                rightAnswerInt = rightAnswerNominator / rightAnswerDenominator;
                rightAnswerNominator = rightAnswerNominator % rightAnswerDenominator;
                break;
            case '*': {
                rightAnswerInt = 0;
                int realFirstNominator = firstOperandNominator + firstOperandInt * firstOperandDenominator;
                int realSecondNominator = secondOperandNominator + secondOperandInt * secondOperandDenominator;
                rightAnswerNominator = realFirstNominator * realSecondNominator;
                rightAnswerDenominator = firstOperandDenominator * secondOperandDenominator;
            }
            break;
            case '/': {
                rightAnswerInt = 0;
                int realFirstNominator = firstOperandNominator + firstOperandInt * firstOperandDenominator;
                int realSecondNominator = secondOperandNominator + secondOperandInt * secondOperandDenominator;
                rightAnswerNominator = realFirstNominator * secondOperandDenominator;
                rightAnswerDenominator = realSecondNominator * firstOperandDenominator;
            }
            break;
        }
        if (Math.abs(rightAnswerNominator) > rightAnswerDenominator) {
            int ratio = rightAnswerNominator / rightAnswerDenominator;
            rightAnswerInt += ratio;
            rightAnswerNominator -= ratio * rightAnswerDenominator;
        }
        int gcd = GDC(rightAnswerNominator, rightAnswerDenominator);
        rightAnswerNominator /= gcd;
        rightAnswerDenominator /= gcd;
        FractionNumber correctAnswer = new FractionNumber(rightAnswerInt, rightAnswerNominator, rightAnswerDenominator);
        return correctAnswer.toString();

    }

//    private void convertToFraction(int intPart, int nominator, int denominator, int[] answer) {
//        answer[0] = intPart * denominator + nominator;
//        answer[1] = denominator;
//    }

    protected int getAnswerButtonID() {
        return R.id.answerButtonID;
    }

    protected int getFinishButtonID() {
        return R.id.finishButtonID;
    }

    @Override
    protected int getDrawButtonID() {
        return R.id.drawButtonFID;
    }

    @Override
    protected int getLearnButtonID() {
        return R.id.learnFractionButtonID;
    }

    @Override
    protected String getExercise() {
        String firstIntStr = firstOperandIntPart.getText().toString();
        String secondIntStr = secondOperandIntPart.getText().toString();
        FractionNumber first = new FractionNumber(Integer.parseInt(firstIntStr.isEmpty()?"0":firstIntStr), Integer.parseInt(firstOperandNominatorPart.getText().toString()), Integer.parseInt(firstOperandDenominatorPart.getText().toString()));
        FractionNumber second = new FractionNumber(Integer.parseInt(secondIntStr.isEmpty()?"0":secondIntStr), Integer.parseInt(secondOperandNominatorPart.getText().toString()), Integer.parseInt(secondOperandDenominatorPart.getText().toString()));

        return first.toString() + "#" + selectedOperator + "#" + second.toString();
    }


}
