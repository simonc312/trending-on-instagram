package com.simonc312.trendingoninstagram.Api;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

/**
 * Created by Simon on 1/29/2016.
 */
public abstract class AbstractApiRequest implements ApiRequestInterface {
    private RequestParams requestParams;
    protected String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    protected Context context;
    public AbstractApiRequest(Context context){
        this.context = context;
        requestParams = new RequestParams();
        requestParams.add("client_id",CLIENT_ID);
    }
    @Override
    public String getUrl() {return "OVERRIDE URL";}

    @Override
    public RequestParams getParams() {
        return requestParams;
    }

    @Override
    public Context getContext(){
        return context;
    }

    @Override
    public void addParam(String key, String value){
        requestParams.add(key,value);
    }

    @Override
    public void processOnSuccess(JSONObject jsonResponse) {
        //attach parsed data object or collection in broadcase (must make objects Parseable)
        //LocalBroadcastManager.getInstance(context).sendBroadcast();
        Toast.makeText(context,"abstract on success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void processOnFailure(String response) {
        Toast.makeText(context,"abstract on failure",Toast.LENGTH_SHORT).show();
    }
}
