package com.simonc312.trendingoninstagram;

import static android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @Bind(R.id.rvItems)
    RecyclerView recyclerView;
    private InstagramAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupRV(this, recyclerView, adapter);
        setupSwipeToRefresh(swipeContainer);
    }

    private void setupSwipeToRefresh(SwipeRefreshLayout swipeContainer) {
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    private void setupRV(Context context, RecyclerView recyclerView, InstagramAdapter adapter) {
        if(adapter == null){
            adapter = new InstagramAdapter();
        }
        if(layoutManager == null){
            layoutManager = new LinearLayoutManager(context);
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void fetchTimelineAsync(int time) {

        swipeContainer.setRefreshing(false);
    }
}
