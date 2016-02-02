package com.simonc312.trendingoninstagram.Fragments;

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

import com.simonc312.trendingoninstagram.Adapters.SearchAdapter;
import com.simonc312.trendingoninstagram.Api.AbstractApiRequest.RequestListener;
import com.simonc312.trendingoninstagram.Api.InstagramApiHandler;
import com.simonc312.trendingoninstagram.Api.TagSearchApiRequest;
import com.simonc312.trendingoninstagram.Models.SearchTag;
import com.simonc312.trendingoninstagram.R;
import com.simonc312.trendingoninstagram.StyleHelpers.HorizontalDividerItemDecoration;

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

    @BindString(R.string.action_query_changed) String ACTION_QUERY_CHANGED;
    private InteractionListener mListener;
    private SearchAdapter adapter;
    private static String DEFAULT_QUERY = "photo";
    private SearchQueryChangeReceiver receiver;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new SearchQueryChangeReceiver();
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
        fetchTagSearchAsync(DEFAULT_QUERY);

        return view;
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface InteractionListener {
        void onListFragmentInteraction(String query);
    }

    private class SearchQueryChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String newQuery = intent.getStringExtra("query");
                fetchTagSearchAsync(newQuery);
            }
        }
    }
}
