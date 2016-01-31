package com.simonc312.trendingoninstagram.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.simonc312.trendingoninstagram.Adapters.TrendingAdapter;
import com.simonc312.trendingoninstagram.Models.InstagramPostData;
import com.simonc312.trendingoninstagram.R;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InteractionListener} interface
 * to handle interaction events.
 */
public class TrendingFragment extends Fragment implements TrendingAdapter.PostItemListener {
    @BindInt(R.integer.grid_layout_span_count)
    int GRID_LAYOUT_SPAN_COUNT;
    @BindInt(R.integer.grid_layout_item_spacing)
    int GRID_LAYOUT_ITEM_SPACING;
    @BindString(R.string.action_back_pressed)
    String ACTION_BACK_PRESSED;
    @BindString(R.string.client_id)
    String CLIENT_ID;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @Bind(R.id.rvItems)
    RecyclerView recyclerView;

    private InteractionListener mListener;
    private BackPressedBroadcastListener backPressedListener;
    private TrendingAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean useGridLayout;

    public TrendingFragment() {
        // Required empty public constructor
    }

    public static TrendingFragment newInstance(boolean useGridLayout){
        Bundle bundle = new Bundle();
        bundle.putBoolean("useGridLayout",useGridLayout);
        TrendingFragment fragment = new TrendingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        backPressedListener = new BackPressedBroadcastListener();
        handleArguments(getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending_main,container,false);
        ButterKnife.bind(this, view);
        setupRV(recyclerView, getContext());
        setupSwipeToRefresh(swipeContainer);
        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionListener) {
            mListener = (InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart(){
        super.onStart();
        IntentFilter filter = new IntentFilter(ACTION_BACK_PRESSED);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(backPressedListener, filter);
    }

    @Override
    public void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(backPressedListener);
    }

    @Override
    public void onPostClick(int position){
        if(useGridLayout) {
            adapter.setIsGridLayout(false);
            setLinearLayout();
            updateRV(recyclerView, layoutManager, adapter);
            recyclerView.scrollToPosition(position);
        }
    }

    private void handleArguments(Bundle bundle) {
        if(!bundle.isEmpty()){
            useGridLayout = bundle.getBoolean("useGridLayout");
        }
    }

    /**
     * When back button is pressed and fragment is visible, if linear layout revert to linear layout
     and scroll to current position
     */
    public void onBackPressed() {
        if(!(useGridLayout)){
            adapter.setIsGridLayout(true);
            setGridLayout();
            updateRV(recyclerView,layoutManager,adapter);
            recyclerView.scrollToPosition(recyclerView.getChildAdapterPosition(recyclerView.getFocusedChild()));
        }
    }

    private void setupSwipeToRefresh(SwipeRefreshLayout swipeContainer) {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

    private void setupRV(RecyclerView recyclerView, Context context) {
        if(adapter == null){
            adapter = new TrendingAdapter(context,useGridLayout,this);
        }
        if(layoutManager == null){
            if(useGridLayout)
                setGridLayout();
            else
                setLinearLayout();
        }

        recyclerView.addOnScrollListener(new RVScrollListener() {
            @Override
            public void onHide() {
                mListener.onScrollDown();
            }

            @Override
            public void onShow() {
                mListener.onScrollUp();
            }
        });

        updateRV(recyclerView, layoutManager, adapter);
    }

    private void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        this.layoutManager = layoutManager;
    }
    private void setGridLayout(){
        setLayoutManager(new GridLayoutManager(getContext(), GRID_LAYOUT_SPAN_COUNT));
    }
    private void setLinearLayout(){
        setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void updateRV(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter){
        //need to remove it otherwise
        if(layoutManager instanceof GridLayoutManager)
            recyclerView.addItemDecoration(new GridItemDecoration(GRID_LAYOUT_SPAN_COUNT,GRID_LAYOUT_ITEM_SPACING,false));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void fetchTimelineAsync() {
        /*InstagramApiHandler handler = InstagramApiHandler.getInstance();
        sendRequest(new PopularApiRequest(this));*/
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
        Log.e("Error", response);
    }

    /**
     * Probably needs to be moved outside since other recyclerviews will use it
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface InteractionListener {

        void onScrollDown();

        void onScrollUp();
    }

    private class BackPressedBroadcastListener extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            onBackPressed();
        }
    }
}
