package com.shema.aoka;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 2/13/13
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class F_OrderWithAnswersFragment extends Fragment {
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_myorders,null);
        return v;
    }
}