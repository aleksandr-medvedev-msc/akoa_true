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

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 23.01.13
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public class ChooseTypeOrderDialog extends DialogFragment implements DialogInterface.OnClickListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("Выберите заявку");
        String[] array = getResources().getStringArray(R.array.orders_russian_names);
        //String[] array = Constants.typesOfOrders;
        Log.d("DIALOG",array.length + " length " + array[0]);
        adb.setSingleChoiceItems(array,-1,this);
        return  adb.create();
    }
    @Override
    public void onClick(DialogInterface dialog,int i)
    {
        ListView listView = ((AlertDialog)dialog).getListView();
        Log.d("DIALOG", listView.getCount() + " listview");
        Fragment fragment = new F_NewOrderFragment(null,Constants.typesOfOrders[listView.getCheckedItemPosition()]);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(this);

        ft.addToBackStack("choose_order_type_dialog");
        ft.add(R.id.f_myorders_layout,fragment);
        ft.commit();
        this.dismiss();
    }
}