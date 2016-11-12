package net.pixeltk.glagol.fargment_catalog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.activity.TabActivity;
import net.pixeltk.glagol.adapter.NavDrawerItem;
import net.pixeltk.glagol.adapter.NavDrawerListAdapter;
import net.pixeltk.glagol.api.Audio;
import net.pixeltk.glagol.api.getHttpGet;
import net.pixeltk.glagol.fragment.MainFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 07.10.16.
 */

public class ListFragmentGlagol extends Fragment implements OnBackPressedListener{

    public ListFragmentGlagol() {
        // Required empty public constructor
    }

    private TypedArray navMenuIcons;
    private ListView mDrawerList;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    Fragment fragment = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ArrayList<Audio> audios = new ArrayList<>();
    getHttpGet request = new getHttpGet();

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

                back_arrow.setBackgroundResource(R.drawable.bakground_arrow);
                onBackPressed();
            }
        });
        navDrawerItems = new ArrayList<NavDrawerItem>();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            Log.d("myLogs","in ");
            JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/categories?salt=df90sdfgl9854gjs54os59gjsogsdf"));
            Gson gson = new Gson();
            Log.d("myLog", data.toString());
            audios = gson.fromJson(data.toString(),  new TypeToken<List<Audio>>(){}.getType());

            if (audios!= null) {

                for (int i=0; i<audios.size(); i++)
                {
                        Audio audio = audios.get(i);
                    Log.d("myLog", audio.getName_book());

                        navDrawerItems.add(new NavDrawerItem(audio.getIcon(), audio.getName_book(), R.mipmap.back_icon));
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDrawerList = (ListView) view.findViewById(R.id.list_view);


        adapter = new NavDrawerListAdapter(getActivity(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                editor.putString("CategoryName", navDrawerItems.get(i).getTitle()).apply();
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
