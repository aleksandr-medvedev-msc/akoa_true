package com.shema.aoka;

import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
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
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 10.02.13
 * Time: 19:54
 * To change this template use File | Settings | File Templates.
 */
public class F_AnswerFragment extends SherlockFragment {
    final String LOG_TAG = "F_ANSWERSFRAGMENT_LOG";
    Calendar date;
    private ArrayList<String[]> data;
    final int REQUEST_SAVE = 1;
    final int REQUEST_LOAD = 2;
    String filePath;
    TextView textViewFile;
    String orderId;
    String orderType;
    String asnwerId;
    String[] finalValues;
    M_XmlParser parser;
    public void setDate(Calendar calendar)
    {
        date=calendar;
    }
    public F_AnswerFragment(ArrayList<String[]> data, String orderId, String orderType, String answerId)
    {
        super();
        this.data=data;
        this.orderId = orderId;                       //global_id of order!
        this.orderType = orderType;
        this.asnwerId = answerId;

        if (data!=null)
        {
            for (int i = 0; i < data.size(); i++)
            {
                if (data.get(i)[0].equals("file"))
                {
                    filePath = data.get(i)[1];
                    break;
                }
            }
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        this.parser = ((AokaApplication)getActivity().getApplication()).getXmlParser();
        View v = inflater.inflate(R.layout.f_answerfragment,null);
        setHasOptionsMenu(true);
        LinearLayout layout = (LinearLayout)v.findViewById(R.id.f_answerfragment_layout);
        ArrayList<HashMap<String,Object>> answerColumns = parser.answers_config_Parse(getActivity());
        String[] keys = new String[answerColumns.get(0).size()];
        answerColumns.get(0).keySet().toArray(keys);
        final String[] values = new String[answerColumns.size()];
        Log.d(LOG_TAG,answerColumns.size() + " SIZE");
        date = Calendar.getInstance();
        for (int i = 0; i < answerColumns.size();i++)
        {
            final int position = i;
            Log.d(LOG_TAG,answerColumns.get(i).toString());
            if (answerColumns.get(i).get("name").equals("date"))
            {
                View item = inflater.inflate(R.layout.f_answerfragment_item_date,layout,false);
                TextView title = (TextView)item.findViewById(R.id.f_answerfragment_item_date_tv);
                final CheckBox checkBox = (CheckBox)item.findViewById(R.id.f_answerfragment_item_date_cb);
                final EditText editText = (EditText)item.findViewById(R.id.f_answerfragment_item_date_et);
                if (data!=null&&data.get(i).length>=2)
                {
                    editText.setText(data.get(i)[1]);
                    values[position]=data.get(i)[1];
                }
                Log.d(LOG_TAG,item.toString() + "item");
                title.setText(answerColumns.get(i).get("display_name").toString());
                Button dateButton = (Button)item.findViewById(R.id.f_answerfragment_datebutton);
                final DateDialogFragment ddf = DateDialogFragment.newInstance(getActivity(), R.string.f_answerfragment_datebutton, date);
                checkBox.setEnabled(false);
                ddf.setDateDialogFragmentListener(new DateDialogFragment.DateDialogFragmentListener() {
                    @Override
                    public void dateDialogFragmentDateSet(Calendar date) {
                        setDate(date);
                        String str = date.get(Calendar.DAY_OF_MONTH) + "." + (date.get(Calendar.MONTH) + 1)
                                + "." + date.get(Calendar.YEAR);
                        editText.setText(str);
                        values[position] = str;
                        Log.d(LOG_TAG, date.toString() + " date");
                        checkBox.setChecked(true);
                    }
                });
                dateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ddf.show(getFragmentManager(), "date picker dialog fragment");
                    }
                });
                layout.addView(item);
                if (i%2==0)
                {
                    item.setBackgroundColor(getResources().getColor(R.color.a_registration_item_first));
                }
                else
                {
                    item.setBackgroundColor(getResources().getColor(R.color.a_registration_item_second));
                }
                continue;
            }
            if (answerColumns.get(i).get("name").equals("comment"))
            {
                View item = inflater.inflate(R.layout.f_answerfragment_item_comment,layout,false);
                TextView title = (TextView)item.findViewById(R.id.f_orderfragment_item_comment_tw);
                title.setText(answerColumns.get(i).get("display_name").toString());
                EditText editText = (EditText)item.findViewById(R.id.f_orderfragment_item_comment_et);
                if (data!=null&&data.get(i).length>=2)
                {
                    editText.setText(data.get(i)[1]);
                    values[position]=data.get(i)[1];
                }
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        values[position]=s.toString();
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
                continue;
            }
            View item = inflater.inflate(R.layout.a_registration_item,layout,false);
            TextView title = (TextView)item.findViewById(R.id.a_registration_item_tv);
            EditText editText = (EditText)item.findViewById(R.id.a_registration_item_et);
            if (data!=null&&data.get(i).length>=2)
            {
                editText.setText(data.get(i)[1]);
                values[position]=data.get(i)[1];
            }
            final CheckBox checkBox = (CheckBox)item.findViewById(R.id.a_registration_item_checkbox);
            title.setText(answerColumns.get(i).get("display_name").toString());

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    values[position]=s.toString();
                    checkBox.setChecked(true);
                    checkBox.setEnabled(false);
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
        View item = inflater.inflate(R.layout.f_answerfragment_file,layout,false);
        textViewFile = (TextView)item.findViewById(R.id.f_answerfragment_file_tv);
        Button fileAttachButton = (Button)item.findViewById(R.id.f_answerfragment_file_button);
        fileAttachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), FileDialog.class);
                intent.putExtra(FileDialog.START_PATH, "/sdcard");
                //can user select directories or not
                intent.putExtra(FileDialog.CAN_SELECT_DIR, true);
                //alternatively you can set file filter
                //intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "png" });
                startActivityForResult(intent, REQUEST_SAVE);

            }
        });
        layout.addView(item);
        finalValues=values;
        return v;
    }
    public synchronized void onActivityResult(final int requestCode,
                                              int resultCode, final Intent data) {
        if (resultCode == FragmentActivity.RESULT_OK) {
            if (requestCode == REQUEST_SAVE) {
                System.out.println("Saving...");
            } else if (requestCode == REQUEST_LOAD) {
                System.out.println("Loading...");
            }
            filePath = data.getStringExtra(FileDialog.RESULT_PATH);
            textViewFile.setText(filePath);
            Log.d("FILEDIALOG",filePath);
        } else if (resultCode == FragmentActivity.RESULT_CANCELED) {
            Toast.makeText(getActivity(),"file not selected",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.f_answerfragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        if (menuItem.getItemId()==R.id.f_answerfragment_menu_send)
        {
            boolean flag = true;
            for (int i = 0; i < finalValues.length; i++)
            {
                if (finalValues[i].isEmpty())
                {
                    flag=false;
                }
            }
            if (filePath==null||filePath.equals("/sdcard")||filePath.isEmpty())
            {
                flag=false;
            }
            if (flag)
            {
            ArrayList<HashMap<String,Object>> list = parser.getColumns(getActivity(),"Answers");
            M_DB db = ((AokaApplication)getActivity().getApplication()).getDb();
            if (!db.isOpen())
            {
                db.open();
            }
            ContentValues cv = new ContentValues();
            for (int i = 1; i  < finalValues.length+1;i++)
            {
                cv.put(list.get(i).get(Constants.keys[0]).toString(),finalValues[i-1]);
            }
                cv.put(list.get(0).get(Constants.keys[0]).toString(),orderId);
                Log.d(LOG_TAG,orderId + " :orderId. " + list.get(0).get(Constants.keys[0]).toString());
                cv.put(list.get(finalValues.length+1).get(Constants.keys[0]).toString(),filePath);
                if (data!=null)
                {
                    cv.put("answer_id",asnwerId);
                }
                else
                {
                    Cursor cursor = db.query("Answers",new String[]{"max(answer_id) as maximum"},null,null,null,null,null);

                    if (cursor.moveToFirst()&&cursor.getString(0)!=null)
                    {
                        cv.put("answer_id",String.valueOf(Integer.parseInt(cursor.getString(0))+1));
                    }
                    else
                    {
                        cv.put("answer_id",String.valueOf(0));
                    }
                }
                db.addRec("Answers",cv);
                if (db.isOpen())
                {
                    db.close();
                }
                //Fragment fragment = new F_MyAnswers();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.hide(this);
                ft.commit();
            }
            else
            {
                Toast.makeText(getActivity(),getResources().getString(R.string.a_registration_wrong_fill),Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        else return false;
    }
}