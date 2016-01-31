package com.simonc312.trendingoninstagram.Api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

/**
 * Created by Simon on 1/29/2016.
 */
public class TagNameSearchApiRequest extends AbstractApiRequest {

    private String tag;
    public TagNameSearchApiRequest(Context context){
        super(context);
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
        //attach parsed data object or collection in broadcase (must make objects Parseable)
        //LocalBroadcastManager.getInstance(context).sendBroadcast();
        Toast.makeText(context,"tag name search on success",Toast.LENGTH_SHORT).show();
        Log.d("Json Response", jsonResponse.toString());
    }

    @Override
    public void processOnFailure(String response) {
        Toast.makeText(context,"tag name search on failure "+ response,Toast.LENGTH_SHORT).show();
    }
}

