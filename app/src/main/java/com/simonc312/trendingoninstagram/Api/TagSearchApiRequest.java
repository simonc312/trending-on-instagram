package com.simonc312.trendingoninstagram.Api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONObject;

/**
 * Created by Simon on 1/29/2016.
 */
public class TagSearchApiRequest extends AbstractApiRequest {

    public TagSearchApiRequest(Context context,RequestListener listener){
        super(context, listener);
    }

    public void setQuery(String query){
        addParam("q",query);
    }

    @Override
    public String getUrl() {
        return "https://api.instagram.com/v1/tags/search/";
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

