package com.simonc312.trendingoninstagram.Api;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

/**
 * Created by Simon on 1/29/2016.
 */
public class TagSearchApiRequestInterface extends AbstractApiRequest {

    public TagSearchApiRequestInterface(Context context){
        super(context);
    }
    @Override
    public String getUrl() {
        return "https://api.instagram.com/v1/tags/search/";
    }

    @Override
    public void processOnSuccess(JSONObject jsonResponse) {
        //attach parsed data object or collection in broadcase (must make objects Parseable)
        //LocalBroadcastManager.getInstance(context).sendBroadcast();
        Toast.makeText(context,"tag search on success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void processOnFailure(String response) {
        Toast.makeText(context,"tag search on failure",Toast.LENGTH_SHORT).show();
    }
}

