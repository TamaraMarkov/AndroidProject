package com.example.mathematics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DrawActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener, View.OnClickListener {

    private ImageView img1;
    private LinearLayout lytImg;
    private FrameLayout lytToDrop;
    private Button finishButton;
    private TextView textExercise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        lytImg = findViewById(R.id.lytImages);
        lytToDrop = findViewById(R.id.lytToDrop);
        finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(this);
        lytToDrop.setOnDragListener(this);
        textExercise = findViewById(R.id.textExercise);
        Bundle bundle = getIntent().getExtras();
        String exercise = bundle.getString("exercise");
        textExercise.setText(exercise);
        for (int i = 0; i < lytImg.getChildCount(); i++) {
            lytImg.getChildAt(i).setOnTouchListener(this);
        }


    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
            case DragEvent.ACTION_DRAG_ENTERED:
            case DragEvent.ACTION_DRAG_LOCATION:
            case DragEvent.ACTION_DRAG_EXITED:
            case DragEvent.ACTION_DRAG_ENDED:
                return true;
            case DragEvent.ACTION_DROP:
                ImageView dragedView = (ImageView) dragEvent.getLocalState();
                FrameLayout container = (FrameLayout) view;
                ImageView imgCopy = new ImageView(this);


                imgCopy.setImageDrawable(dragedView.getDrawable());
                imgCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        container.removeView(view);
                    }
                });

                container.addView(imgCopy);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imgCopy.getLayoutParams();
                if (params != null) {
                    params.width = dpToPx(100, this);
                    params.height = dpToPx(100, this);
                    imgCopy.setX(dragEvent.getX()-dpToPx(50,this));
                    imgCopy.setY(dragEvent.getY()-dpToPx(50,this));
                    imgCopy.setLayoutParams(params);

                }
                return true;


        }
        return false;
    }

    public static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(view);
        view.startDragAndDrop(null, dragShadow, view, 0);
        return true;


    }

    @Override
    public void onClick(View view) {
//        Intent intent=new Intent(this,SimpleNumbersExerciseActivity.class);
//       startActivity(intent);
        finish();

    }
}