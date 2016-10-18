package net.pixeltk.glagol.fargment_catalog;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.adapter.ChoiceListAdapter;
import net.pixeltk.glagol.api.Audio;
import net.pixeltk.glagol.api.getHttpGet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 07.10.16.
 */

public class ChoiceItemList extends Fragment implements OnBackPressedListener {

    public ChoiceItemList() {
        // Required empty public constructor
    }

    Fragment fragment = null;
    String[] sort = {"Сортировка", "По цене", "По имени", "Новинки", "По популярности"};
    Spinner sort_spinner;
    private ArrayList<Audio> choiceItemFromCatalogs;
    private ChoiceListAdapter choiceListAdapter;
    ListView listView;
    private ArrayList<Audio> audios = new ArrayList<>();
    getHttpGet request = new getHttpGet();
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
        View view = inflater.inflate(R.layout.fragment_choise_item, container, false);

        sharedPreferences = getActivity().getSharedPreferences("Category", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sort_spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sort);
        sort_spinner.setAdapter(adapter);

        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 1:
                        sortCatalog("price");
                        break;
                    case 2:
                        sortCatalog("name");
                        break;
                    case 3:
                        sortCatalog("new");
                        break;
                    case 4:
                        sortCatalog("popular");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView = (ListView)  view.findViewById(R.id.list_choice_item);

        choiceItemFromCatalogs = new ArrayList<Audio>();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            Log.d("myLogs","in ");
            JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/newbooks?salt=df90sdfgl9854gjs54os59gjsogsdf"));
            Gson gson = new Gson();
            audios = gson.fromJson(data.toString(),  new TypeToken<List<Audio>>(){}.getType());

            if (audios!= null) {

                for (int i=0; i<audios.size(); i++)
                {
                    if (audios.get(i).getCategorys().equals(sharedPreferences.getString("CategoryName", ""))) {
                        Audio audio = audios.get(i);
                        choiceItemFromCatalogs.add(audio);
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (choiceItemFromCatalogs.size()==0)
        {
            Toast.makeText(getActivity(), "Список пуст!", Toast.LENGTH_LONG).show();
        }
        else
        {
            choiceListAdapter = new ChoiceListAdapter(getActivity(), choiceItemFromCatalogs);
            listView.setAdapter(choiceListAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editor.putString("idbook", choiceItemFromCatalogs.get(i).getId()).apply();
                Log.d("MyLog", " " + choiceItemFromCatalogs.get(i).getId());
                fragment = new CardBook();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.catalog_frame, fragment).commit();
                }
            }
        });


        return view;
    }

    public void sortCatalog(String method)
    {
        choiceItemFromCatalogs.clear();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            Log.d("myLogs","in ");
            JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/sort?salt=df90sdfgl9854gjs54os59gjsogsdf&sortby=" + method));
            Gson gson = new Gson();
            audios = gson.fromJson(data.toString(),  new TypeToken<List<Audio>>(){}.getType());

            if (audios!= null) {

                for (int i=0; i<audios.size(); i++)
                {
                    if (audios.get(i).getCategorys().equals(sharedPreferences.getString("CategoryName", ""))) {
                        Audio audio = audios.get(i);
                        choiceItemFromCatalogs.add(audio);
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (choiceItemFromCatalogs.size()==0)
        {
            Toast.makeText(getActivity(), "Список пуст!", Toast.LENGTH_LONG).show();
        }
        else
        {
            choiceListAdapter = new ChoiceListAdapter(getActivity(), choiceItemFromCatalogs);
            listView.setAdapter(choiceListAdapter);
        }
    }
    @Override
    public void onBackPressed() {
        fragment = new ListFragmentGlagol();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.catalog_frame, fragment).commit();
            editor.clear().apply();
        }

    }


}
