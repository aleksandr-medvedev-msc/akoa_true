package com.shema.aoka;

import android.app.Activity;
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
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class F_Indexes extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.f_indexes,null);
        Fragment fragment = new F_IndexFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.f_indexes_layout,fragment);
        ft.commit();
        return v;
    }
}
