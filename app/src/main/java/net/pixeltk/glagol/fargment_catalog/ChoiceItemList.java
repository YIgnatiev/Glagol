package net.pixeltk.glagol.fargment_catalog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.adapter.ChoiceListAdapter;
import net.pixeltk.glagol.api.Audio;
import net.pixeltk.glagol.api.getHttpGet;
import net.pixeltk.glagol.fragment.ListSearchBook;

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
    private ArrayList<Audio> auto_compliet = new ArrayList<>();
    getHttpGet request = new getHttpGet();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView back_arrow, logo;
    TextView name_frag;
    SearchView searchView;
    LinearLayout search_variant;
    ListView list_variant;

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

        searchView=(SearchView) view.findViewById(R.id.searchView);
        searchView.setQueryHint("Поиск");
        searchView.setHovered(false);

        search_variant = (LinearLayout) view.findViewById(R.id.search_variant);

        list_variant = (ListView) view.findViewById(R.id.list_variant);

        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        name_frag = (TextView) view.findViewById(R.id.name_frag);
        name_frag.setText(sharedPreferences.getString("CategoryName", ""));

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                back_arrow.setBackgroundResource(R.drawable.bakground_arrow);
                onBackPressed();
            }
        });

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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.trim();
                if (newText.toCharArray().length > 3) {
                    search_variant.setVisibility(View.VISIBLE);
                    try {
                        JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/autocomplete?salt=df90sdfgl9854gjs54os59gjsogsdf&search=" + newText));
                        Gson gson = new Gson();

                        auto_compliet = gson.fromJson(data.toString(),  new TypeToken<List<Audio>>(){}.getType());

                        if (auto_compliet!= null) {
                            final String words[] = new String[auto_compliet.size()];
                            for (int i = 0; i < auto_compliet.size(); i++)
                            {
                                Audio audio = auto_compliet.get(i);
                                words[i] = audio.getWord();
                            }
                            if (words[0] != null) {
                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                        R.layout.text_for_list, words);
                                list_variant.setAdapter(adapter);
                                // присваиваем адаптер списку
                                list_variant.setOnTouchListener(new View.OnTouchListener() {
                                    // Setting on Touch Listener for handling the touch inside ScrollView
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        // Disallow the touch request for parent scroll on touch of child view
                                        v.getParent().requestDisallowInterceptTouchEvent(true);
                                        return false;
                                    }
                                });
                                list_variant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        search_variant.setVisibility(View.GONE);
                                        try {
                                            Log.d("myLogs", "in ");
                                            JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/search?salt=df90sdfgl9854gjs54os59gjsogsdf&search=" + words[i]));
                                            Gson gson = new Gson();
                                            audios = gson.fromJson(data.toString(), new TypeToken<List<Audio>>() {
                                            }.getType());

                                            if (audios != null) {
                                                choiceItemFromCatalogs.clear();
                                                for (int j = 0; j < audios.size(); j++) {
                                                    Audio audio = audios.get(j);
                                                    choiceItemFromCatalogs.add(audio);
                                                }
                                                choiceListAdapter.notifyDataSetChanged();


                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            else {
                                search_variant.setVisibility(View.GONE);
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    search_variant.setVisibility(View.GONE);
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editor.putString("idbook", choiceItemFromCatalogs.get(i).getId()).apply();
                Log.d("MyLog", " " + choiceItemFromCatalogs.get(i).getId());
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
                    .replace(R.id.main_frame, fragment).commit();
            editor.clear().apply();
        }

    }


}
