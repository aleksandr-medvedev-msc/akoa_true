package com.shema.aoka;

import android.app.Activity;
import android.support.v4.app.*;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 10.02.13
 * Time: 15:47
 * To change this template use File | Settings | File Templates.
 */
public class F_ListFragment extends SherlockFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    final String LOG_TAG = "F_LISTFRAGMENT_LOG";
    M_DB db;
    M_XmlParser parser;
    Cursor[] cursors;
    MergeCursor mergeCursor;
    private String[] from;
    private int[] to;
    private int[] lengths;
    private String dbTableName;
    private String orderId;
    SimpleCursorAdapter simpleCursorAdapter;
    DialogFragment newOrderDialog;

    public F_ListFragment(String[] from, int[] to, String dbTableName, String orderId)
    {
        super();
        this.from=from;
        this.to=to;
        this.dbTableName=dbTableName;
        this.orderId=orderId;      //может быть local_id or global_id. Это НЕ номер!

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (this.dbTableName.equals("My_orders"))
        {
            Log.d(LOG_TAG, " my orders");
            setHasOptionsMenu(true);
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        this.parser = ((AokaApplication)getActivity().getApplication()).getXmlParser();
        Log.d(LOG_TAG, " oncreate view");
        View v = inflater.inflate(R.layout.f_listfragment,null);
        lengths = new int[Constants.typesOfOrders.length];
        ListView lv = (ListView)v.findViewById(R.id.f_listfragment_lv);
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity(),R.layout.f_listfragment_lv_item,null,from,to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv.setAdapter(simpleCursorAdapter);
        final Fragment fragment = this;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                db.open();
                if (dbTableName.equals("Answers"))
                {
                    String answerIdNumber = simpleCursorAdapter.getCursor().getString(0);          //global_id of answer
                    Cursor answer_cursor = db.query(dbTableName,null,"global_id = ?",new String[]{answerIdNumber},null,null,null);
                    ArrayList<HashMap<String,Object>> answer_map = parser.getColumns(getActivity().getApplicationContext(),dbTableName);
                    ArrayList<String[]> answer_data = new ArrayList<String[]>();
                    if (answer_cursor.moveToFirst())
                    {
                        for (int i = 0; i < answer_cursor.getColumnCount();i++)
                        {
                            Log.d(LOG_TAG,answer_cursor.getCount() + " cursor count") ;
                            if (!answer_cursor.getColumnName(i).equals("global_id")&&!answer_cursor.getColumnName(i).equals("answer_id"))
                            {
                                Log.d(LOG_TAG,answer_cursor.getColumnCount()+ " columnt count at position : " + i);
                                String[] arr = new String[2];
                                arr[0] = answer_map.get(i).get("display_name").toString();
                                arr[0] = answer_cursor.getColumnName(i);                     //TODO: сделать русские имена колонок.
                                arr[1] = answer_cursor.getString(answer_cursor.getColumnIndex(arr[0]));
                                answer_data.add(arr);
                            }
                        }
                    }
                    Fragment fragment2 = new F_AnswerFragment(answer_data,answer_cursor.getString(0),null,answer_cursor.getString(answer_cursor.getColumnIndex("answer_id")));
                    answer_cursor.close();
                    FragmentTransaction ffft = getFragmentManager().beginTransaction();
                    ffft.hide(fragment);
                    ffft.add(R.id.f_myanswers_layout,fragment2);
                    ffft.addToBackStack("EditMyAnswer");
                    ffft.commit();
                    return;
                }

                String orderIdType = "global_id";
                String orderIdNumber = simpleCursorAdapter.getCursor().getString(0);//получаем глобал_ид заявки
                //Log.d("SEARCH_ANSWER",simpleCursorAdapter.getCursor().getString(0) + " order _id ");
                Cursor cursor;
                String my_orders_orderIdNumber = "0";
                if (dbTableName.equals("My_orders"))
                {
                    cursor = db.query(dbTableName,new String[]{"global_id,local_id,type"},orderId + " = ?", new String[]{orderIdNumber},null,null,null);
                    cursor.moveToFirst();
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex("global_id")))==0)
                    {
                        my_orders_orderIdNumber = cursor.getString(cursor.getColumnIndex("local_id"));
                        orderIdType = "local_id";
                    }
                    else
                    {
                        my_orders_orderIdNumber = cursor.getString(cursor.getColumnIndex("global_id"));
                    }
                }
                else
                {
                    cursor = db.query(dbTableName,new String[]{"global_id,type"},orderId + " = ?", new String[]{orderIdNumber},null,null,null);
                }
                Log.d(LOG_TAG,cursor.moveToFirst() + " move to first");
                Log.d(LOG_TAG,cursor.getColumnIndex("type") + " type of order");
                Log.d(LOG_TAG,orderId + " order ID");
                Log.d(LOG_TAG,my_orders_orderIdNumber + " my_orders_number. " + cursor.getColumnName(0) + " column name at 0");
                String orderType = cursor.getString(cursor.getColumnIndex("type"));
                cursor.close();
                /*
                 * Создаем фрагмент с кратким описанием заявки и кратким описанием всех ответов, если это наша заявка и мы можем просматривать к ней ответы
                 */

                if (dbTableName.equals("My_orders"))
                {
                    Log.d(LOG_TAG,"FFT + MY_ORDERS");
                    db.close();
                    FragmentTransaction fft = getFragmentManager().beginTransaction();
                    Fragment answerFragment = new F_AllAnswersFragment(orderType,my_orders_orderIdNumber,orderIdType);
                    fft.hide(fragment);
                    fft.add(R.id.f_myorders_layout,answerFragment);
                    fft.commit();
                }
                else
                {

                    Log.d(LOG_TAG, orderType + " order Type ");
                    ArrayList<HashMap<String,Object>> orderFieldColumns = parser.getColumns(getActivity(),orderType);

                String[] cols = new String[orderFieldColumns.size()-2]; //Все колонки, кроме локал_ид и глобал_ид
                for (int i = 2; i < orderFieldColumns.size(); i++)
                {
                    cols[i-2] = orderFieldColumns.get(i).get("name").toString();
                }
                cursor = db.query(orderType,cols,orderId + " = ?",new String[]{orderIdNumber},null,null,null);
                cursor.moveToFirst();
                ArrayList<String[]> data = new ArrayList<String[]>();
                for (int i = 0; i < cursor.getColumnCount(); i++)
                {
                    String[] strings;
                    if (i!=cursor.getColumnCount()-1)
                    {
                    strings = new String[]{orderFieldColumns.get(i+2).get("display_name").toString(),cursor.getString(i)};
                    }
                    else
                    {
                        strings = new String[]{"Количество ответов",cursor.getString(i)};
                    }
                    data.add(strings);
                }
                cursor.close();
                    if (db.isOpen())
                    {
                    db.close();
                    }
                    Fragment orderFragment = new F_OrderFragment(orderIdNumber,orderType,data,dbTableName);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.hide(fragment);

                ft.add(R.id.f_orders_layout,orderFragment);
                ft.addToBackStack("orderFragment");
                ft.commit();
                }
            }
        });
        getLoaderManager().initLoader(0,null,this);
        return v;
    }
    @Override public Loader<Cursor> onCreateLoader(int id,Bundle args)
    {

        db = ((AokaApplication)getActivity().getApplication()).getDb();
        if (!db.isOpen())
        {
            db.open();
        }
        return new SimpleCursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground()
            {
                Log.d("BACKGROUND", "loadInBackground()");
                ArrayList<String[]> columnsOfAllOrders = new ArrayList<String[]>();
                String[] columnsOfOrder;
                ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
                for (String t : Constants.typesOfOrders)
                {
                    list = parser.getColumns(getActivity(),t);
                    columnsOfOrder = new String[list.size()];
                    for (int i = 0; i < list.size(); i++)
                    {
                        columnsOfOrder[i]=list.get(i).get(Constants.keys[0]).toString();
                    }
                    columnsOfAllOrders.add(columnsOfOrder);
                }
                String[] querys = new String[Constants.typesOfOrders.length];
                for (int i = 0; i < Constants.typesOfOrders.length; i++)
                {
                    querys[i]=Constants.typesOfOrders[i] + " as OO inner join "+ dbTableName + " as OS on OO." + orderId + " = OS." + orderId;
                }
                ArrayList<String[]> columns = new ArrayList<String[]>();
                for (int i = 0; i < Constants.typesOfOrders.length; i++)
                {
                    columns.add(new String[]{"OO." + orderId + " as _id","OO." + columnsOfAllOrders.get(i)[2] + " as "
                            + from[0],"OS.date as " + from[1],"OO.number_of_answers as " + from[2]});
                }
                Log.d(LOG_TAG,columns.size() + " columns.size");
                cursors = new Cursor[Constants.typesOfOrders.length];
                for (int i = 0; i < Constants.typesOfOrders.length; i++)
                {
                    cursors[i] = db.query(querys[i],columns.get(i),null,null,null,null,null);
                    lengths[i] = cursors[i].getCount();
                    Log.d("SEARCH_ANSWER",lengths[i] + " lengths[" + i + "]");
                }
                /*for (int i = 0; i < Constants.typesOfOrders.length;i++)
                {
                    cursors[i].moveToFirst();
                    do {
                        Log.d("SEARCH_ANSWER",cursors[i].getString(cursors[i].getColumnIndex("_id")));
                    }   while (cursors[i].moveToNext());
                }  */
                ArrayList<Cursor> fCursors = new ArrayList<Cursor>();
                for (int i = 0; i < cursors.length; i++)
                {
                    if (cursors[i].moveToFirst())
                    {
                        if (cursors[i].getString(0)!=null)
                        {
                            fCursors.add(cursors[i]);
                        }
                    }
                }
                Cursor[] filtratedCursors = new Cursor[fCursors.size()];
                Log.d(LOG_TAG, filtratedCursors.length + "filtrated cursors.length");
                fCursors.toArray(filtratedCursors);
                if (filtratedCursors.length!=0)
                {
                    mergeCursor = new MergeCursor(filtratedCursors);
                    return mergeCursor;
                }
                else return null;
            }
        }  ;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        simpleCursorAdapter.swapCursor(cursor);
        if (db.isOpen())
        {
            db.close();
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
        if (db.isOpen())
        {
            db.close();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.f_myorders_neworder, menu);
        Log.d(LOG_TAG, " on create options menu");
        super.onCreateOptionsMenu(menu, inflater);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        Log.d(LOG_TAG," ON OPTIONS ITEM SELECTED");
        if (menuItem.getItemId()==R.id.f_myorders_neworderbutton)
        {
            newOrderDialog = new ChooseTypeOrderDialog();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.hide(this);
            newOrderDialog.show(getFragmentManager(),"1");
            ft.addToBackStack("LISTFRAGMENT_NEWORDER");
            Log.d(LOG_TAG,"on options item selected");
            ft.commit();
            super.onOptionsItemSelected(menuItem);
            return true;
        }
        else
        {
            Log.d(LOG_TAG,"on options item selected FALSE!!!");
            super.onOptionsItemSelected(menuItem);
            return false;
        }

    }
}