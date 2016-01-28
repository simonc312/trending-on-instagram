package com.simonc312.trendingoninstagram;

/**
 * Created by Simon on 1/27/2016.
 */
public class InstagramPostData {

    private String username;
    private String likeCount;
    private String timePosted;
    private String caption;
    private String imageSource;


    public InstagramPostData(){
        this.username = "sleepgroper";
        this.likeCount = "420";
        this.timePosted = "Just now";
        this.caption = "Caption caption caption";
        this.imageSource = "kadkfjs";
    }

    public InstagramPostData(String username, String likeCount, String timePosted, String caption, String imageSource){
        this.username = username;
        this.likeCount = likeCount;
        this.timePosted = timePosted;
        this.caption = caption;
        this.imageSource = imageSource;
    }

    public String getUsername() {
        return username;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public String getImageSource() {
        return imageSource;
    }

    public String getCaption() {
        return caption;
    }
}
