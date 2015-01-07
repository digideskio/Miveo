package com.vorticelabs.miveo.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.vorticelabs.miveo.R;
import com.vorticelabs.miveo.fragments.ChannelInfoFragment;
import com.vorticelabs.miveo.fragments.ChannelVideosListFragment;

public class ChannelViewActivity extends FragmentActivity
    implements ChannelVideosListFragment.ChannelVideosListFragmentCallbacks {

    public final static String TAG = ChannelViewActivity.class.getSimpleName();

    //Arguments
    public final static String CHANNEL_ID = "channel_id";

    //Variables
    private int mId;
    private ChannelPagerAdapter mPagerAdapter;

    //Controls
    private ViewPager mPager;
    private PagerSlidingTabStrip mTabs;

    //Lifecycle events
    //onCreate
    protected void onCreate(Bundle savedInstanceState) {
        //Setup view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_view);

        //Get controls
        mPager = (ViewPager) findViewById(R.id.pager);
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        //Tabs should expand to consume all width
        mTabs.setShouldExpand(true);

        //Restore instance
        if(savedInstanceState == null) {
            mId = getIntent().getIntExtra(CHANNEL_ID, 0);
        } else {
            mId = savedInstanceState.getInt(CHANNEL_ID);
        }

        //Setup mPager
        mPagerAdapter = new ChannelPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        //Setup Tabs
        mTabs.setViewPager(mPager);

        //ActionBar return action
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //onSaveInstanceState
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CHANNEL_ID, mId);
    }

    //Menu methods
    //onCreateOptionsMenu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_channel_view, menu);
        return true;
    }
    //onOptionsItemSelected
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //FragmentPagerAdapter
    public class ChannelPagerAdapter extends FragmentPagerAdapter {

        private final int INFO_FRAGMENT_POSITION = 0;
        private final int VIDEOS_FRAGMENT_POSITION = 1;
        private final int NUMBER_OF_ITEMS = 2;

        public ChannelPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case INFO_FRAGMENT_POSITION:
                    return ChannelInfoFragment.newInstance(mId);
                case VIDEOS_FRAGMENT_POSITION:
                    return ChannelVideosListFragment.newInstance(mId);
            }
            return null;
        }

        public int getCount() {
            return NUMBER_OF_ITEMS;
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case INFO_FRAGMENT_POSITION:
                    return getResources().getString(R.string.tab_title_main);
                case VIDEOS_FRAGMENT_POSITION:
                    return getResources().getString(R.string.tab_title_videos);
            }
            return super.getPageTitle(position);
        }
    }

    //Fragment Callbacks
    //ChannelVideosListFragment
    public void onChannelVideoClick(int channelId, long videoId) {
        Intent videoActivityIntent = VideoViewActivity.getStartActivityIntent(this, channelId, videoId);
        startActivity(videoActivityIntent);
    }
}
