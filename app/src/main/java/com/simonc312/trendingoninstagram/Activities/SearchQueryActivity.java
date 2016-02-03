package com.simonc312.trendingoninstagram.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SearchEvent;

import com.simonc312.trendingoninstagram.fragments.SearchFragment;
import static com.simonc312.trendingoninstagram.fragments.SearchFragment.TAG_TYPE;
import static com.simonc312.trendingoninstagram.fragments.SearchFragment.PEOPLE_TYPE;

import com.simonc312.trendingoninstagram.models.SearchTag;
import com.simonc312.trendingoninstagram.R;
import com.simonc312.trendingoninstagram.models.UserTag;
import com.simonc312.trendingoninstagram.viewPagers.ViewPagerAdapter;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;


public class SearchQueryActivity extends AppCompatActivity
        implements SearchFragment.InteractionListener {
    @BindString(R.string.action_query_changed) String ACTION_QUERY_CHANGED;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    private SearchView searchView;
    private ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_query);
        ButterKnife.bind(this);
        setupSupportActionBar();
        setupViewPager();
    }

    private void setupViewPager() {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(SearchFragment.newInstance(PEOPLE_TYPE),"People");
        pagerAdapter.addFragment(SearchFragment.newInstance(TAG_TYPE), "Tags");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

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
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        // set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        menuItem.expandActionView();

        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                finish();
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });

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

    @Override
    public void onListFragmentInteraction(SearchTag searchTag) {
        if(searchView != null){
            //searchView.setQuery(searchTag.getSearchName(),true);
            int queryType = searchTag instanceof UserTag ? SearchFragment.PEOPLE_TYPE : SearchFragment.TAG_TYPE;
            Bundle bundle = new Bundle();
            bundle.putString("title",searchTag.getDisplayName());
            bundle.putInt("queryType", queryType);
            triggerSearch(searchTag.getSearchName(), bundle);
        }
    }
}
