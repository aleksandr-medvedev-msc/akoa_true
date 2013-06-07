package com.shema.aoka;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 18.03.13
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
public class F_NewsNovelFragment extends Fragment {
    private final String LOG_TAG = "F_NewsNovelFragment_Log";
    private byte [] image;
    private String title;
    private String date;
    private String text;
    public F_NewsNovelFragment(byte[] image, String title, String date,String text)
    {
        this.image = image;
        this.text = text;
        this.date = date;
        this.title = title;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.f_newsnovelfragment,null);
        TextView titleView = (TextView)v.findViewById(R.id.f_newsnovelfragment_tv_title);
        TextView dateView = (TextView)v.findViewById(R.id.f_newsnovelfragment_tv_date);
        TextView textView = (TextView)v.findViewById(R.id.f_newsnovelfragment_tv_text);
        ImageView picture = (ImageView)v.findViewById(R.id.f_newsnovelfragment_image);
        titleView.setText(title);
        textView.setText(text);
        dateView.setText(date);
        picture.setImageBitmap(BitmapFactory.decodeByteArray(image,0,image.length));
        return v;
    }
}
