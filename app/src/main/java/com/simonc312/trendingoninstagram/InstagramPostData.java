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
    private String profileImageSource;


    public InstagramPostData(){
        this.username = "sleepgroper";
        this.likeCount = "420";
        this.timePosted = "1296251679";
        this.caption = "Caption caption caption";
        this.imageSource = "iv_image source";
        this.profileImageSource = "profile img";
    }

    public InstagramPostData(String username, String profileImageSource, String likeCount, String timePosted, String caption, String imageSource){
        this.username = username;
        this.profileImageSource = profileImageSource;
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

    public String getProfileImageSource() {
        return profileImageSource;
    }

    public String getRelativeTimePosted(){
        //in seconds
        long timeDifference = (System.currentTimeMillis()/1000 - Long.valueOf(this.timePosted));
        String[] timeUnits = new String[]{"Just now","min","h","d","w","m","y"};
        int[] incrementFactors = new int[]{60,60,24,7,4,12};
        int index = 0;
        while(index < incrementFactors.length && timeDifference > incrementFactors[index]){
            timeDifference /= incrementFactors[index];
            index++;
        }
        int formattedTimeDifference = Math.round(timeDifference);

        return (index == 0) ? timeUnits[index] : String.format("%d%s",formattedTimeDifference,timeUnits[index]);
    }

    public String getDisplayLikeCount(){
        return String.format("%s likes",this.likeCount);
    }
}
