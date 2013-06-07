package com.shema.aoka;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 11.02.13
 * Time: 14:02
 * To change this template use File | Settings | File Templates.
 */
public class F_NewOrderFragment extends SherlockFragment {
    final String LOG_TAG = "F_NEWORDERFRAGMENT_LOG";
    private ArrayList<String[]> orderData;
    private String orderType;
    private M_XmlParser parser;
    Calendar date;
    String[] finalValues;
    public void setValues (String value,int position)
    {
        this.finalValues[position]=value;
    }
    public void setDate(Calendar calendar)
    {
        date=calendar;
    }
    public F_NewOrderFragment(ArrayList<String[]> orderData, String orderType)
    {
       // super();
        this.orderData= orderData;
        this.orderType = orderType;

    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_neworderfragment,null);

        this.parser = ((AokaApplication)getActivity().getApplication()).getXmlParser();
        LinearLayout layout = (LinearLayout)v.findViewById(R.id.f_neworderfragment_layout);
        ArrayList<HashMap<String,Object>> data = parser.orders_config_Parse(getActivity(),orderType,3);//Парсим field        //TODO: Переделать парсинг на нормальный!!!
        final String[] values = new String[data.size()];
        Log.d(LOG_TAG,values.length + " values.length");
        if (orderData!=null)
        {
            Log.d(LOG_TAG,orderData.size() + " orderdata.size");
            finalValues = new String[orderData.size()]; //iz-za number_of_answers.
            for (int i = 0; i < values.length; i++)
            {
                Log.d(LOG_TAG,orderData.get(i)[0] + " " + i + " ORDERDATA[i]");
                setValues(orderData.get(i)[1],i);
            }
//            setValues(String.valueOf(0),values.length);          //TODO: Число number_of_answers ! ne dolzhno bit ravno 0 !!!!
        }
        else
        {
             finalValues = new String[values.length];
        }
        for (String s : finalValues)
        {
            Log.d(LOG_TAG,s + " finalvalues");
        }
        Log.d(LOG_TAG,"onCreateVIew()");
        for (int i = 0; i < data.size(); i++)
        {

            final int position  = i;
            if (data.get(i).get("type").equals("text"))
            {
                Log.d(LOG_TAG, "text" + " " + i);
                View item = inflater.inflate(R.layout.a_registration_item,layout,false);
                TextView textView = (TextView)item.findViewById(R.id.a_registration_item_tv);
                EditText editText = (EditText)item.findViewById(R.id.a_registration_item_et);
                if (orderData!=null)
                {
                    editText.setText(orderData.get(i)[1]);
                }
                final CheckBox checkBox = (CheckBox)item.findViewById(R.id.a_registration_item_checkbox);
                checkBox.setEnabled(false);
                textView.setText(data.get(i).get("name").toString());
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().isEmpty())
                        {
                            checkBox.setChecked(true);
                            values[position] = s.toString();
                            setValues(values[position],position);
                        }
                        else
                        {
                            checkBox.setChecked(false);
                            values[position]=s.toString();
                            setValues(values[position],position);
                        }
                    }
                });
                if (i%2==0)
                {
                    item.setBackgroundColor(getResources().getColor(R.color.a_registration_item_first));
                }
                else
                {
                    item.setBackgroundColor(getResources().getColor(R.color.a_registration_item_second));
                }
                layout.addView(item);
            }
            if (data.get(i).get("type").equals("email"))
            {
                View item = inflater.inflate(R.layout.a_registration_item,layout,false);
                TextView textView = (TextView)item.findViewById(R.id.a_registration_item_tv);
                EditText editText = (EditText)item.findViewById(R.id.a_registration_item_et);
                final CheckBox checkBox = (CheckBox)item.findViewById(R.id.a_registration_item_checkbox);
                checkBox.setEnabled(false);
                textView.setText(data.get(i).get("name").toString());
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().isEmpty())
                        {
                            checkBox.setChecked(true);
                            values[position] = s.toString();
                            setValues(values[position],position);
                        }
                        else
                        {
                            checkBox.setChecked(false);
                            values[position]=s.toString();
                            setValues(values[position],position);
                        }
                    }
                });
                if (i%2==0)
                {
                    item.setBackgroundColor(getResources().getColor(R.color.a_registration_item_first));
                }
                else
                {
                    item.setBackgroundColor(getResources().getColor(R.color.a_registration_item_second));
                }
                layout.addView(item);
            }
            if (data.get(i).get("type").equals("date"))
            {
                Log.d(LOG_TAG, "date" + " " + i);
                date = Calendar.getInstance();
                View item = inflater.inflate(R.layout.f_answerfragment_item_date,layout,false);
                TextView title = (TextView)item.findViewById(R.id.f_answerfragment_item_date_tv);
                final CheckBox checkBox = (CheckBox)item.findViewById(R.id.f_answerfragment_item_date_cb);
                checkBox.setEnabled(false);
                final EditText editText = (EditText)item.findViewById(R.id.f_answerfragment_item_date_et);
                Log.d(LOG_TAG,item.toString() + "item");
                title.setText(data.get(i).get("name").toString());
                Button dateButton = (Button)item.findViewById(R.id.f_answerfragment_datebutton);
                final DateDialogFragment ddf = DateDialogFragment.newInstance(getActivity(), R.string.f_answerfragment_datebutton, date);
                checkBox.setEnabled(false);
                ddf.setDateDialogFragmentListener(new DateDialogFragment.DateDialogFragmentListener() {
                    @Override
                    public void dateDialogFragmentDateSet(Calendar date) {
                        setDate(date);
                        String str = date.get(Calendar.DAY_OF_MONTH) + "." + (date.get(Calendar.MONTH)+1)
                                + "." + date.get(Calendar.YEAR);
                        editText.setText(str);
                        values[position] = str;
                        setValues(values[position],position);
                        Log.d(LOG_TAG,date.toString() + " date");
                        checkBox.setChecked(true);
                    }
                });
                dateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ddf.show(getFragmentManager(), "date picker dialog fragment");
                    }
                });
                if (i%2==0)
                {
                    item.setBackgroundColor(getResources().getColor(R.color.a_registration_item_first));
                }
                else
                {
                    item.setBackgroundColor(getResources().getColor(R.color.a_registration_item_second));
                }
                layout.addView(item);
            }
            if (data.get(i).get("type").equals("spinner"))
            {
                final int values_position = i;
                ArrayList<HashMap<String,Object>> spinnerData = parser.orders_config_Parse(getActivity(),orderType,4);
                final String[] spinnerStrings = new String[spinnerData.size()];
                for (int j = 0; j < spinnerStrings.length; j++)
                {
                    spinnerStrings[j]=spinnerData.get(j).get("name").toString();
                }
                View item = inflater.inflate(R.layout.a_registration_item_with_spinner,layout,false);
                TextView textView = (TextView)item.findViewById(R.id.a_registration_item_with_spinner_tv);
                textView.setText(data.get(i).get("name").toString());
                Spinner spinner = (Spinner)item.findViewById(R.id.a_registration_spinner);
                final CheckBox checkBox = (CheckBox)item.findViewById(R.id.a_registration_item_with_spinner_checkbox);
                checkBox.setEnabled(false);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerStrings);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        values[values_position]=spinnerStrings[position];
                        checkBox.setChecked(true);
                        setValues(values[position],position);
                    }//TODO: Сделать правильное удаление заявки по правильному ИД при редактировании.
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                })     ;
                layout.addView(item);
            }
        }
        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.f_neworderfragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        if (menuItem.getItemId()==R.id.f_neworderfragment_menu_sendbutton)                //Проверить валидность и отправить
        {
            boolean flag = true;
            int[] numbers = new int[finalValues.length];
            Arrays.fill(numbers,1);
            for (int i = 0; i < finalValues.length; i++)
            {
                Log.d(LOG_TAG,finalValues[i] + " i" + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!: " + i);
                if (finalValues[i]==null)
                {
                    flag=false;
                    numbers[i]=0;
                }
                else
                {
                    if (finalValues[i].isEmpty())
                    {
                        flag=false;
                        numbers[i]=0;
                    }
                }
            }
            if (flag)
            {
                M_DB db = ((AokaApplication)getActivity().getApplication()).getDb();
                ArrayList<HashMap<String,Object>> columnsList = parser.getColumns(getActivity(),orderType);
                String[] columns  = new String[columnsList.size()];
                String[] values = new String[columns.length];
                Log.d(LOG_TAG,finalValues.length + " final values length");
                Log.d(LOG_TAG,columns.length + " cols length");
                int k=0;
                for (int i = 0; i < columns.length; i++)
                {
                    columns[i] = columnsList.get(i).get("name").toString();
                    Log.d(LOG_TAG,columns[i] + " columns [ i ] " + i);
                    if (!columnsList.get(i).get("display_name").toString().isEmpty())
                    {
                        values[i] = finalValues[k];
                        k++;
                    }
                }
                values[0]=String.valueOf(0);//global_id у новой заявки всегда 0.
                db.open();
                Cursor cursor = db.query("My_orders",new String[]{"max(local_id) as maximum"},null,null,null,null,null);

                if (cursor.moveToFirst())
                {
                    Log.d(LOG_TAG,cursor.getString(0) + " : MAXIMUM");
                    if (cursor.getString(0)==null)
                    {
                        values[1]=String.valueOf(1);
                    }
                    else
                    {
                        values[1]=String.valueOf(Integer.parseInt(cursor.getString(0)) + 1);
                    }
                }
                else
                {
                    values[1]=String.valueOf(1);
                }
                values[values.length-1]=String.valueOf(0);    //number_of_answers
                String localId = String.valueOf(Integer.parseInt(values[1])-1);
                Log.d(LOG_TAG,columns[0] + "   " + values[1]);
                for (int i = 0; i < columns.length; i++)
                {
                    Log.d(LOG_TAG, columns[i] + "  : " + values[i]);
                }
                if (orderData!=null)
                {
                    db.delete(orderType,"local_id = ?",new String[]{localId});
                    db.delete("My_orders","local_id = ?",new String[]{localId});
                }
                db.addRec(orderType,columns,values);
                columnsList=parser.getColumns(getActivity(),"My_orders");
                columns=new String[columnsList.size()];
                values = new String[columns.length];
                for (int i = 0; i < columns.length;i++)
                {
                    columns[i] = columnsList.get(i).get("name").toString();
                    if (columnsList.get(i).get("name").toString().equals("global_id"))
                    {
                        values[i]=String.valueOf(0);
                    }
                    if (columnsList.get(i).get("name").toString().equals("local_id"))
                    {
                        values[i]=String.valueOf(Integer.parseInt(localId)+1);
                    }
                    if (columnsList.get(i).get("name").toString().equals("type"))
                    {
                        values[i]=orderType;
                    }
                }
                db.addRec("My_orders",columns,values);
                db.close();
                Toast.makeText(getActivity(),"Заявка добавлена",Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.hide(this);
                ft.addToBackStack("LISTFRAGMENT_NEWORDER");
                ft.commit();
                //getActivity().getActionBar().selectTab(getActivity().getActionBar().getTabAt(0));
                return true;
            }
            else
            {
                Toast.makeText(getActivity(),R.string.a_registration_wrong_fill,Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        else return false;
    }
}