package com.simonc312.trendingoninstagram.api;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by Simon on 1/29/2016.
 */
public class UserProfileSearchApiRequest extends AbstractApiRequest {

    private String userid;
    public UserProfileSearchApiRequest(Context context, RequestListener listener){
        super(context, listener);
        userid = "145126571";
    }

    public void setUserid(String userid){
        this.userid = userid;
    }

    @Override
    public String getUrl() {
        return String.format("https://api.instagram.com/v1/users/%s/media/recent/?", userid);
    }

    @Override
    public void processOnSuccess(JSONObject jsonResponse) {
        super.processOnSuccess(jsonResponse);
    }

    @Override
    public void processOnFailure(String response) {
        super.processOnFailure(response);
    }
}

