package com.simonc312.trendingoninstagram.Models;

/**
 * Created by Simon on 1/31/2016.
 */
public class SearchTag {
    private String name;
    private int totalPosts;

    public SearchTag(String name, int totalPosts){
        this.name = name;
        this.totalPosts = totalPosts;
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
        return String.format("%s posts",totalPosts);
    }
}
