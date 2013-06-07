package com.shema.aoka;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 2/18/13
 * Time: 3:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class F_News extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.f_news,null);
        Fragment newsFragment = new F_NewsListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.f_news_layout,newsFragment);
        ft.commit();
        return v;
    }
}
