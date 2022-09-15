package com.example.mathematics;

import static com.example.mathematics.GameActivity.BEGINNER_USER;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LearnListAdapter extends RecyclerView.Adapter<LearnListAdapter.ViewHolder> {
    private List<VideoItem> videoItems;

    public LearnListAdapter(List<VideoItem> videoItems) {
        this.videoItems = videoItems;
    }

    @NonNull
    @Override
    public LearnListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_learn_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LearnListAdapter.ViewHolder holder, int position) {
        VideoItem videoItem = videoItems.get(position);
        holder.bind(videoItem, position);

    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private View row;
        private TextView learnLink;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.videoImageID);
            learnLink = itemView.findViewById(R.id.learnLinkID);
            row = itemView;

        }

        public void bind(VideoItem videoItem, int position) {
            imageView.setImageResource(videoItem.getImageID());
            learnLink.setText(videoItem.getLinkName());


            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(row.getContext(), Uri.parse(videoItem.getUrl()));

                }
            });

        }

    }
}
