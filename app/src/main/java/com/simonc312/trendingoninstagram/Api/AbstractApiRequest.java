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
    protected RequestListener listener;
    public AbstractApiRequest(Context context, RequestListener listener){
        this.context = context;
        this.listener = listener;
        requestParams = new RequestParams();
        requestParams.add("client_id",CLIENT_ID);
    }

    protected void addParam(String key, String value){
        requestParams.add(key,value);
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
    public void processOnSuccess(JSONObject jsonResponse) {
        listener.onSuccess(jsonResponse);
    }

    @Override
    public void processOnFailure(String response) {
        listener.onFailure(response);
    }

    public interface RequestListener{
        void onSuccess(JSONObject response);
        void onFailure(String response);
    }
}
