package com.simonc312.trendingoninstagram.Api;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by Simon on 1/29/2016.
 */
public class PopularApiRequest extends AbstractApiRequest {
    public PopularApiRequest(Context context,RequestListener listener){
        super(context,listener);
    }
    @Override
    public String getUrl() {
        return "https://api.instagram.com/v1/media/popular?";
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
