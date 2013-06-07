package com.shema.aoka;

import android.content.ContentValues;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created with IntelliJ IDEA.
 * User: ag
 * Date: 4/24/13
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class M_Message {
    public String getSecondTableName() {
        return secondTableName;
    }

    public ContentValues getFirstValues() {
        return firstValues;
    }

    public String getFirstTableName() {
        return firstTableName;
    }

    public ContentValues getSecondValues() {
        return secondValues;
    }

    private ContentValues firstValues;
    private String firstTableName;
    private ContentValues secondValues;
    private String secondTableName;
    public M_Message(ContentValues firstValues,String firstTableName,ContentValues secondValues,String secondTableName)
    {

        this.firstValues = firstValues;
        this.firstTableName = firstTableName;
        this.secondTableName = secondTableName;
        this.secondValues = secondValues;
    }
}
