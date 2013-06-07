package com.shema.aoka;

import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragment;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 2/15/13
 * Time: 1:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class F_IndexFragment extends SherlockFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    SimpleCursorAdapter simpleCursorAdapter;
    M_DB db;
    final int[] to = {R.id.f_listfragment_index_name_tv,R.id.f_listfragment_index_value_tv};
    final String[] columns = {"_id","index_name","value"};
    final String dbTableName = "Indexes";
    GraphicalView xyChart;
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    private TimeSeries mCurrentSeries;

    private XYSeriesRenderer mCurrentRenderer;

    private void initChart() {
        mCurrentSeries = new TimeSeries("");
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(mCurrentRenderer);
    }

    /*
    private void addSampleData() {
        mCurrentSeries.add(1, 2);
        mCurrentSeries.add(2, 3);
        mCurrentSeries.add(3, 2);
        mCurrentSeries.add(4, 5);
        mCurrentSeries.add(5, 4);
    }                      */
    @Override
    public void onResume()
    {
        super.onResume();
        //LinearLayout layout = (LinearLayout)getView().findViewById(R.id.f_graphiclayout);
        if (xyChart==null)
        {
            mRenderer.setAxisTitleTextSize(36.0f);
            mRenderer.setLegendTextSize(36.0f);
            mRenderer.setGridColor(R.color.menu_text);
            mRenderer.setShowGrid(true);
            mRenderer.setMarginsColor(R.color.menu_header);
            mRenderer.setBackgroundColor(R.color.menu_header);
            //mRenderer.setXTitle("xTitle");
            //mRenderer.setYTitle("Дата");
            mRenderer.setLabelsTextSize(32.0f);
            mRenderer.setMargins(new int[]{56, 56,96, 56});
            mRenderer.setXLabelsAngle(0.5f);
            initChart();
            //addSampleData();
            xyChart = ChartFactory.getTimeChartView(getActivity(),mDataset,mRenderer,"dd/MM/yyyy");

            //xyChart= ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, 0.3f);
            xyChart.setBackgroundColor(R.color.menu_header);
            //layout.addView(xyChart);
            //layout.setBackgroundColor(R.color.menu_header);
        } else {
            xyChart.repaint();
        }
    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        Log.d("VIEW_PAGER","START!!!" + "index fragment");
        View v = inflater.inflate(R.layout.f_indexfragment,null);
        v.setBackgroundColor(Color.WHITE);
        getLoaderManager().initLoader(0,null,this);
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity(),R.layout.f_listfragment_index_item,null,columns,to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ListView lv = (ListView)v.findViewById(R.id.f_indexfragment_lv);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        LinearLayout lvLayout = (LinearLayout)v.findViewById(R.id.f_indexfragment_lv_layout);
        lvLayout.setMinimumHeight(metrics.heightPixels/3);
        lv.setAdapter(simpleCursorAdapter);
        if(!db.isOpen())
        {
            db.open();
        }

        Cursor cursor4eg = db.getAllData("Indexes");
        boolean a = cursor4eg.moveToFirst();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] cols = {"date","value"};
                ArrayList<long[]> indexValues = new ArrayList<long[]>();

                Cursor indexValuesCursor = db.query(dbTableName,cols,"index_name = ?",new String[]{simpleCursorAdapter.getCursor().getString(1)},null,null,cols[0]);
                if (indexValuesCursor.moveToFirst())
                {
                    do {
                        long[] pair = {indexValuesCursor.getLong(0),indexValuesCursor.getLong(1)};
                        indexValues.add(pair);
                    }   while (indexValuesCursor.moveToNext());
                }
                LinearLayout layout = (LinearLayout)getView().findViewById(R.id.f_graphiclayout);
                mCurrentSeries.clear();
                double xMaxValue = Double.MIN_VALUE;
                double xMinValue = Double.MAX_VALUE;
                for (long[] kvPair : indexValues)
                {
                    mCurrentSeries.add(new Date(kvPair[0]*1000),kvPair[1]);
                    if (xMaxValue < kvPair[0]*1000)
                    {
                        xMaxValue = kvPair[0]*1000;
                    }
                    if (xMinValue > kvPair[0]*1000)
                    {
                        xMinValue = kvPair[0]*1000;
                    }
                }
                mRenderer.setScale(2.0f);
                mRenderer.setZoomRate(5.0f);
                mRenderer.setXLabelsAngle(45);
                //mRenderer.setStartAngle(90);
                //mRenderer.initAxesRangeForScale(2);
         //       mRenderer.setXAxisMax(xMaxValue,2);
           //     mRenderer.setXAxisMin(xMinValue,2);
                GregorianCalendar calendar = new GregorianCalendar();
                  //      mRenderer.setShowLabels(false);
                for (long[] kvPair : indexValues)
                {
                    Date time = new Date(kvPair[0]*1000);
                    calendar.setTime(time);
                    String str = "" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR);
                //    mRenderer.addXTextLabel(kvPair[0],str);
                }
                //mRenderer.setShowLegend(false);
                //mRenderer.setShowLabels(false);
                if (layout.getChildCount()==0)
                {
                    layout.addView(xyChart);
                }
                mRenderer.setScale(100.0f);
                mCurrentSeries.setTitle(simpleCursorAdapter.getCursor().getString(1));
                xyChart.setBackgroundColor(Color.WHITE);
                xyChart.repaint();

            }
        });
        return v;
    }

    @Override public Loader<Cursor> onCreateLoader(int id,Bundle args)
    {
        db = ((AokaApplication)getActivity().getApplication()).getDb();
        return new SimpleCursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground()
            {
                //db.open();
                Log.d("BACKGROUND", "loadInBackground()");
                Cursor maxCursor = db.query(dbTableName,new String[]{"max(date)"},null,null,null,null,null);
                maxCursor.moveToFirst();
                String selection = "date = ?";
                String[] selectionArgs ={maxCursor.getString(0)};
                Cursor cursor = db.query(dbTableName,columns,selection,selectionArgs,null,null,null);
                return cursor;
            }
        };
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        simpleCursorAdapter.swapCursor(cursor);
        //db.close();
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.f_indexfragment_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        if (menuItem.getItemId()==R.id.f_indexfragment_menu_timeframe)
        {
            ChooseTimeframeDialog dialog = new ChooseTimeframeDialog(xyChart,mRenderer,null);
            dialog.show(getFragmentManager(),"");
        }
        return true;
    }

    }