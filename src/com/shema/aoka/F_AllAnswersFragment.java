package com.shema.aoka;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 2/20/13
 * Time: 4:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class F_AllAnswersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    final String[] groupFrom = {"title","number","date"};
    private M_DB db;
    final private String LOG_TAG = "ALLANSWERSFRAGMENT_LOG";
    private String orderType;              //Bunkering_order or Tank_order and so on;
    private String orderId;     //Global_id.
    private SimpleCursorAdapter adapter;
    private String orderIdName;
    private M_XmlParser parser;
    public F_AllAnswersFragment(String orderType, String orderId, String orderIdName)
    {
        this.orderIdName = orderIdName;              //if of order
        this.orderType = orderType;
        this.orderId = orderId;     //number of order!!.

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        this.parser = ((AokaApplication)getActivity().getApplication()).getXmlParser();
        Log.d(LOG_TAG,"ALL ANSWER STARTED");
        final int[] groupTo = {R.id.f_listfragment_lv_item_title,R.id.f_listfragment_lv_item_number,R.id.f_listfragment_lv_item_date};
        View v = inflater.inflate(R.layout.f_allanswersfragment,null);
        ListView lv = (ListView)v.findViewById(R.id.f_allanswersfragment_lv);
        adapter = new SimpleCursorAdapter(getActivity(),R.layout.f_listfragment_lv_item,null,
                groupFrom,groupTo,0);
        lv.setAdapter(adapter);
        final Fragment fragment = this;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                db.open();
                if (position != 0)
                {
                    Cursor cursor = adapter.getCursor();
                    String answerId = cursor.getString(cursor.getColumnIndex("_id"));
                    Cursor newCursor = db.query("Answers",null,"answer_id = ?",new String[]{answerId},null,null,null);
                    ArrayList<String[]> answerData = new ArrayList<String[]>();
                    ArrayList<HashMap<String,Object>> map = parser.getColumns(getActivity(),"Answers");
                    newCursor.moveToFirst();
                    int j = 0;
                    for (int i = 0; i < newCursor.getColumnCount(); i++)
                    {
                        if (!newCursor.getColumnName(i).equals(answerId)&&!newCursor.getColumnName(i).equals("global_id"))
                        {
                            String[] data = new String[2];
                            data[1] = newCursor.getString(i);
                            data[0] = map.get(j).get("name").toString();
                            answerData.add(data);
                            j++;
                        }
                    }
                    newCursor.close();
                    db.close();
                    Fragment orderFragment = new F_OrderFragment(orderId,orderType,answerData,"Answers");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.hide(fragment);
                    ft.add(R.id.f_myorders_layout,orderFragment);
                    ft.addToBackStack("SINGLEANSWERFRAGMENT");
                    ft.commit();
                }
                else
                {
                    ArrayList<HashMap<String,Object>> orderFieldColumns = parser.getColumns(getActivity(),orderType);
                    String[] cols = new String[orderFieldColumns.size()-3]; //Все колонки, кроме локал_ид, глобал_ид  and number_of_answers

                    for (int i = 2; i < orderFieldColumns.size()-1; i++)
                    {
                        cols[i-2] = orderFieldColumns.get(i).get("name").toString();
                    }
                    Cursor cursor = db.query(orderType,cols,orderIdName + " = ?",new String[]{orderId},null,null,null);
                    cursor.moveToFirst();
                    ArrayList<String[]> data = new ArrayList<String[]>();
                    for (int i = 0; i < cursor.getColumnCount(); i++)
                    {
                        String[] strings = {orderFieldColumns.get(i+2).get("display_name").toString(),cursor.getString(i)};
                        data.add(strings);
                    }
                    cursor.close();
                    db.close();
                    Fragment orderFragment = new F_OrderFragment(orderId,orderType,data,"My_orders"); // TODO:// проверить последний аргумент
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.hide(fragment);
                    ft.add(R.id.f_myorders_layout,orderFragment);
                    ft.addToBackStack("orderFragment");
                    ft.commit();
                }
            }
        });
        lv.getCount();
        adapter.getCount();
        Loader<Cursor> loader = getLoaderManager().getLoader(0);
        if (loader!=null &&!loader.isReset())
        {
            getLoaderManager().restartLoader(0,null,this);
        }
        else
        {
            getLoaderManager().initLoader(0,null,this);
        }

        return v;
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        db = ((AokaApplication)getActivity().getApplication()).getDb();
        if (!db.isOpen())
        {
            db.open();
        }
        return new SimpleCursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground() {
                Log.d(LOG_TAG,orderIdName + " :orderIdName");
                Log.d(LOG_TAG,"load in background");
                ArrayList<HashMap<String,Object>> map = parser.getColumns(getActivity(),orderType);
                Log.d(LOG_TAG,orderType + " :ordertype");
                Log.d(LOG_TAG,orderId + " :orderId");
                String dbTableName = orderType + " as OO inner join " + "My_orders as OS on OO." + orderIdName + " = OS." + orderIdName;
                String[] columns = {"OO." + orderIdName + " as _id","OO." + map.get(2).get("name").toString() + " as " + groupFrom[0],
                            "OO.number_of_answers as " + groupFrom[1],"OS.date as " + groupFrom[2]};
                String selection = "OO." + orderIdName + " = ?";
                String[] selectionArgs = {orderId};
                Cursor cursor4eg = db.getAllData("My_orders");
                cursor4eg.moveToFirst();
                for (int i = 0; i < cursor4eg.getColumnCount(); i++)
                {
                    Log.d(LOG_TAG, cursor4eg.getString(i) + " STRING AT : " + i);
                }
                cursor4eg.close();
                Log.d(LOG_TAG,cursor4eg.getCount() + " my_orders alldata count");
                Cursor cursor = db.query(dbTableName,columns,selection,selectionArgs,null,null,null);
                if (orderIdName.equals("local_id"))
                {
                    return cursor;
                }
                dbTableName = "Answers";
                map = parser.getColumns(getActivity(),dbTableName);
                columns = new String[]{"answer_id as _id","comment as " + groupFrom[0],"price as " + groupFrom[1],map.get(map.size()-2).get("name").toString() + " as " + groupFrom[2]};
                selection = "global_id = ?";
                Cursor answersCursor = db.query(dbTableName,columns,selection,selectionArgs,null,null,null);
                Log.d(LOG_TAG,answersCursor.getCount() + " A_C_COUNT. " + cursor.getCount() + " C_COUNT.");
                MergeCursor mergeCursor = new MergeCursor(new Cursor[]{cursor,answersCursor});
                return mergeCursor;
            }
        }       ;
    }
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        Log.d(LOG_TAG, " on load finished");
        Log.d(LOG_TAG, data.getCount() + " data count");
        Log.d(LOG_TAG, data.getColumnCount() + " data column count");
        adapter.swapCursor(data);
        Log.d(LOG_TAG, adapter.getCount() + "adapter.getcount");
        if (db.isOpen())
        {
            db.close();
        }
    }
    public void onLoaderReset(Loader<Cursor> loader)
    {
        adapter.swapCursor(null);
        if (db.isOpen())
        {
            db.close();
        }
    }
}
