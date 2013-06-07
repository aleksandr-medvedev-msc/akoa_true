package com.shema.aoka;

import android.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 3/12/13
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class F_SubscriptionFragment extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.f_subscriptionfragment,null);
        return v;
    }
    private final String LOG_TAG = "F_SUBSCRIPTIONFRAGMENT_LOG";
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        //setHasOptionsMenu(true);
        String[] data = getResources().getStringArray(R.array.orders_russian_names);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice,data);
        setListAdapter(adapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        SharedPreferences sp = getActivity().getSharedPreferences("subscription",Context.MODE_PRIVATE);
        for(int i = 0; i < Constants.typesOfOrders.length; i++)
        {
            if (sp.getBoolean(Constants.typesOfOrders[i],false))
            {
                Log.d("F_SUBSCRIPTIONFRAGMENT_LOG",i + " _ " + Constants.typesOfOrders[i]);
                getListView().setItemChecked(adapter.getPosition(data[i]),true);
            }
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.f_subscriptionfragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            /*
             * После нажатия на кнопку "Подписаться" записываем новые значения в конфигурационный файл.
             */
            case R.id.f_subscriptionfragment_menu_submit:
                SharedPreferences sp = getActivity().getSharedPreferences("subscription",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                SparseBooleanArray array = getListView().getCheckedItemPositions();
                for (int i = 0; i < array.size(); i++)
                {
                    int key = array.keyAt(i);
                    if (array.get(key))
                    {
                        editor.remove(Constants.typesOfOrders[key]);
                        editor.putBoolean(Constants.typesOfOrders[key],true);
                    }
                    else
                    {
                        editor.remove(Constants.typesOfOrders[key]);
                    }
                }
                editor.commit();
                for (int i = 0; i < array.size(); i++)
                {
                    int key = array.keyAt(i);
                    if (array.get(key))
                    {
                        editor.remove(Constants.typesOfOrders[key]);
                        editor.putBoolean(Constants.typesOfOrders[key],true);
                        Log.d(LOG_TAG,key + " : key. ");
                    }
                }
                break;
        }
        return false;
    }
}
