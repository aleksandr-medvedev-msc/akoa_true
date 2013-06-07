package com.shema.aoka;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created with IntelliJ IDEA.
 * User: ag
 * Date: 4/26/13
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class DemoObjectFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.


        View rootView = inflater.inflate(
                R.layout.example_layout, container,false);
        Bundle args = getArguments();
        Log.d("VIEW_PAGER","demo object fragment number :");
        TextView tv = (TextView) rootView.findViewById(R.id.text1);
//        tv.setText(
               // Integer.toString(args.getInt(ARG_OBJECT)));
        return rootView;

    }
}
