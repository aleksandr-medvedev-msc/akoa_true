package com.shema.aoka;


import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ListView;
import org.achartengine.GraphicalView;
import org.achartengine.chart.XYChart;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 23.01.13
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public class ChooseTimeframeDialog extends DialogFragment implements DialogInterface.OnClickListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private GraphicalView xyChart;
    private XYMultipleSeriesRenderer renderer;
    private List<double[]> range;
    public ChooseTimeframeDialog(GraphicalView xyChart, XYMultipleSeriesRenderer renderer,List<double[]> range)
    {
        this.xyChart = xyChart;
        this.renderer = renderer;
        this.range = range;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("Выберите диапазон дат");
        String[] array = getResources().getStringArray(R.array.timeframes);
        //String[] array = Constants.typesOfOrders;
        adb.setSingleChoiceItems(array,-1,this);
        return  adb.create();
    }
    @Override
    public void onClick(DialogInterface dialog,int i)
    {
        ListView listView = ((AlertDialog)dialog).getListView();
        Log.d("DIALOG", listView.getCount() + " listview");
        //renderer.setRange(range.get(listView.getCheckedItemPosition()));        //TODO: test this !!
        //Fragment fragment = new F_NewOrderFragment(null,Constants.typesOfOrders[listView.getCheckedItemPosition()]);
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.hide(this);
        //ft.addToBackStack("choose_order_type_dialog");
        //ft.add(R.id.f_myorders_layout,fragment);
        //ft.commit();
        this.dismiss();
    }
}