package net.pixeltk.glagol.fargment_catalog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.Splash;
import net.pixeltk.glagol.activity.TabActivity;
import net.pixeltk.glagol.adapter.BookMarksHelper;
import net.pixeltk.glagol.adapter.ChoiceListAdapter;
import net.pixeltk.glagol.adapter.DataBasesHelper;
import net.pixeltk.glagol.api.Audio;
import net.pixeltk.glagol.api.getHttpGet;
import net.pixeltk.glagol.fragment.MainFragment;
import net.pixeltk.glagol.fragment.Variant;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    String id, collection, subscribe_collection, author, publisher, reader, name_collection;
    Button subscription, cancel_subscription;
    getHttpGet request = new getHttpGet();
    ListView listView;
    private ArrayList<Audio> listcheck = new ArrayList<>();
    private ArrayList<Audio> audios = new ArrayList<>();
    private ChoiceListAdapter choiceListAdapter;
    private ArrayList<Audio> choiceItemFromCatalogs;
    ImageView back_arrow, logo;
    TextView name_frag;
    ProgressDialog pdia;

    DataBasesHelper dataBackPressed;
    BookMarksHelper backPressedHelper;
    ArrayList BackPressedId = new ArrayList();

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

        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        name_frag = (TextView) view.findViewById(R.id.name_frag);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_arrow.setBackgroundResource(R.drawable.bakground_arrow);
                onBackPressed();
            }
        });

        listView = (ListView) view.findViewById(R.id.list_choice_item);

        subscription = (Button) view.findViewById(R.id.subscription);
        cancel_subscription = (Button) view.findViewById(R.id.cancel_subscription);

        dataBackPressed = new DataBasesHelper(getActivity());

        BackPressedId = dataBackPressed.getIdBackPressed();

        int last_element = BackPressedId.size() - 1;
        Log.d("MyLog", "click ok");
        backPressedHelper = dataBackPressed.getBackPressed(BackPressedId.get(last_element).toString());
        final String[] someInformation = backPressedHelper.getSome_info().split(",");
        collection = someInformation[1];

        Log.d("MyLog", "colection " + someInformation[1] + " name " + someInformation[2]);


        choiceItemFromCatalogs = new ArrayList<Audio>();

        if (collection.equals("author"))
        {
            author = someInformation[2];
            name_frag.setText(author);
            new getBooksAuthor().execute();

        }
        else  if (collection.equals("reader"))
        {
            reader = someInformation[2];
            name_frag.setText(reader);

            new getBooksReader().execute();

        }
        else  if (collection.equals("publisher"))
        {
            publisher = someInformation[2];
            name_frag.setText(publisher);
            new getBooksPublishers().execute();

        }
        else  if (collection.equals("collection"))
        {
            id = someInformation[2];
            name_collection = someInformation[0];
            name_frag.setText(name_collection);
            Log.d("MyLog", name_collection);
            subscribe_collection = name_collection;
            if (sharedPreferences.contains(subscribe_collection))
            {
                subscription.setVisibility(View.INVISIBLE);
                cancel_subscription.setVisibility(View.VISIBLE);
            }
            new getCollections().execute();

        }
        else
        {
            id = someInformation[2];
            name_collection = someInformation[0];

            name_frag.setText(name_collection);
            subscribe_collection = name_collection;
            if (sharedPreferences.contains(subscribe_collection))
            {
                subscription.setVisibility(View.INVISIBLE);
                cancel_subscription.setVisibility(View.VISIBLE);
            }
            new getNameSell().execute();
        }


        subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Sign", Context.MODE_PRIVATE);
                String user_id = sharedPreferences.getString("id", "");
                if (!user_id.equals("")) {
                    request.getHttpGet("http://www.glagolapp.ru/api/subscribe?salt=df90sdfgl9854gjs54os59gjsogsdf&collection_id=" + id + "&model=" + collection + "&user_id=" + user_id);
                    editor.putString(subscribe_collection, subscribe_collection).apply();
                    subscription.setVisibility(View.INVISIBLE);
                    cancel_subscription.setVisibility(View.VISIBLE);
                    switch (collection)
                    {
                        case "author":
                            Toast.makeText(getActivity(), "Вы подписаны на  новинки автора - " + author +"!", Toast.LENGTH_LONG).show();
                            return;
                        case "reader":
                            Toast.makeText(getActivity(), "Вы подписаны на  новинки чтеца - " + reader + "!", Toast.LENGTH_LONG).show();
                            return;
                        case "publisher":
                            Toast.makeText(getActivity(), "Вы подписаны на  новинки издательства - " + publisher + "!", Toast.LENGTH_LONG).show();
                            return;
                        case "collection":
                            Toast.makeText(getActivity(), "Вы подписаны на  новинки издательства - " + name_collection + "!", Toast.LENGTH_LONG).show();
                            return;
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Вы не вошли или не зарегистрировались!", Toast.LENGTH_LONG).show();
                }


            }
        });

        cancel_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Sign", Context.MODE_PRIVATE);
                request.getHttpGet("http://www.glagolapp.ru/api/subscribe?salt=df90sdfgl9854gjs54os59gjsogsdf&collection_id=" + id + "&model=" + collection + "&user_id=" + sharedPreferences.getString("id", "") + "&remove=1");
                editor.remove(subscribe_collection).apply();
                cancel_subscription.setVisibility(View.INVISIBLE);
                subscription.setVisibility(View.VISIBLE);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dataBackPressed.insertBackPressed("FragmentSubscription", choiceItemFromCatalogs.get(i).getId() + "," + collection + "," + someInformation[2]);
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

        int i = BackPressedId.size() - 1;
        Log.d("MyLog", "click ok");
        backPressedHelper = dataBackPressed.getBackPressed(BackPressedId.get(i).toString());
        Log.d("MyLog", "click ok" + backPressedHelper.getClass_name());

        if (backPressedHelper.getClass_name().equals("CardBook"))
        {
            fragment = new CardBook();

            SQLiteDatabase db = dataBackPressed.getWritableDatabase();
            db.delete("BackPressed", "id = " + BackPressedId.get(i), null);
            db.close();
        }
        else  if (backPressedHelper.getClass_name().equals("MainFragment"))
        {
            fragment = new MainFragment();
            SQLiteDatabase db = dataBackPressed.getWritableDatabase();
            db.delete("BackPressed", "id = " + BackPressedId.get(i), null);
            db.close();
        }
        else  if (backPressedHelper.getClass_name().equals("Variant"))
        {
            fragment = new Variant();
            SQLiteDatabase db = dataBackPressed.getWritableDatabase();
            db.delete("BackPressed", "id = " + BackPressedId.get(i), null);
            db.close();
        }

        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame, fragment).commit();

        }

    }

    class getNameSell extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Загрузка...");
            pdia.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            try {
                JSONArray data_selection = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getCollectionBooks?salt=df90sdfgl9854gjs54os59gjsogsdf&collection_id=" + id + "&model=" + collection));
                Gson gson_selection = new Gson();
                audios = gson_selection.fromJson(data_selection.toString(),  new TypeToken<List<Audio>>(){}.getType());

                if (audios!= null) {

                    for (int j = 0; j < audios.size(); j++) {
                        Audio audio = audios.get(j);
                        choiceItemFromCatalogs.add(audio);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (choiceItemFromCatalogs.size()==0)
            {
                Toast.makeText(getActivity(), "Список пуст!", Toast.LENGTH_LONG).show();
            }
            else
            {
                choiceListAdapter = new ChoiceListAdapter(getActivity(), choiceItemFromCatalogs);
                listView.setAdapter(choiceListAdapter);
            }
            pdia.dismiss();
        }
    }
    class getCollections extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Загрузка...");
            pdia.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            try {
                JSONArray data_selection = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getCollectionBooks?salt=df90sdfgl9854gjs54os59gjsogsdf&collection_id=" + id + "&model=collection"));
                Gson gson_selection = new Gson();
                audios = gson_selection.fromJson(data_selection.toString(),  new TypeToken<List<Audio>>(){}.getType());

                if (audios!= null) {

                    for (int j = 0; j < audios.size(); j++) {
                        Audio audio = audios.get(j);
                        choiceItemFromCatalogs.add(audio);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (choiceItemFromCatalogs.size()==0)
            {
                Toast.makeText(getActivity(), "Список пуст!", Toast.LENGTH_LONG).show();
            }
            else
            {
                choiceListAdapter = new ChoiceListAdapter(getActivity(), choiceItemFromCatalogs);
                listView.setAdapter(choiceListAdapter);
            }
            pdia.dismiss();
        }
    }

    class getBooksAuthor extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Загрузка...");
            pdia.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
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
                        if (listcheck.get(i).getName_book().equals(author)) {
                            id = listcheck.get(i).getId();
                            subscribe_collection = listcheck.get(i).getName_book();
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
                        }
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (sharedPreferences.contains(subscribe_collection))
            {
                subscription.setVisibility(View.INVISIBLE);
                cancel_subscription.setVisibility(View.VISIBLE);
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
            pdia.dismiss();

        }
    }
    class getBooksReader extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Загрузка...");
            pdia.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
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
                        if (listcheck.get(i).getName_book().equals(reader)) {
                            id = listcheck.get(i).getId();
                            subscribe_collection = listcheck.get(i).getName_book();

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
                        }
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (sharedPreferences.contains(subscribe_collection))
            {
                subscription.setVisibility(View.INVISIBLE);
                cancel_subscription.setVisibility(View.VISIBLE);
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
            pdia.dismiss();
        }
    }
    class getBooksPublishers extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Загрузка...");
            pdia.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
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
                        if (listcheck.get(i).getName_book().equals(publisher)) {
                            id = listcheck.get(i).getId();
                            subscribe_collection = listcheck.get(i).getName_book();

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
                        }
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (sharedPreferences.contains(subscribe_collection))
            {
                subscription.setVisibility(View.INVISIBLE);
                cancel_subscription.setVisibility(View.VISIBLE);
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
            pdia.dismiss();

        }
    }
}
