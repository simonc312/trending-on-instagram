package com.simonc312.trendingoninstagram;

/**
 * Created by Simon on 1/27/2016.
 */
public class InstagramPostData {

    private String username;
    private String likeCount;
    private String timePosted;
    private String imageSource;


    public InstagramPostData(){
        this.username = "sleepgroper";
        this.likeCount = "420";
        this.timePosted = "Just now";
        this.imageSource = "kadkfjs";
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
}
