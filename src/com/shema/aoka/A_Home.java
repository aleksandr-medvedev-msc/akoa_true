package com.shema.aoka;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

public class A_Home extends SherlockFragmentActivity {
    final String LOG_TAG = "A_HOME_LOG";
    ArrayList<Fragment> currentFragment;
    String currentFragmentString;
    LinearLayout customActionLayout;

    /**
     * Called when the activity is first created.
     */
    SlidingMenu menu;
    M_DB db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        getActionBar().setHomeButtonEnabled(true);
        db = ((AokaApplication)getApplication()).getDb();
        db.open();
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        menu.setBehindOffset(metrics.widthPixels/6);
        //DBFiller filler = new DBFiller((AokaApplication)getApplication());
        SharedPreferences sp = getSharedPreferences("database_fill.xml",MODE_PRIVATE);  //TODO: заполнить еще и чужие заявки!
        if (!sp.getBoolean("News",false))
        {
            DBFiller dbFiller = new DBFiller((AokaApplication)getApplication());
            ((AokaApplication) getApplication()).getXmlParser().indexesToDB((AokaApplication)getApplication(),null);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("News",dbFiller.isFlag());
            db.close();
            editor.commit();
        }
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setMenu(R.layout.main_menu);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_menu,new F_Menu(R.id.home_layout,menu)).commit();
        menu.toggle(true);


        /*ActionBar bar = getActionBar();
        customActionLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.menu_customview,null);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        bar.setCustomView(customActionLayout);
        Button profileButton = (Button)customActionLayout.findViewById(R.id.menu_profile_button);
        profileButton.setOnClickListener(this);
        Button orderButton = (Button)customActionLayout.findViewById(R.id.menu_order_button);
        orderButton.setOnClickListener(this);
        Button analyticsButton  = (Button)customActionLayout.findViewById(R.id.menu_analytics_button);
        analyticsButton.setOnClickListener(this);

        if (this.getTitle().equals("A_Home"))
        {
            orderButton.setBackgroundResource(R.color.a_home_button_enabled);
        }
        if (this.getTitle().equals("A_Profile"))
        {
            profileButton.setBackgroundResource(R.color.a_home_button_enabled);
        }
        if (this.getTitle().equals("A_Analytics"))
        {
            analyticsButton.setBackgroundResource(R.color.a_home_button_enabled);
        } */
        //bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        /*currentFragment = new ArrayList<Fragment>();
        bar.setDisplayShowHomeEnabled(true);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        Fragment myAnswersFragment = new F_MyAnswers();
        Fragment ordersFragment = new F_Orders();
        Fragment myOrdersFragment = new F_MyOrders();
        ActionBar.Tab tab = bar.newTab();
        tab.setText(getResources().getString(R.string.a_home_myorders_fragment));
        tab.setTabListener(this);
        tab.setTag(myOrdersFragment);
        bar.addTab(tab);

        tab = bar.newTab();
        tab.setText(getResources().getString(R.string.a_home_myanswers_fragment));
        tab.setTag(myAnswersFragment);
        tab.setTabListener(this);
        bar.addTab(tab);

        tab = bar.newTab();
        tab.setText(getResources().getString(R.string.a_home_orders_fragment));
        tab.setTabListener(this);
        tab.setTag(ordersFragment);
        bar.addTab(tab);
        */
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        Log.d("sdfsdfsdf","sdfsfawdfasf");

        if (menuItem.getItemId()==android.R.id.home)
        {
            menu.toggle(true);
            return true;
        }
        else {
            return false;
        }
    }
}
