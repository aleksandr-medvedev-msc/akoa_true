package com.shema.aoka;

import android.app.Activity;


import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 3/13/13
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class F_NewsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    M_DB db;
    SimpleCursorAdapter adapter;
    ListView lv;
    private final String LOG_TAG = "F_NewsListFragment_Log";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.d("VIEW_PAGER"," F_NewsListFragment is loaded()");
        db = ((AokaApplication)getActivity().getApplication()).getDb();

        //db.open();
        View v = inflater.inflate(R.layout.f_newslistfragment,null);
        lv = (ListView)v.findViewById(R.id.f_newslistfragment_lv);
        getLoaderManager().initLoader(0,null,this);
        adapter = new SimpleCursorAdapter(getActivity(),R.layout.f_newslistfragment_item,null,new String[]{"photo","title"},new int[]{R.id.f_newslistfragment_image,
        R.id.f_newslistfragment_item_title}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId()==R.id.f_newslistfragment_image)
                {
                    ((ImageView)view).setImageBitmap(BitmapFactory.decodeByteArray(cursor.getBlob(columnIndex),0,cursor.getBlob(columnIndex).length));
                    return true;
                }
                else
                {
                    ((TextView)view).setText(cursor.getString(columnIndex));
                    return true;
                }
                 //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        final Fragment thisFragment = this;
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                db.open();
                String text = "";
                String date = "";
                String title;
                byte [] image;

                Cursor dateAndTextCursor = db.query("News",new String[]{"date","text"},"novel_id = ?",new String[]{adapter.getCursor().getString(0)},null,null,null);
                if (dateAndTextCursor!=null&&dateAndTextCursor.moveToFirst())
                {
                    date = dateAndTextCursor.getString(0);
                    text = dateAndTextCursor.getString(1);
                }
                title = adapter.getCursor().getString(adapter.getCursor().getColumnIndex("title"));
                image = adapter.getCursor().getBlob(adapter.getCursor().getColumnIndex("photo"));
                db.close();
                Fragment novelFragment = new F_NewsNovelFragment(image,title,date,text);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.hide(thisFragment);
                ft.add(R.id.f_news_layout,novelFragment);
                ft.addToBackStack("F_NewsNovelFragment");
                ft.commit();
                Log.d(LOG_TAG, adapter.getCursor().getPosition() + " : cursor position");
                Log.d(LOG_TAG, i + " : i");
            }
        });
        return v;

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new SimpleCursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground() {
                String tableName = "News as NS inner join News_Photos as NP on NS.novel_id = NP.novel_id";
                String[] columns = {"NS.novel_id as _id","NS.title as title","NP.body_photo as photo"};
                return db.query(tableName,columns,null,null,null,null,null);
            }
        }      ;
    }
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        adapter.swapCursor(data);
        //db.close();
    }
    public void onLoaderReset(Loader<Cursor> loader)
    {
        adapter.swapCursor(null);
        //db.close();
    }
}
