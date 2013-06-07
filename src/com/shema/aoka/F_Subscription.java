package com.shema.aoka;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.PageSize;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 3/12/13
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class F_Subscription extends Fragment {
    private final String LOG_TAG = "SUBSCRIPTION_";
    Fragment fragment;
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(LOG_TAG,"fragment 1 on create view()");
        View v = inflater.inflate(R.layout.f_subscription,null);
        fragment = new F_SubscriptionFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.f_subscription_layout,fragment);
        //ft.add(R.id.f_subscription_layout,fragment);
        ft.addToBackStack("F_SUBSCRIPTIONFRAGMENT");
        ft.commit();
        return v;
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
