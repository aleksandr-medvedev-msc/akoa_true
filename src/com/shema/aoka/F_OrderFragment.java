package com.shema.aoka;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 10.02.13
 * Time: 17:00
 * To change this template use File | Settings | File Templates.
 */
public class F_OrderFragment extends SherlockFragment {
    private String orderId;
    private String orderType;
    private String dbTableName;
    private ArrayList<String[]> data;
    final String[] from = {"title","text"};
    public F_OrderFragment(String orderId, String orderType,ArrayList<String[]> data,String dbTableName)
    {
        this.orderId=orderId;                         //ID of order (number)
        this.orderType=orderType;
        this.data=data;                                    //Названия полей и значения полей, очищенные от *_id
        Log.d("ORDER__________FRAGMENT",orderId);
        this.dbTableName = dbTableName;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        if (!dbTableName.equals("Answers"))
        {
            setHasOptionsMenu(true);
        }
        int[] to;
        View v;
        ListView lv;
        if (dbTableName.equals("My_orders"))
        {
            v = inflater.inflate(R.layout.f_orderfragment,null);
            lv = (ListView)v.findViewById(R.id.f_orderfragment_lv);
            to = new int[]{R.id.f_orderfragment_lv_item_title,R.id.f_orderfragment_lv_item_text};
        }
        else
        {
            to =new int[]{R.id.f_orderfragment_lv_item_title,R.id.f_orderfragment_lv_item_text};
            v = inflater.inflate(R.layout.f_orderfragment,null);
            lv = (ListView)v.findViewById(R.id.f_orderfragment_lv);
        }
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < data.size(); i++)
        {
            HashMap<String,Object> map = new HashMap<String, Object>();
            map.put(from[0],data.get(i)[0]);
            map.put(from[1],data.get(i)[1]);
            list.add(map);
        }
        SimpleAdapter sAdapter = new SimpleAdapter(getActivity(),list,R.layout.f_orderfragment_lv_item,from,to);
        lv.setAdapter(sAdapter);
        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (dbTableName.equals("My_orders"))
        {
             inflater.inflate(R.menu.f_orderfragment_myorders_menu,menu);
        }
        if (dbTableName.equals("Orders"))
        {
            inflater.inflate(R.menu.f_orderfragment_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        if (menuItem.getItemId()==R.id.f_orderfragment_menu_answer)
        {
            Fragment fragment = new F_AnswerFragment(null,orderId,orderType,String.valueOf(0));
            Log.d("LOG_TAGG",orderId + " : orderId");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.hide(this);
            ft.add(R.id.f_orders_layout,fragment);
            ft.addToBackStack("F_ANSWERFRAGMENT");
            ft.commit();
            return true;
        }
        if (menuItem.getItemId()==R.id.f_orderfragment_myorders_menu_edit)
        {
            Fragment fragment = new F_NewOrderFragment(data,orderType);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.hide(this);
            ft.add(R.id.f_myorders_layout,fragment);
            ft.addToBackStack("F_ANSWERFRAGMENT_EDIT");
            ft.commit();
            return true;
        }
        return false;
    }

    public void onDetach() {
        super.onDetach();
        Log.d("ORDER FRAGMENT!!", "ORDER onDetach");
    }
}