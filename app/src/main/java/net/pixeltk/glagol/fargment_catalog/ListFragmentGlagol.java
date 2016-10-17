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
import android.widget.ListView;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.adapter.NavDrawerItem;
import net.pixeltk.glagol.adapter.NavDrawerListAdapter;

import java.util.ArrayList;

/**
 * Created by root on 07.10.16.
 */

public class ListFragmentGlagol extends Fragment {

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
                            .replace(R.id.catalog_frame, fragment).commit();
                }
            }
        });

        return view;
    }
}
