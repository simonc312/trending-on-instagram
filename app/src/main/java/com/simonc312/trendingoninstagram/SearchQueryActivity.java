package com.simonc312.trendingoninstagram;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.simonc312.trendingoninstagram.Fragments.SearchFragment;
import com.simonc312.trendingoninstagram.Fragments.TrendingFragment;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;


public class SearchQueryActivity extends AppCompatActivity
        implements SearchFragment.InteractionListener {
    @BindString(R.string.action_query_changed) String ACTION_QUERY_CHANGED;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupSupportActionBar();
        swapFragment(SearchFragment.newInstance());

    }

    private void setupSupportActionBar(){
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHideOnContentScrollEnabled(true);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setShowHideAnimationEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            case R.id.action_search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Search users or tags");
        searchView.setIconifiedByDefault(false);
        // set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //min query length to update
                if (newText.length() >= 2) {
                    Intent intent = new Intent(ACTION_QUERY_CHANGED);
                    intent.putExtra("query", newText);
                    LocalBroadcastManager.getInstance(SearchQueryActivity.this).sendBroadcast(intent);
                }
                return false;
            }
        });

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
    public void onListFragmentInteraction(String query) {
        if(searchView != null){
            searchView.setQuery(query,true);
        }
    }
}
