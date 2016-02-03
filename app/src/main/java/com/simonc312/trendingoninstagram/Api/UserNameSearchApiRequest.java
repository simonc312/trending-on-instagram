package com.simonc312.trendingoninstagram.api;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by Simon on 1/29/2016.
 */
public class UserNameSearchApiRequest extends AbstractApiRequest {

    private String query;
    public UserNameSearchApiRequest(Context context, RequestListener listener){
        super(context, listener);
        query = "sleepgroper";
    }

    public void setQuery(String query){
        addParam("q",query);
    }

    @Override
    public String getUrl() {
        return "https://api.instagram.com/v1/users/search?";
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

