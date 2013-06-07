package com.shema.aoka;

import android.content.ContentValues;
import android.content.Context;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 09.02.13
 * Time: 18:05
 * To change this template use File | Settings | File Templates.
 */
public class M_XmlParser {
    public ContentValues orderParse(XmlPullParser parser)       //Парсим всю заявку, один парсер - одна заявка.
    {
        ContentValues cv = new ContentValues();
        try {
            while (parser.getEventType()!=XmlPullParser.END_DOCUMENT)
            {
                switch (parser.getEventType())
                {
                    case XmlPullParser.START_TAG:
                        if (parser.getDepth()!=1)
                        {
                            for (int i = 0; i < parser.getAttributeCount();i++)
                            {
                                cv.put(parser.getAttributeName(i),parser.getAttributeValue(i));
                            }
                        }
                        break;
                }
                parser.next();
            }
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return cv;
    }
    public boolean indexesToDB(AokaApplication app,XmlPullParser xpp)
    {
        xpp = app.getResources().getXml(R.xml.indexes);    //TODO: pereopredelit xpp obratno!:)
        ContentValues cv = new ContentValues(3);
        final int depth = 3;
        M_DB db = app.getDb();
        if (!db.isOpen())
        {
            db.open();
        }
        try {
            while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT)
            {
                switch (xpp.getEventType())
                {
                    case XmlPullParser.START_TAG:
                        if (xpp.getDepth()==depth-1)
                        {
                            cv.put(xpp.getAttributeName(0),xpp.getAttributeValue(0));
                        }
                        if (xpp.getDepth()==depth)
                        {
                            for (int i = 0; i < xpp.getAttributeCount(); i++)
                            {
                                cv.put(xpp.getAttributeName(i),xpp.getAttributeValue(i));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xpp.getDepth()==depth-1)
                        {
                            db.addRec("Indexes",cv);
                            cv = new ContentValues(3);
                        }
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        db.close();
        return true;
    }
    public ArrayList<HashMap<String,Object>> orders_config_Parse(Context context,String orderType,int mainDepth)
    {
        ArrayList<HashMap<String,Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        HashMap<String,Object> map = new HashMap<String, Object>();
        final int depth = 2;
        final int field_depth = mainDepth;
        boolean flag = false;
        try{
            XmlPullParser xpp = context.getResources().getXml(R.xml.orders_config);
            while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT)
            {
                switch (xpp.getEventType())
                {
                    case XmlPullParser.START_TAG:
                        if (xpp.getDepth()==depth)
                        {
                            if (xpp.getAttributeValue(0).equals(orderType))
                            {
                                flag=true;
                            }
                        }
                        if (xpp.getDepth()==field_depth&&flag)
                        {
                            for (int i = 0; i < xpp.getAttributeCount(); i++)
                            {
                                map.put(xpp.getAttributeName(i),xpp.getAttributeValue(i));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xpp.getDepth()==depth&&flag)
                        {
                            return arrayList;
                        }
                        if (xpp.getDepth()==field_depth&&flag)
                        {
                            arrayList.add(map);
                            map = new HashMap<String, Object>();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        }   catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }   catch (IOException e)
        {
            e.printStackTrace();
        }
        return arrayList;

    }


    public ArrayList<HashMap<String,Object>> answers_config_Parse(Context context)
    {
        ArrayList<HashMap<String,Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        HashMap<String,Object> map = new HashMap<String, Object>();
        int field_depth = 2;
        try{
            XmlPullParser xpp = context.getResources().getXml(R.xml.answers_config);
            while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT)
            {
                switch (xpp.getEventType())
                {
                    case XmlPullParser.START_TAG:

                        if (xpp.getDepth()==field_depth)
                        {
                            for (int i = 0; i < xpp.getAttributeCount(); i++)
                            {
                                map.put(xpp.getAttributeName(i),xpp.getAttributeValue(i));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:

                        if (xpp.getDepth()==field_depth)
                        {
                            arrayList.add(map);
                            map = new HashMap<String, Object>();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        }   catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }   catch (IOException e)
        {
            e.printStackTrace();
        }
        return arrayList;
    }
    public String[] sector_config_Parse(Context context)
    {
        ArrayList<String> sectorList = new ArrayList<String>();
        try{
            XmlPullParser xpp = context.getResources().getXml(R.xml.registration_config);
            while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT)
            {
                switch (xpp.getEventType())
                {
                    case XmlPullParser.START_TAG:
                        if (xpp.getDepth()==2)
                        {
                            sectorList.add(xpp.getAttributeValue(0));
                        }
                        break;
                    case XmlPullParser.END_TAG:

                        break;
                    case XmlPullParser.TEXT:
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        }   catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }   catch (IOException e)
        {
            e.printStackTrace();
        }
        String[] sectors = new String[sectorList.size()];
        sectors = sectorList.toArray(sectors);
        return sectors;
    }
    public ArrayList<HashMap<String,Object>> registration_config_Parse(Context context,String profileId) //В файле лежат наборы полей для регистрации юзера и для регистрации компании
    {
        ArrayList<HashMap<String,Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        HashMap<String,Object> map = new HashMap<String, Object>();
        int profile_depth = 2;
        int field_depth = 3;
        boolean flag = false;
        try{
            XmlPullParser xpp = context.getResources().getXml(R.xml.registration_config);
            while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT)
            {
                switch (xpp.getEventType())
                {
                    case XmlPullParser.START_TAG:
                        if (xpp.getDepth()==profile_depth)
                        {
                            if (xpp.getAttributeValue(0).equals(profileId))
                            {
                                flag=true;
                            }
                        }
                        if (xpp.getDepth()==field_depth&&flag)
                        {
                            for (int i = 0; i < xpp.getAttributeCount(); i++)
                            {
                                map.put(xpp.getAttributeName(i),xpp.getAttributeValue(i));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xpp.getDepth()==profile_depth&&flag)
                        {
                            return arrayList;
                        }
                        if (xpp.getDepth()==field_depth)
                        {
                            arrayList.add(map);
                            map = new HashMap<String, Object>();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        }   catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }   catch (IOException e)
        {
            e.printStackTrace();
        }
        return arrayList;
    }
    public ArrayList<HashMap<String,Object>> getColumns(Context context,String tableId)         //Забирает имена столбцов из соотв. таблицы
    {
        ArrayList<HashMap<String,Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        HashMap<String,Object> map = new HashMap<String, Object>();
        final int depth = 2;
        final int field_depth = 3;
        boolean flag = false;
        try{
            XmlPullParser xpp = context.getResources().getXml(R.xml.database_config);
            while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT)
            {
                switch (xpp.getEventType())
                {
                    case XmlPullParser.START_TAG:
                        if (xpp.getDepth()==depth)
                        {
                            if (xpp.getAttributeValue(0).equals(tableId))
                            {
                                flag=true;
                            }
                        }
                        if (xpp.getDepth()==field_depth&&flag)
                        {
                            for (int i = 0; i < xpp.getAttributeCount(); i++)
                            {
                                map.put(xpp.getAttributeName(i),xpp.getAttributeValue(i));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xpp.getDepth()==depth&&flag)
                        {
                            return arrayList;
                        }
                        if (xpp.getDepth()==field_depth&&flag)
                        {
                            arrayList.add(map);
                            map = new HashMap<String, Object>();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        }   catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }   catch (IOException e)
        {
            e.printStackTrace();
        }
        return arrayList;
    }
}
