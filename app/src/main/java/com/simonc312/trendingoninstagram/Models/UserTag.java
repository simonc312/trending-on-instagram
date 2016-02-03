package com.simonc312.trendingoninstagram.Models;

/**
 * Created by Simon on 2/2/2016.
 */
public class UserTag extends SearchTag {

    private String profilePicture;

    public UserTag(String name, String profilePicture){
        super(name);
        this.profilePicture = profilePicture;
    }

    public String getProfilePicture(){
            return profilePicture;
    }

    @Override
    public String getDisplayName(){
        return String.format("@%s",name);
    }

}
