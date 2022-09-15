package com.example.mathematics;

public class VideoItem {
    private String linkName;
    private String url;
    private  int imageID;

    public VideoItem(String linkName, String url, int imageID) {
        this.linkName = linkName;
        this.url = url;
        this.imageID = imageID;
    }


    public String getLinkName() {
        return linkName;
    }

    public String getUrl() {
        return url;
    }

    public int getImageID() {
        return imageID;
    }

}
