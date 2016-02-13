package com.simonc312.trendingoninstagram.activities;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.simonc312.trendingoninstagram.fragments.SearchFragment;
import com.simonc312.trendingoninstagram.fragments.TrendingFragment;
import com.simonc312.trendingoninstagram.R;

import java.net.URI;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;


public class SearchResultActivity extends AppCompatActivity implements TrendingFragment.InteractionListener {

    @BindString(R.string.action_back_pressed)String ACTION_BACK_PRESSED;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private String TITLE;
    private int SEARCH_TYPE;
    private LayoutChangeBroadcastReciever broadcastReciever;
    private String QUERY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        handleSearchIntent(getIntent());
        handleSchemeIntent(getIntent());
        setupSupportActionBar();
        broadcastReciever = new LayoutChangeBroadcastReciever();
        swapFragment(TrendingFragment.newInstance(true,QUERY,SEARCH_TYPE));

    }

    @Override
    public void onNewIntent(Intent intent){
        setIntent(intent);
        handleSearchIntent(intent);
    }

    private void setupSupportActionBar(){
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setShowHideAnimationEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(TITLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("ACTION_LAYOUT_CHANGE");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReciever, intentFilter);
    }

    @Override
    protected void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReciever);
    }

    @Override
    public void onBackPressed(){
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_BACK_PRESSED));
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void swapFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLayoutChange(boolean show) {
        // always keep support action bar displayed because its not the parent activity unlike Main
        //getSupportActionBar().setDisplayHomeAsUpEnabled(show);
    }

    @Override
    public void onBackPress(boolean pop){
        if(pop){
            finish();
        }
    }

    private void handleSearchIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Bundle bundle = intent.getBundleExtra(SearchManager.APP_DATA);
            QUERY = intent.getStringExtra(SearchManager.QUERY);
            SEARCH_TYPE = bundle.getInt("queryType");
            TITLE = bundle.getString("title");
        }
    }

    private void handleSchemeIntent(Intent intent) {
        if(intent != null){
            Uri data = intent.getData();
            if(data == null) return;
            QUERY = data.getQueryParameter("id");
            TITLE = data.getQueryParameter("name");
            if(data.getScheme().equals(getString(R.string.profile_scheme))){
                SEARCH_TYPE = SearchFragment.PEOPLE_TYPE;
            } else if(data.getScheme().equals(getString(R.string.tag_scheme))){
                SEARCH_TYPE = SearchFragment.TAG_TYPE;
            }
        }
    }

    private class LayoutChangeBroadcastReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
          //inflate fragment but pass adapter data to fragment
            Toast.makeText(SearchResultActivity.this, "intent received", Toast.LENGTH_SHORT).show();

        }
    }
}
