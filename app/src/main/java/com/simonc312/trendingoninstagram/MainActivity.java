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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.simonc312.trendingoninstagram.Adapters.InstagramAdapter;
import com.simonc312.trendingoninstagram.Api.InstagramApiHandler;
import com.simonc312.trendingoninstagram.Api.TagNameSearchApiRequest;
import com.simonc312.trendingoninstagram.Api.TagSearchApiRequest;
import com.simonc312.trendingoninstagram.Models.InstagramPostData;
import com.simonc312.trendingoninstagram.StyleHelpers.GridItemDecoration;
import com.simonc312.trendingoninstagram.StyleHelpers.RVScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    @BindString(R.string.item_position_extra)
    String ITEM_POSITION_EXTRA;
    @BindInt(R.integer.grid_layout_span_count)
    int GRID_LAYOUT_SPAN_COUNT;
    @BindInt(R.integer.grid_layout_item_spacing)
    int GRID_LAYOUT_ITEM_SPACING;
    @BindString(R.string.action_layout_change)
    String ACTION_LAYOUT_CHANGE;
    @BindString(R.string.client_id)
    String CLIENT_ID;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @Bind(R.id.rvItems)
    RecyclerView recyclerView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private InstagramAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private LayoutChangeBroadcastReciever broadcastReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHideOnContentScrollEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
        setupRV(recyclerView);
        setupSwipeToRefresh(swipeContainer);
        broadcastReciever = new LayoutChangeBroadcastReciever();
        //fetchTimelineAsync();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(layoutManager instanceof GridLayoutManager)
            super.onBackPressed();
        else{
            adapter.setIsGridLayout(true);
            layoutManager = new GridLayoutManager(this,GRID_LAYOUT_SPAN_COUNT);
            updateRV(recyclerView,layoutManager,adapter);
            recyclerView.scrollToPosition(recyclerView.getChildAdapterPosition(recyclerView.getFocusedChild()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Search users or tags");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchTagSearchAsync(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
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

    private void setupRV(RecyclerView recyclerView) {
        if(adapter == null){
            adapter = new InstagramAdapter(this,true);
        }
        if(layoutManager == null){
            setGridLayout();
        }

        recyclerView.addOnScrollListener(new RVScrollListener() {
            @Override
            public void onHide() {
                getSupportActionBar().hide();
                //toolbarContainer.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            }

            @Override
            public void onShow() {
                getSupportActionBar().show();
                //toolbarContainer.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
            }
        });

        updateRV(recyclerView, layoutManager, adapter);
    }

    private void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        this.layoutManager = layoutManager;
    }
    private void setGridLayout(){
        setLayoutManager(new GridLayoutManager(this,GRID_LAYOUT_SPAN_COUNT));
    }
    private void setLinearLayout(){
        setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateRV(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter){
        if(layoutManager instanceof GridLayoutManager)
            recyclerView.addItemDecoration(new GridItemDecoration(GRID_LAYOUT_SPAN_COUNT,GRID_LAYOUT_ITEM_SPACING,false));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void fetchTimelineAsync() {
        /*InstagramApiHandler handler = InstagramApiHandler.getInstance();
        handler.sendRequest(new PopularApiRequest(this));*/
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
        TagNameSearchApiRequest request = new TagNameSearchApiRequest(this);
        request.setTag(tag);
        //handler.sendRequest(request);
    }
    private void fetchTagSearchAsync(String query){
        InstagramApiHandler handler = InstagramApiHandler.getInstance();
        TagSearchApiRequest request = new TagSearchApiRequest(this);
        request.setQuery(query);
        handler.sendRequest(request);
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
            adapter.setIsGridLayout(false);
            setLinearLayout();
            updateRV(recyclerView,layoutManager, adapter);
            recyclerView.scrollToPosition(intent.getIntExtra(ITEM_POSITION_EXTRA, 0));
        }
    }
}
