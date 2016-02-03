package com.simonc312.trendingoninstagram.fragments;

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

import com.simonc312.trendingoninstagram.adapters.TrendingAdapter;
import com.simonc312.trendingoninstagram.api.AbstractApiRequest;
import com.simonc312.trendingoninstagram.api.ApiRequestInterface;
import com.simonc312.trendingoninstagram.api.InstagramApiHandler;
import com.simonc312.trendingoninstagram.api.PopularApiRequest;
import com.simonc312.trendingoninstagram.api.TagNameSearchApiRequest;
import com.simonc312.trendingoninstagram.api.UserNameSearchApiRequest;
import com.simonc312.trendingoninstagram.api.UserProfileSearchApiRequest;
import com.simonc312.trendingoninstagram.models.InstagramPostData;
import com.simonc312.trendingoninstagram.R;
import com.simonc312.trendingoninstagram.helpers.EndlessRVScrollListener;
import com.simonc312.trendingoninstagram.helpers.GridItemDecoration;
import com.simonc312.trendingoninstagram.helpers.RVScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InteractionListener} interface
 * to handle interaction events.
 */
public class TrendingFragment extends Fragment
        implements TrendingAdapter.PostItemListener,
        AbstractApiRequest.RequestListener{
    public static final int TRENDING_TYPE = -123;
    @BindInt(R.integer.grid_layout_span_count)
    int GRID_LAYOUT_SPAN_COUNT;
    @BindInt(R.integer.grid_layout_item_spacing)
    int GRID_LAYOUT_ITEM_SPACING;
    @BindString(R.string.action_back_pressed)
    String ACTION_BACK_PRESSED;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @Bind(R.id.rvItems)
    RecyclerView recyclerView;

    private InteractionListener mListener;
    private BackPressedBroadcastListener backPressedListener;
    private TrendingAdapter adapter;
    private boolean useGridLayout;
    private RecyclerView.ItemDecoration itemDecoration;
    private String query;
    //determines where to add new posts
    private boolean addToEnd = false;
    private int queryType;

    public TrendingFragment() {
        // Required empty public constructor
    }

    public static TrendingFragment newInstance(boolean useGridLayout,String query, int queryType){
        Bundle bundle = new Bundle();
        bundle.putBoolean("useGridLayout", useGridLayout);
        bundle.putString("query", query);
        bundle.putInt("queryType",queryType);
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
        fetchAsync();
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
            useGridLayout = false;
            mListener.onLayoutChange(true);
            adapter.setIsGridLayout(false);
            updateRV(recyclerView, getLinearLayout(), adapter);
            recyclerView.scrollToPosition(position);
        }
    }

    @Override
    public void onSuccess(JSONObject response) {
        handleSuccessResponse(response);
        stopRefresh();
    }

    @Override
    public void onFailure(String response) {
        handleErrorResponse(response);
        stopRefresh();
    }

    /**
     * When back button is pressed and fragment is visible, if linear layout revert to grid layout
     and scroll to current position
     */
    public void onBackPressed() {
        if(!(useGridLayout)){
            useGridLayout = true;
            mListener.onLayoutChange(false);
            adapter.setIsGridLayout(true);
            getGridLayout();
            updateRV(recyclerView,getLayout(),adapter);
            recyclerView.scrollToPosition(recyclerView.getChildAdapterPosition(recyclerView.getFocusedChild()));
        } else {
            mListener.onBackPress(true);
        }
    }

    private void handleArguments(Bundle bundle) {
        if(!bundle.isEmpty()){
            useGridLayout = bundle.getBoolean("useGridLayout",true);
            query = bundle.getString("query");
            queryType = bundle.getInt("queryType",TRENDING_TYPE);
        }
    }

    private void setupSwipeToRefresh(SwipeRefreshLayout swipeContainer) {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addToEnd = false;
                fetchAsync();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void stopRefresh(){
        if(swipeContainer != null){
            swipeContainer.setRefreshing(false);
        }
    }

    private void fetchAsync() {
        if(query == null)
            fetchTimelineAsync();
        else{
            if(queryType == SearchFragment.PEOPLE_TYPE)
                fetchUserProfileSearchAsync(query);
            else if(queryType == SearchFragment.TAG_TYPE)
                fetchTagNameSearchAsync(query);
        }
    }

    private void setupRV(RecyclerView recyclerView, Context context) {
        if(adapter == null){
            adapter = new TrendingAdapter(context,useGridLayout,this);
        }

        if(itemDecoration == null){
            itemDecoration = new GridItemDecoration(GRID_LAYOUT_SPAN_COUNT,GRID_LAYOUT_ITEM_SPACING,false);
        }

        recyclerView.addOnScrollListener(new EndlessRVScrollListener() {
            @Override
            public void onLoadMore(int current_page) {
                addToEnd = true;
                fetchAsync();
            }
        });

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

        updateRV(recyclerView, getLayout(), adapter);
    }

    private RecyclerView.LayoutManager getLayout(){
        if(useGridLayout)
            return getGridLayout();
        else
           return getLinearLayout();
    }

    private GridLayoutManager getGridLayout(){
        return new GridLayoutManager(getContext(), GRID_LAYOUT_SPAN_COUNT);
    }
    private LinearLayoutManager getLinearLayout(){
        return new LinearLayoutManager(getContext());
    }

    private void updateRV(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter){
        //need to remove it otherwise
        if(layoutManager instanceof GridLayoutManager)
            recyclerView.addItemDecoration(itemDecoration);
        else
            recyclerView.removeItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void fetchTimelineAsync() {
        sendRequest(new PopularApiRequest(getContext(), this));
    }

    private void fetchTagNameSearchAsync(String tag){
        TagNameSearchApiRequest request = new TagNameSearchApiRequest(getContext(),this);
        request.setTag(tag);
        sendRequest(request);
    }

    private void fetchUserProfileSearchAsync(String userid){
        UserProfileSearchApiRequest request = new UserProfileSearchApiRequest(getContext(),this);
        request.setUserid(userid);
        sendRequest(request);
    }

    private void sendRequest(ApiRequestInterface request){
        InstagramApiHandler.getInstance().sendRequest(request);
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
                    adapter.addPost(postData, addToEnd);
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

        void onLayoutChange(boolean show);

        void onBackPress(boolean pop);
    }

    private class BackPressedBroadcastListener extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            onBackPressed();
        }
    }
}
