package com.shema.aoka;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.slidingmenu.lib.SlidingMenu;

import java.security.cert.PolicyNode;


/**
 * Created with IntelliJ IDEA.
 * User: ag
 * Date: 5/6/13
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class F_Menu extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    int parent_layout;
    SlidingMenu menu;
    private Fragment[] fragments;
    public F_Menu(int layout,SlidingMenu menu)
    {
        this.parent_layout = layout;
        this.menu = menu;
        fragments = new Fragment[]{new F_News(),new F_Indexes(),new F_Orders(),new F_Subscription(),new F_MyOrders(),new F_MyAnswers()};
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.f_menu,null);
        LinearLayout layout = (LinearLayout)v.findViewById(R.id.f_menulayout);
        View vv = inflateHeader("Заявки",layout,inflater);
        layout.addView(inflateHeader("Заявки",layout,inflater));
        layout.addView(inflateText("Все заявки",layout,inflater,fragments[2]));
        layout.addView(inflateHeader("Аналитика",layout,inflater));
        layout.addView(inflateText("Новости",layout,inflater,fragments[0]));
        layout.addView(inflateText("Индексы",layout,inflater,fragments[1]));
        layout.addView(inflateHeader("Кабинет", layout, inflater));
        layout.addView(inflateText("Подписка",layout,inflater,fragments[3]));
        layout.addView(inflateText("Мои заявки",layout,inflater,fragments[4]));
        layout.addView(inflateText("Мои ответы",layout,inflater,fragments[5]));
                               //                       layout.setBackgroundColor(R.color.menu_text);
        //layout.setBackgroundColor();
        //layout.addView(inflateText("Поиск заявок",layout,inflater,new F_));

        return v;
    }
    private View inflateHeader(String name, LinearLayout layout, LayoutInflater inflater)
    {
        View next = inflater.inflate(R.layout.f_menu_header_tv,layout,false);
        TextView header = (TextView)next.findViewById(R.id.f_menu_headertv);
        header.setText(name);
        header.setTextColor(Color.argb(255,255,255,255));
        next.setBackgroundColor(Color.CYAN);
        next.setBackgroundColor(Color.argb(255,66,49,137));
        return next;
    }
    private View inflateText(String name, LinearLayout layout, LayoutInflater inflater, final Fragment fragment)
    {
        View next = inflater.inflate(R.layout.f_menu_text,layout,false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(parent_layout, fragment);
                ft.addToBackStack("slidingMenuStack");
                ft.commit();
                menu.showContent(true);
            }
        });
        TextView text = (TextView)next.findViewById(R.id.f_menu_text);
        text.setText(name);
        text.setPadding(24,8,8,8);
        text.setTextColor(Color.argb(255,255,255,255));
        next.setBackgroundColor(Color.BLUE);
        next.setBackgroundColor(Color.argb(255,102,0,255));
        return next;
    }
}