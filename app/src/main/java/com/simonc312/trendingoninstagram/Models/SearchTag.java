package com.simonc312.trendingoninstagram.Models;

/**
 * Created by Simon on 1/31/2016.
 */
public class SearchTag {
    protected String name;
    protected int totalPosts;

    public SearchTag(String name, int totalPosts){
        this.name = name;
        this.totalPosts = totalPosts;
    }

    public SearchTag(String name){
        this.name = name;
        this.totalPosts = -1;
    }

    public String getName() {
        return name;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public String getDisplayName(){
        return String.format("#%s",name);
    }

    public String getDisplayTotalPosts(){
        if(totalPosts == -1)
            return "";
        else
            return String.format("%s posts",totalPosts);
    }
}
