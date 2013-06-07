package com.shema.aoka;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 10.02.13
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public class F_MyAnswers extends Fragment {
    final String LOG_TAG = "F_MYANSWERS_LOG";
    final String[] from = {"title","date","number"};
    final int[] to = {R.id.f_listfragment_lv_item_title,R.id.f_listfragment_lv_item_date,R.id.f_listfragment_lv_item_number};
    final String dbTableName = "Answers";
    final String orderId = "global_id";
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_myanswers,null);
        Fragment fragment = new F_ListFragment(from,to,dbTableName,orderId);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.f_myanswers_layout,fragment);
        ft.addToBackStack("F_LISTFRAGMENT");
        ft.commit();
        return v;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "Fragment1 onAttach");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Fragment1 onCreate");
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "Fragment1 onActivityCreated");
    }

    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "Fragment1 onStart");
    }

    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Fragment1 onResume");
    }

    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "Fragment1 onPause");
    }

    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "Fragment1 onStop");
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "Fragment1 onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Fragment1 onDestroy");
    }

    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "Fragment1 onDetach");
    }
}