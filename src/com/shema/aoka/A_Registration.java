package com.shema.aoka;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 09.02.13
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
public class A_Registration extends Activity implements Button.OnClickListener{
    private final String LOG_TAG = "A_REGISTRATION_LOG";
    EditText login;
    EditText password;
    ArrayList<HashMap<String,Object>> data;
    final String[] keys = {"name","type"};
    String[] finalEditTextData;
    boolean[] checks;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_registration);
        M_XmlParser parser = ((AokaApplication)getApplication()).getXmlParser();
        data=parser.registration_config_Parse(this, "user");
        final String[] editTextData = new String[data.size()];
        LinearLayout layout = (LinearLayout)findViewById(R.id.a_registration_layout);
        final String[] spinnerData = parser.sector_config_Parse(this);
        checks=new boolean[data.size()];
        for (int i = 0; i < data.size(); i++)
        {
            final int t = i;

            if (data.get(i).get(keys[1]).equals("text"))
            {
                View item = getLayoutInflater().inflate(R.layout.a_registration_item,layout,false);
                final CheckBox checkBox = (CheckBox)item.findViewById(R.id.a_registration_item_checkbox);
                checkBox.setEnabled(false);
                Log.d(LOG_TAG,"text");
                TextView title = (TextView)item.findViewById(R.id.a_registration_item_tv);
                title.setText(data.get(i).get(keys[0]).toString());
                EditText editText = (EditText)item.findViewById(R.id.a_registration_item_et);
                editText.setText(editTextData[t]);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        editTextData[t]=s.toString();
                        if (editTextData[t].isEmpty())
                        {
                            checkBox.setChecked(false);
                            checks[t]=false;
                        }
                        else
                        {
                            checkBox.setChecked(true);
                            checks[t]=true;
                        }
                    }
                });
                layout.addView(item);
            }
            if (data.get(i).get(keys[1]).equals("password"))
            {
                View item = getLayoutInflater().inflate(R.layout.a_registration_item,layout,false);
                final CheckBox checkBox = (CheckBox)item.findViewById(R.id.a_registration_item_checkbox);
                checkBox.setEnabled(false);
                TextView title = (TextView)item.findViewById(R.id.a_registration_item_tv);
                title.setText(data.get(i).get(keys[0]).toString());
                EditText editText = (EditText)item.findViewById(R.id.a_registration_item_et);
                Log.d(LOG_TAG,editText.getInputType()+ " input type");
                editText.setInputType(129); //TODO:Сделать правильный input type
                Log.d(LOG_TAG,"password");
                editText.setText(editTextData[t]);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        editTextData[t]=s.toString();
                        if (editTextData[t].isEmpty())
                        {
                            checkBox.setChecked(false);
                            checks[t]=false;
                        }
                        else
                        {
                            checkBox.setChecked(true);
                            checks[t]=true;
                        }

                    }
                });
                layout.addView(item);
            }
            if (data.get(i).get(keys[1]).equals("spinner"))
            {
                View item = getLayoutInflater().inflate(R.layout.a_registration_item_with_spinner,layout,false);
                TextView title = (TextView)item.findViewById(R.id.a_registration_item_with_spinner_tv);
                Log.d(LOG_TAG,"spinner");
                title.setText(data.get(i).get(keys[0]).toString());
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,spinnerData);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner spinner = (Spinner)item.findViewById(R.id.a_registration_spinner);
                spinner.setAdapter(spinnerAdapter);
                final CheckBox checkBox = (CheckBox)item.findViewById(R.id.a_registration_item_with_spinner_checkbox);
                checkBox.setEnabled(false);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        editTextData[t]=spinnerData[position];
                        checkBox.setChecked(true);
                        checks[t]=true;
                        }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        checkBox.setChecked(false);
                    }
                });
                layout.addView(item);
            }
        }
        View view = getLayoutInflater().inflate(R.layout.a_registration_button,layout,false);
        Button registrationButton = (Button)view.findViewById(R.id.a_registration_confirmbutton);
        registrationButton.setOnClickListener(this);
        layout.addView(view);
        this.finalEditTextData=editTextData;
    }
    @Override
    public void onClick(View v)
    {
        boolean flag = true;
        for (int i = 0; i < checks.length; i++)
        {
            if (!checks[i])
            {
                Toast.makeText(this,getResources().getString(R.string.a_registration_wrong_fill) + " " + data.get(i).get(keys[0]).toString(),Toast.LENGTH_SHORT).show();
                flag=false;
                break;
            }
        }
    if (flag)  //Проверить через интернет уникальность logina  пользователя,
    {
        M_DB db = ((AokaApplication)getApplication()).getDb();
        final String dbTableName = "Profile";
        ArrayList<HashMap<String,Object>> arrayList = ((AokaApplication)getApplication()).getXmlParser().getColumns(this,dbTableName);
        db.open();
        String[] checkLoginColumn = {"login"};
        String selection = "login = ?";
        String[] columns = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size();i++)
        {
            columns[i]=arrayList.get(i).get(keys[0]).toString();
        }
        String[] selectionArgs = {finalEditTextData[Arrays.binarySearch(columns,"login")]};
        Cursor checkCursor = db.query(dbTableName,checkLoginColumn,selection,selectionArgs,null,null,null);
        if (checkCursor.moveToFirst())
        {
            Toast.makeText(this,R.string.a_registration_busy_login,Toast.LENGTH_SHORT).show();
        }
        else
        {
            db.addRec(dbTableName,columns,finalEditTextData);
            SharedPreferences sp = getSharedPreferences("account_options.xml",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("ASD",false);
            editor.putString("login",finalEditTextData[Arrays.binarySearch(columns,"login")]);
            editor.commit();
            Intent intent = new Intent(this,A_Home.class);      //Запись в профиль.
            startActivity(intent);
        }
        db.close();
    }
    }

}