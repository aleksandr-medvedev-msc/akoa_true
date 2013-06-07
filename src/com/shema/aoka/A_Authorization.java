package com.shema.aoka;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 09.02.13
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public class A_Authorization extends Activity implements Button.OnClickListener{
    private final String LOG_TAG = "A_AUTHORIZATION_LOG";
    EditText login;
    EditText password;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_authorization);
        login = (EditText)findViewById(R.id.a_authorization_login);
        password = (EditText)findViewById(R.id.a_authorization_password);
        Button confirmButton = (Button)findViewById(R.id.a_authorization_confirm_button);
        confirmButton.setOnClickListener(this);
        /*HttpTask task = new HttpTask();
        task.execute();*/
    }
    class HttpTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            /*TestHttpPost post = new TestHttpPost();
            try {
                String str = post.executeHttpPost();
                if (str==null)
                    Log.d(LOG_TAG,"NULL !!!");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            } */
            return null;
        }
    }
    @Override
    public void onClick(View v)
    {
        M_DB db = ((AokaApplication)getApplication()).getDb();
        final String dbTableName = "Profile";
        final String[] columns = {"login","password"};
        db.open();
        Cursor cursor = db.query(dbTableName, columns, null, null, null, null, null);
        boolean flag = false;
        if (cursor.moveToFirst())
        {
            do {
                String strLogin = cursor.getString(cursor.getColumnIndex(columns[0]));
                String strPassword = cursor.getString(cursor.getColumnIndex(columns[1]));
                if (strLogin.equals(login.getText().toString())&&strPassword.equals(password.getText().toString()))
                {
                    SharedPreferences sp = getSharedPreferences("account_options.xml",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("login",login.getText().toString());
                    editor.commit();
                    Intent intent = new Intent(this,A_Home.class);
                    startActivity(intent);
                    flag=true;
                }
            }   while (cursor.moveToNext())  ;
        }
        if (!flag)
        {
            Toast.makeText(this, R.string.a_authorization_wrong_login_or_password, Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}