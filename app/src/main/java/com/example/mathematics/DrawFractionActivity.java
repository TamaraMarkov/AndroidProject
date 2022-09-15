package com.example.mathematics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

public class DrawFractionActivity extends AppCompatActivity {


    private ArrayList<Fraction> fractions=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_fraction);
        Intent intent=getIntent();
        if(intent==null)
        {
            finish();
            return;
        }
        String firstOperandIntPart=intent.getStringExtra("firstOperandIntPart");
        TextView fractionFirstIntID= findViewById(R.id.fractionFirstIntID);
        fractionFirstIntID.setText(firstOperandIntPart);

        String secondOperandIntPart=intent.getStringExtra("secondOperandIntPart");
        TextView fractionSecondIntID= findViewById(R.id.fractionSecondIntID);
        fractionSecondIntID.setText(secondOperandIntPart);

        String firstOperandNominatorPart=intent.getStringExtra("firstOperandNominatorPart");
        TextView fractionFirstNominatorID= findViewById(R.id.fractionFirstNominatorID);
        fractionFirstNominatorID.setText(firstOperandNominatorPart);

        String secondOperandNominatorPart=intent.getStringExtra("secondOperandNominatorPart");
        TextView fractionSecondNominatorID= findViewById(R.id.fractionSecondNominatorID);
        fractionSecondNominatorID.setText(secondOperandNominatorPart);

        String operator=intent.getStringExtra("operator");
        TextView fractionOperatorID= findViewById(R.id.fractionOperatorID);
        fractionOperatorID.setText(operator);



        String firstOperandDenominatorPart=intent.getStringExtra("firstOperandDenominatorPart");
        TextView fractionFirstDenominatorID= findViewById(R.id.fractionFirstDenominatorID);
        fractionFirstDenominatorID.setText(firstOperandDenominatorPart);

        String secondOperandDenominatorPart=intent.getStringExtra("secondOperandDenominatorPart");
        TextView fractionSecondDenominatorID= findViewById(R.id.fractionSecondDenominatorID);
        fractionSecondDenominatorID.setText(secondOperandDenominatorPart);

        int firstDenominator=Integer.parseInt(firstOperandDenominatorPart);
        int secondDenominator=Integer.parseInt(secondOperandDenominatorPart);


        fractions.add(new Fraction(2,firstDenominator*secondDenominator));
        FractionsAdapter adapter=new FractionsAdapter(fractions);
        LinearLayoutManager manager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        RecyclerView rv= findViewById(R.id.listOfCicles);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
        Button addButton=findViewById(R.id.addCircleButton);
        Button removeButton=findViewById(R.id.removeCircleButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fractions.add(new Fraction(2,firstDenominator*secondDenominator));
                adapter.notifyItemInserted(fractions.size()-1);
                manager.scrollToPosition(fractions.size()-1);

            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index=fractions.size()-1;
                fractions.remove(index);

                adapter.notifyItemRemoved(index);

            }
        });






    }


}