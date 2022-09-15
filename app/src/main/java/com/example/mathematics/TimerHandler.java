package com.example.mathematics;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimerHandler {
    private Handler handler;
    private long endTime;
    private Activity activity;
    protected TextView timerTextView;
    private Runnable overCallback;
    private boolean isStop=false;

    public boolean isOver() {
        return isOver;
    }

    protected boolean isOver=false;

    public TimerHandler(Activity activity,int level,Runnable overCallback) {
        this.activity=activity;
        handler = new Handler(activity.getMainLooper());
        int seconds=level==GameActivity.BEGINNER_USER?10:20;
        endTime = System.currentTimeMillis()+seconds*1000;
        this.overCallback=overCallback;
        updateTimer();

    }
    public void stop(){
        isStop=true;

    }
    public void findViews(){
        activity.findViewById(R.id.timerLayout).setVisibility(View.VISIBLE);
        timerTextView = activity.findViewById(R.id.timerTextView);

    }

    public static String prettifyTime(long millis) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }


    public void updateTimer() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(isStop){
                    return;
                }
                long duration = endTime - System.currentTimeMillis();
                if (duration<=0) {
                    isOver=true;
                    duration=0;

                }
                String time = prettifyTime(duration);
                timerTextView.setText(time);
                if (duration != 0) {
                    handler.postDelayed(this, 500);
                }else {
                    overCallback.run();
                }
            }
        };
        handler.postDelayed(runnable, 500);

    }

}
