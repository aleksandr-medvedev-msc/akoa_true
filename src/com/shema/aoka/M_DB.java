package com.shema.aoka;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 16.01.13
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
public class M_DB {
    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;
    private ArrayList<String[]> columnsForTables;
    private String[] dbTableNames;
    private ArrayList<String[]> typesForTableColumns;
    public static final String COLUMN_ID = "_id";
    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;
    private ConcurrentLinkedQueue<M_Message> queue;
    private AokaApplication app;
    public String formDBCreateString (String dbTableName, String[] arrayOfColumns, String[] types)//если true, то Integer
    {
        if (dbTableName.equals("News_Photos"))
        {
            Log.d("QUERY", Arrays.deepToString(types));
        }
        //Log.d("QUERY", "QUERY: = " + " WTF ?????");
        String str = "create table " + dbTableName+ " (" + COLUMN_ID + " integer primary key autoincrement, ";
        if (arrayOfColumns.length==types.length)
        {
            for (int i = 0; i < arrayOfColumns.length; i++)
            {
                str += arrayOfColumns[i];
                str += (" " + types[i]);
                if (i!=arrayOfColumns.length-1)
                {
                    str += ", ";
                }
            }
        }
        str+=");";
        Log.d("QUERY", "QUERY: = " + str);
        return str;
    }
    public M_DB(Context ctx,ArrayList<String[]> columnsForTables,ArrayList<String[]> typesForTableColumns,String[] dbTableNames,AokaApplication application)
    {
        this.mCtx=ctx;
        this.columnsForTables=columnsForTables;
        this.typesForTableColumns=typesForTableColumns;
        this.dbTableNames=dbTableNames;
        queue = new ConcurrentLinkedQueue<M_Message>();
        this.app = application;
        isRunning = false;

    }
    public M_DB(Context ctx)
    {
        this.mCtx=ctx;
    }

    // открыть подключение
    private DBWorker worker;
    private boolean isRunning;
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        if (!isRunning)
        {
            Thread thread = new Thread(new DBWorker());
            isRunning = true;
            thread.start();
        }
    }
    public boolean inTransaction()
    {
        return (mDB==null||mDB.inTransaction());
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы dbTableName
    public Cursor getAllData(String dbTableName) {
        Cursor cursor1 = mDB.query(dbTableName,null,null,null,null,null,null);
        return cursor1;
    }

    // добавить запись в Таблицу
    public boolean addRec(String dbTableName, String[] columns, String[] values) {
        ContentValues cv = new ContentValues();
        boolean result = false;
        if (columns.length==values.length)
        {
            for (int i = 0; i < columns.length; i++)
            {
                cv.put(columns[i],values[i]);
            }
            mDB.beginTransaction();
            try {
                mDB.insert(dbTableName, null, cv);
                mDB.setTransactionSuccessful();
                result=true;
            }
            finally {
                mDB.endTransaction();
            }
        }
        return result;
    }
    public boolean addRec(String dbTableName,ContentValues contentValues)
    {
        boolean result=false;
        mDB.beginTransaction();
        try {
            mDB.insert(dbTableName,null,contentValues);
            mDB.setTransactionSuccessful();
            result=true;
        }
        finally {
            mDB.endTransaction();
        }
        return result;
    }

    // удалить запись из DB_TABLE
    public boolean delRec(long id,String dbTableName) {
        mDB.beginTransaction();
        boolean result = false;
        try {
            mDB.delete(dbTableName, COLUMN_ID + " = " + id, null);
            mDB.setTransactionSuccessful();
            result=true;
        }
        finally {
            mDB.endTransaction();
        }
        return result;
    }

    public boolean update(String dbTableName, ContentValues contentValues,String whereClause,String[] whereArgs)
    {
        boolean result = false;
        mDB.beginTransaction();
        try {
            mDB.update(dbTableName,contentValues,whereClause,whereArgs);
            mDB.setTransactionSuccessful();
            result=true;
        }
        finally {
            mDB.endTransaction();
        }
        return result;
    }

    public boolean delete(String dbTableName,String whereClause,String[] whereArgs)
    {
        boolean result = false;
        mDB.beginTransaction();
        try {

            int del = mDB.delete(dbTableName,whereClause,whereArgs);
            Log.d("DB_delete",del + "delete rows count");
            mDB.setTransactionSuccessful();
            result=true;
        }
        finally {
            mDB.endTransaction();
        }
        return result;
    }

    public Cursor rawQuery(String sql,String[] selectionArgs)
    {
        Cursor cursor = mDB.rawQuery(sql,selectionArgs);
        return cursor;
    }
    public Cursor query(String table,String[] columns,String selection,String[] selectionArgs,
                        String groupBy,String having,String orderBy)
    {
        if (!mDB.isOpen())
        {
            this.open();
        }
        Cursor cursor = mDB.query(table,columns,selection,selectionArgs,groupBy,having,orderBy);
        if (cursor==null) Log.d("SEARCHORDERS", "NULL CURSOR");
        return cursor;
    }
    public void writeOrder(ContentValues values,String dbTableName,long date)
    {
        if (!mDB.isOpen())
        {
            this.open();
        }
        final String _ordersTableName = "Orders";
        ContentValues _orderValues = new ContentValues();
        _orderValues.put("global_id",values.getAsLong("global_id"));
        _orderValues.put("type",dbTableName);
        if (date>0)
        {
            _orderValues.put("date",date);
        }
        M_Message _message = new M_Message(values,dbTableName,_orderValues,_ordersTableName);
        queue.add(_message);
    }
    public boolean isOpen()
    {
        return mDB.isOpen();
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
            Log.d("CURSOR","NEWDBHELPER");
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            Log.d("CURSOR","onCREATE DBHELPER");
            if (dbTableNames.length==columnsForTables.size()&&dbTableNames.length==typesForTableColumns.size())
            {
                for (int i = 0; i < dbTableNames.length;i++)
                {
                    db.execSQL(formDBCreateString(dbTableNames[i],columnsForTables.get(i),typesForTableColumns.get(i)));
                }
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
        /*
          Функция принимает поля из таблицы bunkering_order, таблицу order функция заполняет сама
         */


    }
    private class DBWorker implements Runnable
    {
        public boolean isRunned() {
            return isRunned;
        }

        private boolean isRunned;
        public DBWorker()
        {

        }
        @Override
        public void run()
        {
            isRunned = true;
            while (true)
            {
                if (queue.isEmpty())
                {
                    try {
                        Thread.sleep(100,0);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    M_Message _message = queue.peek();
                    app.getDb().addRec(_message.getFirstTableName(),_message.getFirstValues());
                    if (_message.getSecondTableName()!=null)
                    {
                        app.getDb().addRec(_message.getSecondTableName(),_message.getSecondValues());
                    }
                    int t = 0;
                }
            }
        }
    }
}
