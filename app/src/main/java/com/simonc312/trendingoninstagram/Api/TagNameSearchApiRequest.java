package com.simonc312.trendingoninstagram.api;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by Simon on 1/29/2016.
 */
public class TagNameSearchApiRequest extends AbstractApiRequest {

    private String tag;
    public TagNameSearchApiRequest(Context context, RequestListener listener){
        super(context, listener);
        tag = "sleepgroper";
    }

    public void setTag(String tag){
        this.tag = tag;
    }

    @Override
    public String getUrl() {
        return String.format("https://api.instagram.com/v1/tags/%s/media/recent?",tag);
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

