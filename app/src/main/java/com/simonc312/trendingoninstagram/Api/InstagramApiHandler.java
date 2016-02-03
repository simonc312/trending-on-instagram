package com.simonc312.trendingoninstagram.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Simon on 1/29/2016.
 */
public class InstagramApiHandler {
    private AsyncHttpClient client;
    private static InstagramApiHandler singleton;

    private InstagramApiHandler(){
        client = new AsyncHttpClient();
    }

    public static InstagramApiHandler getInstance(){
        if(singleton == null)
            singleton = new InstagramApiHandler();
        return singleton;
    }

    public void sendRequest(final ApiRequestInterface request){
        client.get(
                request.getContext(),
                request.getUrl(),
                request.getParams(),
                new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                request.processOnSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                request.processOnFailure(res);
            }
        });
    }
}
