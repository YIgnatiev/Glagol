package net.pixeltk.glagol.fargment_catalog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.activity.TabActivity;
import net.pixeltk.glagol.adapter.NavDrawerItem;
import net.pixeltk.glagol.adapter.NavDrawerListAdapter;
import net.pixeltk.glagol.fragment.MainFragment;

import java.util.ArrayList;

/**
 * Created by root on 07.10.16.
 */

public class ListFragmentGlagol extends Fragment implements OnBackPressedListener{

    public ListFragmentGlagol() {
        // Required empty public constructor
    }
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ListView mDrawerList;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    Fragment fragment = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ImageView back_arrow, logo;
    TextView name_frag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        sharedPreferences = getActivity().getSharedPreferences("Category", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        name_frag = (TextView) view.findViewById(R.id.name_frag);
        name_frag.setText("Каталог");

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerList = (ListView) view.findViewById(R.id.list_view);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        for (int i = 0; i < navMenuIcons.length(); i++) {
            navDrawerItems.add(new NavDrawerItem(navMenuIcons.getResourceId(i, -1), navMenuTitles[i], R.mipmap.back_icon));
        }

        adapter = new NavDrawerListAdapter(getActivity(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                editor.putString("CategoryName", navMenuTitles[i]).apply();
                fragment = new ChoiceItemList();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, fragment).commit();
                }
            }
        });

        return view;
    }
    @Override
    public void onBackPressed() {
        fragment = new MainFragment();
        TabActivity tabActivity = new TabActivity();
        tabActivity.changePage();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame, fragment).commit();

        }


    }
}
