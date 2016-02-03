package com.simonc312.trendingoninstagram.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.simonc312.trendingoninstagram.adapters.SearchAdapter;
import com.simonc312.trendingoninstagram.api.AbstractApiRequest.RequestListener;
import com.simonc312.trendingoninstagram.api.InstagramApiHandler;
import com.simonc312.trendingoninstagram.api.TagSearchApiRequest;
import com.simonc312.trendingoninstagram.api.UserNameSearchApiRequest;
import com.simonc312.trendingoninstagram.models.SearchTag;
import com.simonc312.trendingoninstagram.models.UserTag;
import com.simonc312.trendingoninstagram.R;
import com.simonc312.trendingoninstagram.helpers.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link InteractionListener}
 * interface.
 */
public class SearchFragment extends Fragment {

    public final static int TAG_TYPE = 0;
    public final static int PEOPLE_TYPE = 1;
    @BindString(R.string.action_query_changed) String ACTION_QUERY_CHANGED;
    private InteractionListener mListener;
    private SearchAdapter adapter;
    private String DEFAULT_TAG_QUERY = "photo";
    private String DEFAULT_PEOPLE_QUERY = "bernie";
    private SearchQueryChangeReceiver receiver;
    private int searchType;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchFragment() {
    }

    public static SearchFragment newInstance(int searchType) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt("searchType",searchType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new SearchQueryChangeReceiver();
        handleArguments(getArguments());
    }

    private void handleArguments(Bundle arguments) {
        if(arguments != null){
            searchType = arguments.getInt("searchType");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_item_list, container, false);
        ButterKnife.bind(this, view);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            adapter = new SearchAdapter( new ArrayList<SearchTag>(), mListener);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            Drawable drawable = getActivity().getResources().getDrawable(android.R.drawable.divider_horizontal_bright);
            recyclerView.addItemDecoration(new HorizontalDividerItemDecoration(drawable));
            recyclerView.setAdapter(adapter);
        }
        fetchAsyncDefault();

        return view;
    }

    private void fetchAsyncDefault() {
        switch(searchType){
            case TAG_TYPE:
                fetchTagSearchAsync(DEFAULT_TAG_QUERY);
                break;
            case PEOPLE_TYPE:
                fetchUserNameSearchAsync(DEFAULT_PEOPLE_QUERY);
                break;
            default:
                fetchTagSearchAsync(DEFAULT_TAG_QUERY);
        }
    }

    private void fetchAsync(String query) {
        switch(searchType){
            case TAG_TYPE:
                fetchTagSearchAsync(query);
                break;
            case PEOPLE_TYPE:
                fetchUserNameSearchAsync(query);
                break;
            default:
                fetchTagSearchAsync(query);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        IntentFilter filter = new IntentFilter(ACTION_QUERY_CHANGED);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
    }

    @Override
    public void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
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

    private void fetchTagSearchAsync(String query){
        TagSearchApiRequest request = new TagSearchApiRequest(getContext(), new RequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    List<SearchTag> newList = new ArrayList<>(dataArray.length());
                    for(int i=0;i<dataArray.length();i++){
                        JSONObject data = dataArray.getJSONObject(i);
                        String name = data.getString("name");
                        int postCount = data.getInt("media_count");
                        SearchTag tag = new SearchTag(name,postCount);
                        newList.add(tag);
                    }
                    adapter.update(newList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String response) {
                Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
            }
        });
        request.setQuery(query);
        InstagramApiHandler.getInstance().sendRequest(request);
    }

    private void fetchUserNameSearchAsync(String query){
        UserNameSearchApiRequest request = new UserNameSearchApiRequest(getContext(), new RequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    List<SearchTag> newList = new ArrayList<>(dataArray.length());
                    for(int i=0;i<dataArray.length();i++){
                        JSONObject data = dataArray.getJSONObject(i);
                        int id = data.getInt("id");
                        String name = data.getString("username");
                        String uri = data.getString("profile_picture");
                        UserTag tag = new UserTag(id,name,uri);
                        newList.add(tag);
                    }
                    adapter.update(newList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String response) {
                Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
            }
        });
        request.setQuery(query);
        InstagramApiHandler.getInstance().sendRequest(request);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface InteractionListener {
        void onListFragmentInteraction(SearchTag selectedTag);
    }

    private class SearchQueryChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String newQuery = intent.getStringExtra("query");
                fetchAsync(newQuery);
            }
        }
    }
}
