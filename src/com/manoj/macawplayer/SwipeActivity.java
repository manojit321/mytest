package com.manoj.macawplayer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

import com.manoj.adapters.PagerAdapter;
import com.manoj.fragments.AlbumFragment;
import com.manoj.fragments.ArtistFragment;
import com.manoj.fragments.SongFragment;
import com.manoj.helper.FileHandlers;
import com.manoj.helper.Utilities;
import com.manoj.macawplayer.SwipeActivity.TabFactory;
 
/**
 * The <code>TabsViewPagerFragmentActivity</code> class implements the Fragment activity that maintains a TabHost using a ViewPager.
 * @author mwho
 */
public class SwipeActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
 
    private TabHost mTabHost;
    private ViewPager mViewPager;
    private LinkedHashMap<String, TabInfo> mapTabInfo = new LinkedHashMap<String, SwipeActivity.TabInfo>();
    private PagerAdapter mPagerAdapter;
    private LinearLayout homeScreen;
    private Utilities utilities;
    /**
     *
     * @author mwho
     * Maintains extrinsic info of a tab's construct
     */
    private class TabInfo {
         private String tag;
         private Class<?> clss;
         private Bundle args;
         private Fragment fragment;
         TabInfo(String tag, Class<?> clazz, Bundle args) {
             this.tag = tag;
             this.clss = clazz;
             this.args = args;
         }
 
    }
    /**
     * A simple factory that returns dummy views to the Tabhost
     * @author mwho
     */
    class TabFactory implements TabContentFactory {
 
        private final Context mContext;
 
        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }
 
        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
 
    }
    /** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    protected void onCreate(Bundle savedInstanceState) {
    	utilities = new Utilities();
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        // Inflate the layout
        setContentView(R.layout.tabs_viewpager_layout);
        homeScreen=(LinearLayout)findViewById(R.id.tablayout);
        //homeScreen.setBackgroundColor(Color.parseColor(new FileHandlers().findKeyValue("temp.txt", getApplicationContext(), "backgroundColor")));
        //utilities.colorSeter(homeScreen, getApplicationContext());
        
        // Initialise the TabHost
        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        // Intialise ViewPager
        this.intialiseViewPager();
    }
 
    /** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }
 
    /**
     * Initialise ViewPager
     */
    private void intialiseViewPager() {
 
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, AlbumFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, ArtistFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, SongFragment.class.getName()));
        this.mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        //
        this.mViewPager = (ViewPager)super.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }
 
    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;
        SwipeActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator("Albums"), ( tabInfo = new TabInfo("Tab1", AlbumFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        SwipeActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator("Artist"), ( tabInfo = new TabInfo("Tab2", ArtistFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        SwipeActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator("Songs"), ( tabInfo = new TabInfo("Tab3", SongFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);
    }
 
    /**
     * Add Tab content to the Tabhost
     * @param activity
     * @param tabHost
     * @param tabSpec
     * @param clss
     * @param args
     */
    private static void AddTab(SwipeActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }
 
    /** (non-Javadoc)
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */
    public void onTabChanged(String tag) {
        //TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        Log.i("", "onTabChanged   current pos :"+pos+"tag selected  :"+tag);
        this.mViewPager.setCurrentItem(pos);
    }
 
    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
            int positionOffsetPixels) {
        // TODO Auto-generated method stub
 
    }
 
    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     */
    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
    	Log.i("", "onPageSelected   current pos :"+position);
        this.mTabHost.setCurrentTab(position);
    }
 
    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub
 
    }
}