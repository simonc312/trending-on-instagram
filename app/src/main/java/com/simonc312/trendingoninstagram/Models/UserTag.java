package com.simonc312.trendingoninstagram.models;

/**
 * Created by Simon on 2/2/2016.
 */
public class UserTag extends SearchTag {

    private int id;
    private String profilePicture;

    public UserTag(int id, String name, String profilePicture){
        super(name);
        this.id = id;
        this.profilePicture = profilePicture;
    }

    public String getProfilePicture(){
            return profilePicture;
    }

    @Override
    public String getDisplayName(){
        return String.format("@%s",name);
    }

    @Override
    public String getSearchName(){
        return String.valueOf(id);
    }
}
