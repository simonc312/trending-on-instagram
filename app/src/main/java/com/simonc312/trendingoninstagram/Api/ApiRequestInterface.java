package com.simonc312.trendingoninstagram.Api;

import android.content.Context;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

/**
 * Created by Simon on 1/29/2016.
 */
public interface ApiRequestInterface {

    String getUrl();

    RequestParams getParams();

    Context getContext();

    void processOnSuccess(JSONObject jsonResponse);

    void processOnFailure(String response);
}
