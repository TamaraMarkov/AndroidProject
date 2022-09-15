package com.example.mathematics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityWithMenu extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.chooseAvatar);
        MenuItem volumeItem = menu.findItem(R.id.volumeChange);
        if(User.isIsGuest()){
            item.setVisible(false);

        }
        if(User.getLoggedUser()==null){
            volumeItem.setVisible(false);
        }
        else{
            if(User.isIsVolume()){
                volumeItem.setIcon(R.drawable.ic_baseline_volume_up_24);
            }
            else{
                volumeItem.setIcon(R.drawable.ic_baseline_volume_off_24);

            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    protected void clap(){
        if(User.isIsVolume()){
            mediaPlayer.start();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = MediaPlayer.create(this, R.raw.clapping);
        updatePointsView();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    protected void handleAvatar(int level){
        handleAvatar(level,1);

    }
    protected void handleAvatar(int level,int numberOfCorrectAnswers){

        boolean isOverStep = User.addPoints(level == GameActivity.BEGINNER_USER ? 5*numberOfCorrectAnswers : 10*numberOfCorrectAnswers);
        updatePointsView();
        if (isOverStep) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose avatar");
            builder.setMessage("Do you want to choose new avatar?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getBaseContext(), ChooseAvatarActivity.class);
                    startActivity(intent);

                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logout) {
            SharedPreferences settings = getApplicationContext().getSharedPreferences(MainActivity.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("UserName", "");
            editor.putString("Password", "");
            editor.apply();
            // exit the application
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Confirm");
            builder.setMessage("Are you sure?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    finishAffinity();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    User.setLoggedUser(null,true);
                    startActivity(intent);


                    //System.exit(0);

                    ////dialog.dismiss();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
        else if(item.getItemId()==R.id.chooseAvatar){

            Intent intent = new Intent(getBaseContext(), ChooseAvatarActivity.class);
            startActivity(intent);

        }
        else if(item.getItemId()==R.id.volumeChange){
            boolean isVolume = User.isIsVolume();
            isVolume=!isVolume;
            User.setIsVolume(isVolume);
            if(isVolume){
                item.setIcon(R.drawable.ic_baseline_volume_up_24);
            }
            else{
                item.setIcon(R.drawable.ic_baseline_volume_off_24);

            }
        }
        return true;
    }
    protected void updatePointsView() {
        if (User.getLoggedUser() != null)
            setTitle("Mathematics (points: " + User.getLoggedUserPoints() + ")");
        else
            setTitle("Mathematics");
    }
}
