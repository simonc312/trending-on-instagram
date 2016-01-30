package com.simonc312.trendingoninstagram;

import static android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.simonc312.trendingoninstagram.Api.InstagramApiHandler;
import com.simonc312.trendingoninstagram.Api.PopularApiRequestInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @BindString(R.string.action_layout_change) String ACTION_LAYOUT_CHANGE;
    @BindString(R.string.client_id) String CLIENT_ID;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @Bind(R.id.rvItems)
    RecyclerView recyclerView;
    private InstagramAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private LayoutChangeBroadcastReciever broadcastReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupRV(this, recyclerView);
        setupSwipeToRefresh(swipeContainer);
        broadcastReciever = new LayoutChangeBroadcastReciever();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ACTION_LAYOUT_CHANGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReciever, intentFilter);
    }

    @Override
    protected void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReciever);
    }

    private void setupSwipeToRefresh(SwipeRefreshLayout swipeContainer) {
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    private void setupRV(Context context, RecyclerView recyclerView) {
        if(adapter == null){
            adapter = new InstagramAdapter(this,true);
        }
        if(layoutManager == null){
            layoutManager = new GridLayoutManager(context,3);//new LinearLayoutManager(context);
        }

        updateRV(layoutManager, adapter);
    }

    private void updateRV(RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter){
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void fetchTimelineAsync() {
        InstagramApiHandler handler = InstagramApiHandler.getInstance();
        handler.sendRequest(new PopularApiRequestInterface(this));
        String url = "https://api.instagram.com/v1/media/popular?client_id="+CLIENT_ID;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                handleSuccessResponse(response);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                handleErrorResponse(res);
                swipeContainer.setRefreshing(false);
            }
        });

    }

    private void fetchTagNameSearchAsync(String tag){
        String url = String.format("https://api.instagram.com/v1/tags/%s/media/recent?client_id=%s",tag,CLIENT_ID);
    }
    private void fetchTagSearchAsync(String query){
        String url = String.format("https://api.instagram.com/v1/tags/search/?q=%s&client_id=%s",query,CLIENT_ID);
    }

    private void handleSuccessResponse(JSONObject response) {
        try {
            JSONArray dataArray = response.getJSONArray("data");
            // - type = "iv_image" or "video"
            // - caption.text
            // - images.standard_resolution.url
            // - user.username
            // - likes.count
            for(int i=0; i<dataArray.length();i++){
                JSONObject data = dataArray.getJSONObject(i);
                if(data.getString("type").equals("image")){
                    String username = data.getJSONObject("user").getString("username");
                    String profileImageSource = data.getJSONObject("user").getString("profile_picture");
                    String imageSource = data.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                    String caption = data.has("caption") && !data.isNull("caption") ? data.getJSONObject("caption").getString("text") : "";
                    String likeCount = data.getJSONObject("likes").getString("count");
                    String timePosted = data.getString("created_time");
                    JSONArray comments = data.getJSONObject("comments").getJSONArray("data");
                    InstagramPostData postData = new InstagramPostData(username, profileImageSource, likeCount, timePosted, caption, imageSource);
                    adapter.addPost(postData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleErrorResponse(String response) {
        Log.e("Error",response);
    }

    private class LayoutChangeBroadcastReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
          //inflate fragment but pass adapter data to fragment
            Toast.makeText(MainActivity.this,"intent received",Toast.LENGTH_SHORT).show();
        }
    }
}
