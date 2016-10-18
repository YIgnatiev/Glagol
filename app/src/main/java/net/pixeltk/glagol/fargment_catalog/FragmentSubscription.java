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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
 * Created by root on 18.10.16.
 */

public class FragmentSubscription extends Fragment implements OnBackPressedListener{

    public FragmentSubscription() {
        // Required empty public constructor
    }

    Fragment fragment = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String id, collection, subscribe_collection;
    Button subscription, cancel_subscription, my_news;
    LinearLayout news_line;
    getHttpGet request = new getHttpGet();
    ListView listView;
    private ArrayList<Audio> listcheck = new ArrayList<>();
    private ArrayList<Audio> audios = new ArrayList<>();
    private ChoiceListAdapter choiceListAdapter;
    private ArrayList<Audio> choiceItemFromCatalogs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);

        sharedPreferences = getActivity().getSharedPreferences("Subscription", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        listView = (ListView) view.findViewById(R.id.list_choice_item);

        subscription = (Button) view.findViewById(R.id.subscription);
        my_news = (Button) view.findViewById(R.id.my_news);
        cancel_subscription = (Button) view.findViewById(R.id.cancel_subscription);

        news_line = (LinearLayout) view.findViewById(R.id.news_line);

        choiceItemFromCatalogs = new ArrayList<Audio>();

        if (sharedPreferences.contains("Author"))
        {
            collection = "author";
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            try {
                Log.d("myLogs","in ");
                JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/authors?salt=df90sdfgl9854gjs54os59gjsogsdf"));
                Gson gson = new Gson();
                listcheck = gson.fromJson(data.toString(),  new TypeToken<List<Audio>>(){}.getType());

                if (listcheck!= null) {

                    for (int i=0; i<listcheck.size(); i++)
                    {
                        if (listcheck.get(i).getName_book().equals(sharedPreferences.getString("Author", ""))) {
                            id = listcheck.get(i).getId();
                            subscribe_collection = listcheck.get(i).getName_book();
                            if (sharedPreferences.contains(subscribe_collection))
                            {
                                subscription.setVisibility(View.INVISIBLE);
                                news_line.setVisibility(View.VISIBLE);
                            }
                            JSONArray data_selection = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getCollectionBooks?salt=df90sdfgl9854gjs54os59gjsogsdf&collection_id=" + id + "&model=author"));
                            Gson gson_selection = new Gson();
                            audios = gson_selection.fromJson(data_selection.toString(),  new TypeToken<List<Audio>>(){}.getType());

                            if (audios!= null) {

                                for (int j=0; j<audios.size(); j++)
                                {
                                    Audio audio = audios.get(j);
                                    choiceItemFromCatalogs.add(audio);
                                }

                            }
                            editor.remove("Author").apply();
                        }
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else  if (sharedPreferences.contains("Reader"))
        {
            collection = "reader";
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            try {
                Log.d("myLogs","in ");
                JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/readers?salt=df90sdfgl9854gjs54os59gjsogsdf"));
                Gson gson = new Gson();
                listcheck = gson.fromJson(data.toString(),  new TypeToken<List<Audio>>(){}.getType());

                if (listcheck!= null) {

                    for (int i=0; i<listcheck.size(); i++)
                    {
                        if (listcheck.get(i).getName_book().equals(sharedPreferences.getString("Reader", ""))) {
                            id = listcheck.get(i).getId();
                            subscribe_collection = listcheck.get(i).getName_book();
                            Log.d("MyLog", String.valueOf(sharedPreferences.contains(subscribe_collection)));
                            if (sharedPreferences.contains(subscribe_collection))
                            {
                                subscription.setVisibility(View.INVISIBLE);
                                news_line.setVisibility(View.VISIBLE);
                            }
                            JSONArray data_selection = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getCollectionBooks?salt=df90sdfgl9854gjs54os59gjsogsdf&collection_id=" + id + "&model=reader"));
                            Gson gson_selection = new Gson();
                            audios = gson_selection.fromJson(data_selection.toString(),  new TypeToken<List<Audio>>(){}.getType());

                            if (audios!= null) {

                                for (int j=0; j<audios.size(); j++)
                                {
                                    Audio audio = audios.get(j);
                                    choiceItemFromCatalogs.add(audio);
                                }

                            }
                            editor.remove("Reader").apply();
                        }
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else  if (sharedPreferences.contains("Publisher"))
        {
            collection = "publisher";
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            try {
                Log.d("myLogs","in ");
                JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/publishers?salt=df90sdfgl9854gjs54os59gjsogsdf"));
                Gson gson = new Gson();
                listcheck = gson.fromJson(data.toString(),  new TypeToken<List<Audio>>(){}.getType());

                if (listcheck!= null) {

                    for (int i=0; i<listcheck.size(); i++)
                    {
                        if (listcheck.get(i).getName_book().equals(sharedPreferences.getString("Publisher", ""))) {
                            id = listcheck.get(i).getId();
                            subscribe_collection = listcheck.get(i).getName_book();
                            if (sharedPreferences.contains(subscribe_collection))
                            {
                                subscription.setVisibility(View.INVISIBLE);
                                news_line.setVisibility(View.VISIBLE);
                            }
                            JSONArray data_selection = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getCollectionBooks?salt=df90sdfgl9854gjs54os59gjsogsdf&collection_id=" + id + "&model=publisher"));
                            Gson gson_selection = new Gson();
                            audios = gson_selection.fromJson(data_selection.toString(),  new TypeToken<List<Audio>>(){}.getType());

                            if (audios!= null) {

                                for (int j=0; j<audios.size(); j++)
                                {
                                    Audio audio = audios.get(j);
                                    choiceItemFromCatalogs.add(audio);
                                }

                            }
                            editor.remove("Publisher").apply();
                        }
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
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

        subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Sign", Context.MODE_PRIVATE);
                request.getHttpGet("http://www.glagolapp.ru/api/subscribe?salt=df90sdfgl9854gjs54os59gjsogsdf&collection_id=" + id + "&model=" + collection + "&user_id=" + sharedPreferences.getString("id", ""));
                editor.putString(subscribe_collection, subscribe_collection).apply();
                subscription.setVisibility(View.INVISIBLE);
                news_line.setVisibility(View.VISIBLE);



            }
        });

        cancel_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Sign", Context.MODE_PRIVATE);
                request.getHttpGet("http://www.glagolapp.ru/api/subscribe?salt=df90sdfgl9854gjs54os59gjsogsdf&collection_id=" + id + "&model=" + collection + "&user_id=" + sharedPreferences.getString("id", ""));
                editor.remove(subscribe_collection).apply();
                news_line.setVisibility(View.INVISIBLE);
                subscription.setVisibility(View.VISIBLE);

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
                            .replace(R.id.catalog_frame, fragment).commit();
                }
            }
        });

        return view;
    }
    @Override
    public void onBackPressed() {
        fragment = new CardBook();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.catalog_frame, fragment).commit();

        }

    }
}
