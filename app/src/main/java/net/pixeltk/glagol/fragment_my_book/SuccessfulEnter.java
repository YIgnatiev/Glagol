package net.pixeltk.glagol.fragment_my_book;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.activity.TabActivity;
import net.pixeltk.glagol.adapter.BookMarksHelper;
import net.pixeltk.glagol.adapter.DataBasesHelper;
import net.pixeltk.glagol.adapter.DrawItemBookMarks;
import net.pixeltk.glagol.adapter.DrawerListBookMarks;
import net.pixeltk.glagol.fargment_catalog.CardBook;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;
import net.pixeltk.glagol.fragment.MainFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 07.10.16.
 */

public class SuccessfulEnter extends Fragment implements OnBackPressedListener{

    public SuccessfulEnter() {
        // Required empty public constructor
    }
    LinearLayout listen_incl, buy_incl, my_tab_incl, history_incl;
    Button listen, buy, my_tab, history;
    private ArrayList<DrawItemBookMarks> navDrawerItems;
    private static DrawerListBookMarks adapter;
    ListView listView;
    DataBasesHelper dataBasesHelper;
    BookMarksHelper bookMarksHelper;
    ArrayList IdList = new ArrayList();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView name_frag;
    ImageView back_arrow, logo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_successful_enter, container, false);

        listen_incl = (LinearLayout) view.findViewById(R.id.listen_incl);
        buy_incl = (LinearLayout) view.findViewById(R.id.buy_incl);
        my_tab_incl = (LinearLayout) view.findViewById(R.id.my_tab_incl);
        history_incl = (LinearLayout) view.findViewById(R.id.history_incl);

        listen = (Button) view.findViewById(R.id.listen);
        buy = (Button) view.findViewById(R.id.buy);
        my_tab = (Button) view.findViewById(R.id.my_tab);
        history = (Button) view.findViewById(R.id.history);

        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        name_frag = (TextView) view.findViewById(R.id.name_frag);
        name_frag.setText("Мои книги");

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sharedPreferences = getActivity().getSharedPreferences("Category", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        listView = (ListView) view.findViewById(R.id.list_view);

        navDrawerItems = new ArrayList<DrawItemBookMarks>();
        dataBasesHelper = new DataBasesHelper(getActivity());

        IdList = dataBasesHelper.getidRow();
        Log.d("MyLog", String.valueOf(IdList));

        for (int i = 0; i < IdList.size(); i++)
        {
            bookMarksHelper = dataBasesHelper.getProduct(IdList.get(i).toString());
            Log.d("MyLog", "Id" + bookMarksHelper.getId_book());
            navDrawerItems.add(new DrawItemBookMarks(bookMarksHelper.getName_author(), bookMarksHelper.getName_book(), bookMarksHelper.getPrice(), bookMarksHelper.getImg_url(), bookMarksHelper.getId_book()));
            bookMarksHelper = null;
        }

        adapter = new DrawerListBookMarks(getActivity(), navDrawerItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                editor.putString("idbook", navDrawerItems.get(i).getId());
                editor.putString("intent", "Successful").apply();
                Fragment fragment = new CardBook();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, fragment).commit();
                }
            }
        });


        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen.setTextColor(Color.parseColor("#000000"));
                buy.setTextColor(Color.parseColor("#F1F1F1"));
                my_tab.setTextColor(Color.parseColor("#F1F1F1"));
                history.setTextColor(Color.parseColor("#F1F1F1"));

                listen.setBackgroundResource(R.drawable.background);
                buy.setBackgroundResource(R.drawable.backsearch);
                my_tab.setBackgroundResource(R.drawable.backsearch);
                history.setBackgroundResource(R.drawable.backsearch);

                listen_incl.setVisibility(View.VISIBLE);
                buy_incl.setVisibility(View.GONE);
                my_tab_incl.setVisibility(View.GONE);
                history_incl.setVisibility(View.GONE);
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen.setTextColor(Color.parseColor("#F1F1F1"));
                buy.setTextColor(Color.parseColor("#000000"));
                my_tab.setTextColor(Color.parseColor("#F1F1F1"));
                history.setTextColor(Color.parseColor("#F1F1F1"));

                listen.setBackgroundResource(R.drawable.backsearch);
                buy.setBackgroundResource(R.drawable.background);
                my_tab.setBackgroundResource(R.drawable.backsearch);
                history.setBackgroundResource(R.drawable.backsearch);

                listen_incl.setVisibility(View.GONE);
                buy_incl.setVisibility(View.VISIBLE);
                my_tab_incl.setVisibility(View.GONE);
                history_incl.setVisibility(View.GONE);
            }
        });

        my_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen.setTextColor(Color.parseColor("#F1F1F1"));
                buy.setTextColor(Color.parseColor("#F1F1F1"));
                my_tab.setTextColor(Color.parseColor("#000000"));
                history.setTextColor(Color.parseColor("#F1F1F1"));

                listen.setBackgroundResource(R.drawable.backsearch);
                buy.setBackgroundResource(R.drawable.backsearch);
                my_tab.setBackgroundResource(R.drawable.background);
                history.setBackgroundResource(R.drawable.backsearch);

                listen_incl.setVisibility(View.GONE);
                buy_incl.setVisibility(View.GONE);
                my_tab_incl.setVisibility(View.VISIBLE);
                history_incl.setVisibility(View.GONE);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen.setTextColor(Color.parseColor("#F1F1F1"));
                buy.setTextColor(Color.parseColor("#F1F1F1"));
                my_tab.setTextColor(Color.parseColor("#F1F1F1"));
                history.setTextColor(Color.parseColor("#000000"));

                listen.setBackgroundResource(R.drawable.backsearch);
                buy.setBackgroundResource(R.drawable.backsearch);
                my_tab.setBackgroundResource(R.drawable.backsearch);
                history.setBackgroundResource(R.drawable.background);

                listen_incl.setVisibility(View.GONE);
                buy_incl.setVisibility(View.GONE);
                my_tab_incl.setVisibility(View.GONE);
                history_incl.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
    @Override
    public void onBackPressed() {
        Fragment fragment = new MainFragment();
        TabActivity tabActivity = new TabActivity();
        tabActivity.changePage();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame, fragment).commit();

        }


    }
    public static void dataChanged()
    {
        adapter.notifyDataSetChanged();
    }
}