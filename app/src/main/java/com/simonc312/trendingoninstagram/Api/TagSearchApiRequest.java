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
        //attach parsed data object or collection in broadcase (must make objects Parseable)
        //LocalBroadcastManager.getInstance(context).sendBroadcast();
        Toast.makeText(context,"tag search on success",Toast.LENGTH_SHORT).show();
        Log.d("Json Response",jsonResponse.toString());
    }

    @Override
    public void processOnFailure(String response) {
        Toast.makeText(context,"tag search on failure "+response,Toast.LENGTH_SHORT).show();
    }
}

