package com.shema.aoka;

import android.content.ContentValues;
import android.content.Context;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 09.02.13
 * Time: 14:43
 * To change this template use File | Settings | File Templates.
 */
public class M_DBFiller {
    private ArrayList<String[]> DBcolumns;
    private String[] tables;
    private ArrayList<String[]> DBtypes;
    private boolean flag;
    private AokaApplication context;
    public M_DBFiller(AokaApplication app)
    {
        this.context=app;
        this.flag=DBCreate();
    }
    public boolean isFlag()
    {
        return flag;
    }
    public byte[] imageDecode(String filename)
    {
        FileInputStream stream = null;
        long length=0;
        try {
            stream = context.getResources().getAssets().openFd(filename).createInputStream();
            length=context.getResources().getAssets().openFd(filename).getLength();
        }  catch (IOException e)
        {
            e.printStackTrace();
        }
        byte[] imageBytes = new byte[(int)length];
        if (length!=0)
        {
            for (int i = 0; i < length;i++)
            {
                try {
                    imageBytes[i]=(byte)stream.read();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return imageBytes;
    }
    public boolean newsTableFill()
    {

        ContentValues cv = new ContentValues();
        cv.put("novel_id",2);
        cv.put("body_photo",imageDecode("tanker.png"));
        M_DB db = new M_DB(context);
        db.open();
        db.addRec("News_Photos", cv);
        cv.remove("novel_id");
        cv.put("novel_id", 3);
        cv.remove("body_photo");
        cv.put("body_photo",imageDecode("bunkerport.png"));
        db.addRec("News_Photos", cv);
        db.close();
        addNewsToDatabase(2);
        addNewsToDatabase(3);
        return true;
    }
    public void addNewsToDatabase(int id)  //Временно, будет заменена на сервис
    {
        M_DB db = new M_DB(context);
        db.open();
        int index = Arrays.binarySearch(tables, "News", String.CASE_INSENSITIVE_ORDER);
        String[] values = new String[DBcolumns.get(index).length];
        if (id==2)
        {
            values[1]="Пираты освободили угнанный французский танкер, захватив часть груза";
            values[2]="1 января 2013 года";
            values[3]="Французский нефтяной танкер \"Гасконь\", захваченный ранее пиратами в территориальных водах Кот-д'Ивуара освобожден, сообщает ИТАР-ТАСС со ссылкой на владельца судна - компанию Sea Tankers.\n" +
                    "\n" +
                    "\"Он был освобожден в среду, рано утром. Пираты покинули корабль, который сейчас находится под контролем капитана\", - отметили в судоходной компании. Члены экипажа - 17 человек - получили во время захвата легкие ранения, и сейчас их осматривают врачи.\n" +
                    "\n" +
                    "Угнанный пиратами танкер перевозил дизельное топливо. Обычно пираты откачивают из захваченных судов топливо, которое потом продают на черном рынке.\n" +
                    "\n" +
                    "Как отметили в Sea Tankers, морские бандиты ушли не с пустыми руками. \"Была похищена часть груза\", - добавили в компании. Где именно сейчас находится \"Гасконь\" и куда держит курс, не сообщается из соображений безопасности. Владельцы судна также не стали объяснять, на каких условиях оно было освобождено, однако поблагодарили местные власти за содействие в этом вопросе.";
        }
        if (id==3)
        {
            values[1]="Во Владивостоке грязный снег сбрасывают в море";
            values[2]="15 января 2013 года";
            values[3]="В столице Приморья снег, убранный с городских трасс, не стесняются сбрасывать прямо в Амурский залив. Об этом в одной из социальных сетей рассказали очевидцы. За 40 минут приехало более 20 грузовиков. Снег загружают на площади Борцов за власть Советов, а сбрасывают в море за Набережной, возле автодрома «Аник».\n" +
                    "Пресс-служба мэрии Владивостока сообщила о том, что грузовики, которые сбрасывают грязный снег в акваторию, МУП «Дороги Владивостока» не принадлежат. \n" +
                    "Все машины этой организации имеют фирменный оранжевый логотип. Они счищают снег, складируют его на специально отведенных площадках, а затем вывозят на территорию выработанного буто-щебёночного карьера.";
        }

        values[0]=id+"";
        db.addRec(tables[index],DBcolumns.get(index),values);
        db.close();
    }
    public M_DB db;
    private  boolean DBCreate()        //Функция, создающая и размечающая базу данных при первом запуске приложения
    {
        if (columnsDBParse(R.xml.database_config))
        {
            db = new M_DB(context,DBcolumns,DBtypes,tables,context);
            db.open();
            db.close();
            return true;
        }
        return false;
    }
    private boolean ordersTableFill() //Служебная функция для заполнения таблицы, прототип функции для сервиса.
    {
        M_DB db = new M_DB(this.context);
        db.open();
        String[] typesOfOrders = {"Bunkering_order","Tank_order","Oil_delivery_order","Benzin_order"};
        boolean[] results = new boolean[typesOfOrders.length];
        ArrayList<String[]> columnsOfAllOrders = new ArrayList<String[]>();
        ContentValues[] cv = new ContentValues[typesOfOrders.length];
        String[] columnsOfOrder;

        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
        for (String t : Constants.typesOfOrders)
        {
            list = context.getXmlParser().getColumns(context,t);
            columnsOfOrder = new String[list.size()];
            for (int i = 0; i < list.size(); i++)
            {
                columnsOfOrder[i]=list.get(i).get(Constants.keys[0]).toString();
            }
            columnsOfAllOrders.add(columnsOfOrder);
        }
        boolean ordersResult = true;
        list = context.getXmlParser().getColumns(context,"Orders");
        String[] colsOfMainTable = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
        {
            colsOfMainTable[i] = list.get(i).get(Constants.keys[0]).toString();
        }
        ContentValues orders = new ContentValues();
        for (int i = 0; i < Constants.typesOfOrders.length;i++)
        {
            cv[i] = new ContentValues();
            for (int j = 0; j < columnsOfAllOrders.get(i).length;j++)
            {
                String str = "";
                if (j==0)
                {
                    str+=(i+1);
                }
                if (j==1)
                {
                    str+=0;
                }
                if (j!=0&&j!=1)
                {
                    str+=(j + " " + i + " j + i");
                }
                cv[i].put(columnsOfAllOrders.get(i)[j],str);
            }
            for (int j = 0; j < colsOfMainTable.length; j++)
            {
                if (j!=1)
                {
                    orders.put(colsOfMainTable[j],"" + (i+1));
                }
                else
                {
                    orders.put(colsOfMainTable[j],Constants.typesOfOrders[i]);
                }
            }

            ordersResult=db.addRec("Orders",orders);
            orders.clear();
            results[i] = db.addRec(typesOfOrders[i],cv[i]);
        }
        db.close();
        boolean result = true;
        for(boolean i : results)
        {
            if (!i) result=false;
        }
        return result&ordersResult;
    }
    public boolean columnsDBParse(int idOfFile)    //Функция, разбирающая xml файл с конфигами всех таблиц, сохраняющаяя названия таблиц, названия столбцов и типы столбцов
    {
        ArrayList<String[]> arrayList = new ArrayList<String[]>();
        ArrayList<String[]> typesArrayList = new ArrayList<String[]>();
        ArrayList<String> strings=new ArrayList<String>();
        ArrayList<String> typesStrings = new ArrayList<String>();
        ArrayList<String> list = new ArrayList<String>();
        try
        {
            XmlPullParser xpp=context.getResources().getXml(idOfFile);
            while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT)
            {
                switch (xpp.getEventType())
                {
                    case XmlPullParser.START_TAG:
                        if (xpp.getDepth()==2)
                        {
                            list.add(xpp.getAttributeValue(0));
                        }
                        if (xpp.getDepth()==3)
                        {
                            strings.add(xpp.getAttributeValue(0));
                            typesStrings.add(xpp.getAttributeValue(1));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xpp.getDepth()==2)
                        {
                            String[] helpArray = new String[strings.size()];
                            helpArray=strings.toArray(helpArray);
                            arrayList.add(helpArray);
                            helpArray=new String[typesStrings.size()];
                            helpArray=typesStrings.toArray(helpArray);
                            typesArrayList.add(helpArray);
                            typesStrings=new ArrayList<String>();
                            strings=new ArrayList<String>();
                        }
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
            this.DBcolumns = arrayList;
            this.DBtypes = typesArrayList;
            String[] arr = new String[list.size()];
            arr=list.toArray(arr);
            this.tables = arr;
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if (this.DBcolumns==null||this.tables==null)
        {
            return false;
        }
        else {
            return true;
        }
    }
}
