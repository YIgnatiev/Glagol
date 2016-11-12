package net.pixeltk.glagol.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import net.pixeltk.glagol.adapter.ChoiceListAdapter;
import net.pixeltk.glagol.api.Audio;
import net.pixeltk.glagol.api.getHttpGet;
import net.pixeltk.glagol.fargment_catalog.CardBook;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 24.10.16.
 */

public class ListSearchBook extends Fragment implements OnBackPressedListener {

    public ListSearchBook() {
        // Required empty public constructor
    }
    ImageView back_arrow, logo;
    TextView name_frag;
    Fragment fragment = null;
    ListView list_book;
    private ArrayList<Audio> itemData = new ArrayList<>();
    private ArrayList<Audio> audios = new ArrayList<>();
    getHttpGet request = new getHttpGet();
    private ChoiceListAdapter choiceListAdapter;
    SharedPreferences search_line;
    SharedPreferences.Editor serch_edit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.list_for_search_book, container, false);

        search_line = getActivity().getSharedPreferences("Search", Context.MODE_PRIVATE);
        serch_edit = search_line.edit();

        Log.d("MyLog", search_line.getString("search", " "));

        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        name_frag = (TextView) view.findViewById(R.id.name_frag);
        name_frag.setText("Поиск");

        list_book = (ListView) view.findViewById(R.id.list_book);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                back_arrow.setBackgroundResource(R.drawable.bakground_arrow);
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            Log.d("myLogs","in ");
            JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/search?salt=df90sdfgl9854gjs54os59gjsogsdf&search=" + search_line.getString("search", " ")));
            Gson gson = new Gson();
            audios = gson.fromJson(data.toString(),  new TypeToken<List<Audio>>(){}.getType());

            if (audios!= null) {

                for (int i=0; i<audios.size(); i++)
                {
                    Audio audio = audios.get(i);
                    itemData.add(audio);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        choiceListAdapter = new ChoiceListAdapter(getActivity(), itemData);
        list_book.setAdapter(choiceListAdapter);
        list_book.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Category", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("idbook", itemData.get(i).getId());
                editor.putString("intent", "ListSearch");
                editor.apply();
                fragment = new CardBook();
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
        serch_edit.remove("search").apply();
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
