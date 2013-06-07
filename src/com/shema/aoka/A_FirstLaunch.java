package com.shema.aoka;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.MediaController;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.slidingmenu.lib.SlidingMenu;
import com.actionbarsherlock.view.MenuItem;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;

public class A_FirstLaunch extends SherlockFragmentActivity {
    /**
     * Called when the activity is first created.
     */

    MainPagerAdapter adapter;
    ViewPager pager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /*AokaApplication app = (AokaApplication)getApplication();
        ContentValues values = new ContentValues();
        List<HashMap<String,Object>> columns = app.getXmlParser ().getColumns(getApplicationContext(), "Tank_order");
        for (HashMap<String,Object> map : columns)
        {
            values.put(map.get("name").toString(),"12311111111");
        }
        app.getDb().writeOrder(values,"Tank_order",-1);  */




        /*String[] titles = {"Новости","Индексы","Подписка"};
        final Fragment[] fragments = {new F_News(),new F_Indexes(),new F_Subscription()};
        adapter = new MainPagerAdapter(getSupportFragmentManager(),titles,fragments);
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position) {
                // When swiping between pages, select the
                // corresponding tab.
                for (int i = 0; i < fragments.length; i++)
                {
                    if (i==position)
                    {
                        fragments[position].setHasOptionsMenu(true);
                        fragments[position].setMenuVisibility(true);

                    }
                }
                Log.d("VIEW_PAGER","on page selected");
             //   getActionBar().setSelectedNavigationItem(position);
            }
        });*/
    }

    public class MainPagerAdapter extends FragmentStatePagerAdapter
    {
        private String[] titles;
        private Fragment[] fragments;
        public MainPagerAdapter(FragmentManager fm,String[] titles,Fragment[] fragments)
        {
            super(fm);
            this.titles = titles;
            this.fragments = fragments;
        }
        @Override
        public Fragment getItem(int index)
        {
            Fragment fragment = fragments[index];
            return fragment;
        }
        @Override
        public int getCount()
        {
            return titles.length;
        }
        @Override
        public CharSequence getPageTitle(int index)
        {
            return titles[index];
        }
    }

}
