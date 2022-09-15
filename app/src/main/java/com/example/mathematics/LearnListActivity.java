package com.example.mathematics;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LearnListActivity extends ActivityWithMenu {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_list);
        RecyclerView learnList = findViewById(R.id.learnListID);
        LearnListAdapter learnListAdapter = new LearnListAdapter(getVideoItems());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        learnList.setLayoutManager(linearLayoutManager);
        learnList.setAdapter(learnListAdapter);
    }
    private List<VideoItem> getVideoItems(){
        ArrayList<VideoItem> videoItems = new ArrayList<>();
        videoItems.add(new VideoItem("Multiplication Word Problems","https://www.youtube.com/watch?v=ojBAgcZXIMo",R.drawable.video_icon_multiplication));
        videoItems.add(new VideoItem("Addition Word Problems ","https://www.youtube.com/watch?v=q7mi24ClSMw",R.drawable.video_icon_addition));
        videoItems.add(new VideoItem("Subtraction Word Problems ","https://www.youtube.com/watch?v=UffIn6yh7QQ",R.drawable.video_icon_subtraction));
        videoItems.add(new VideoItem("Division Word Problems ","https://www.youtube.com/watch?v=oteEjhP1YwA",R.drawable.video_icon_division));
        videoItems.add(new VideoItem("Addition and Multiplication Word Problems ","https://www.youtube.com/watch?v=PbY6QFMF2iM",R.drawable.addition_multiplication));
        videoItems.add(new VideoItem("Addition and Subtraction Word Problems ","https://www.youtube.com/watch?v=8F0dySDLbXU",R.drawable.video_icon_addition_and_subtraction));
        videoItems.add(new VideoItem("Multiplication and Subtraction Word Problems ","https://www.youtube.com/watch?v=XyMBSsm3_is",R.drawable.multiplication_subtraction));





        return videoItems;
    }



}