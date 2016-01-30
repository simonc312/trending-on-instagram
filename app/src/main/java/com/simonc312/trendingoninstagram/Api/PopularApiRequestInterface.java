package com.simonc312.trendingoninstagram.Api;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by Simon on 1/29/2016.
 */
public class PopularApiRequestInterface extends AbstractApiRequest {
    public PopularApiRequestInterface(Context context){
        super(context);
    }
    @Override
    public String getUrl() {
        return "https://api.instagram.com/v1/media/popular?";
    }

    @Override
    public void processOnSuccess(JSONObject jsonResponse) {
        //attach parsed data object or collection in broadcase (must make objects Parseable)
        //LocalBroadcastManager.getInstance(context).sendBroadcast();
        Toast.makeText(context,"popular on success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void processOnFailure(String response) {
        Toast.makeText(context,"popular on failure",Toast.LENGTH_SHORT).show();
    }
}
