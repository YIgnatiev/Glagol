package net.pixeltk.glagol.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import net.pixeltk.glagol.R;
import net.pixeltk.glagol.Splash;
import net.pixeltk.glagol.activity.TabActivity;
import net.pixeltk.glagol.adapter.ChoiceListAdapter;
import net.pixeltk.glagol.adapter.RecyclerAdapter;
import net.pixeltk.glagol.adapter.RecyclerAdapterVariant;
import net.pixeltk.glagol.adapter.RecyclerClickListener;
import net.pixeltk.glagol.api.Audio;
import net.pixeltk.glagol.api.getHttpGet;
import net.pixeltk.glagol.fargment_catalog.CardBook;
import net.pixeltk.glagol.fargment_catalog.FragmentSubscription;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by root on 04.10.16.
 */

public class MainFragment extends Fragment implements View.OnClickListener {

    public MainFragment() {
        // Required empty public constructor
    }

    SearchView searchView;
    Button my_news, my_variant;
    private RecyclerView variant, news, top_sale, sale, choice_editor, soon_be;
    private RecyclerView.Adapter mAdapter, mAdapterVariant;
    private RecyclerView.LayoutManager mLayoutManager, mnews, mtop_sale, msale, mchoice_editor, msoon_be;
    private ArrayList<Audio> itemData = new ArrayList<>();
    private ArrayList<Audio> itemDataVariant = new ArrayList<>();
    private ArrayList<Audio> audios = new ArrayList<>();
    private ArrayList<Audio> checkSelection = new ArrayList<>();
    private ArrayList<Audio> audiosVariant = new ArrayList<>();
    private ArrayList<Audio> audiosSelection = new ArrayList<>();
    private ArrayList<Audio> auto_compliet = new ArrayList<>();

    private ChoiceListAdapter newsListAdapter;
    private ArrayList<Audio> newsList = new ArrayList<>();

    getHttpGet request = new getHttpGet();
    Fragment fragment = null;
    LinearLayout variant_frag, news_frag, search_variant, main_frag_part;
    ImageView back_arrow, news_point;
    ListView list_variant, list_selection, list_news;
    TextView variant_text, text_news, text_top_sale, text_sale, text_choice_editor, text_soon_be, log;
    TextView not_login_selection, not_login_news;
    SharedPreferences sharedPreferences, sig, subscription;
    SharedPreferences.Editor editor, edit_sign, editorsubscription;

    String dateSubscription, dateBook;
    Date dateSub, dateB;
    ProgressDialog pdia;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        searchView=(SearchView) view.findViewById(R.id.searchView);
        searchView.setQueryHint("Поиск");
        searchView.setFocusable(false);
        searchView.clearFocus();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        sharedPreferences = getActivity().getSharedPreferences("ClickMain", Context.MODE_PRIVATE);
        subscription = getActivity().getSharedPreferences("Subscription", Context.MODE_PRIVATE);
        sig = getActivity().getSharedPreferences("Sign", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        edit_sign = sig.edit();
        editorsubscription = subscription.edit();


        variant_frag = (LinearLayout) view.findViewById(R.id.variant_frag);
        news_frag = (LinearLayout) view.findViewById(R.id.news_frag);
        main_frag_part = (LinearLayout) view.findViewById(R.id.main_frag_part);
        search_variant = (LinearLayout) view.findViewById(R.id.search_variant);

        list_variant = (ListView) view.findViewById(R.id.list_variant);
        list_selection = (ListView) view.findViewById(R.id.variants);
        list_news = (ListView) view.findViewById(R.id.news);

        new NewsTask().execute();

        my_news = (Button) view.findViewById(R.id.my_news);
        my_variant = (Button) view.findViewById(R.id.my_variant);

        back_arrow = (ImageView) view.findViewById(R.id.back);
        news_point = (ImageView) view.findViewById(R.id.news_point);

        variant_text = (TextView) view.findViewById(R.id.variant);
        text_news = (TextView) view.findViewById(R.id.text_news);
        text_top_sale = (TextView) view.findViewById(R.id.text_top_sale);
        text_sale = (TextView) view.findViewById(R.id.text_sale);
        text_choice_editor = (TextView) view.findViewById(R.id.text_choice_editor);
        text_soon_be = (TextView) view.findViewById(R.id.text_soon_be);
        log = (TextView) view.findViewById(R.id.log);
        not_login_selection = (TextView) view.findViewById(R.id.not_login_selection);
        not_login_news = (TextView) view.findViewById(R.id.not_login_news);

        variant_text.setOnClickListener(this);
        text_news.setOnClickListener(this);
        text_top_sale.setOnClickListener(this);
        text_sale.setOnClickListener(this);
        text_choice_editor.setOnClickListener(this);
        text_soon_be.setOnClickListener(this);

        back_arrow.setVisibility(View.INVISIBLE);

        if (sig.contains("login"))
        {
            not_login_selection.setVisibility(View.GONE);
            not_login_news.setVisibility(View.GONE);
            list_news.setVisibility(View.VISIBLE);
            list_selection.setVisibility(View.VISIBLE);
            log.setVisibility(View.VISIBLE);
            log.setText(sig.getString("login", ""));
        }

        if (sig.getInt("News", 0) < newsList.size())
        {
            news_point.setVisibility(View.VISIBLE);
        }

        my_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_news.setTextColor(Color.parseColor("#000000"));
                my_variant.setTextColor(Color.parseColor("#F1F1F1"));
                my_news.setBackgroundResource(R.drawable.background);
                my_variant.setBackgroundResource(R.drawable.backsearch);

                news_frag.setVisibility(View.VISIBLE);
                main_frag_part.setVisibility(View.GONE);
                variant_frag.setVisibility(View.GONE);
                news_point.setVisibility(View.INVISIBLE);
                if (newsList.size() == 0)
                {
                    list_news.setVisibility(View.GONE);
                    not_login_news.setVisibility(View.VISIBLE);
                    Toast toast = Toast.makeText(getActivity(),
                            "По вашим подпискам новостей нет!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                list_news.setAdapter(newsListAdapter);
                list_news.setOnTouchListener(new View.OnTouchListener() {
                    // Setting on Touch Listener for handling the touch inside ScrollView
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Disallow the touch request for parent scroll on touch of child view
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });
                list_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Category", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("idbook", newsList.get(i).getId());
                        editor.putString("intent", "Main");
                        editor.apply();
                        fragment = new CardBook();
                        if (fragment != null) {
                            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_frame, fragment).commit();
                        }
                    }
                });
            }
        });
        my_variant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_news.setTextColor(Color.parseColor("#F1F1F1"));
                my_variant.setTextColor(Color.parseColor("#000000"));
                my_news.setBackgroundResource(R.drawable.backsearch);
                my_variant.setBackgroundResource(R.drawable.background);

                variant_frag.setVisibility(View.VISIBLE);
                main_frag_part.setVisibility(View.GONE);
                news_frag.setVisibility(View.GONE);

                if (sig.contains("id")) {

                    if (android.os.Build.VERSION.SDK_INT > 9) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                    }
                    try {
                        JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getUserSubscribe?salt=df90sdfgl9854gjs54os59gjsogsdf&user_id=" + sig.getString("id", "")));
                        if (data.toString().equals("[{}]")) {
                            list_selection.setVisibility(View.GONE);
                            not_login_selection.setVisibility(View.VISIBLE);
                        } else {
                            Gson gson = new Gson();
                            audiosSelection = gson.fromJson(data.toString(), new TypeToken<List<Audio>>() {
                            }.getType());


                            if (audiosSelection != null) {

                                final ArrayList<Map<String, Object>> selection = new ArrayList<Map<String, Object>>(audiosSelection.size());
                                Map<String, Object> m;

                                String[] from = {"text"};
                                // массив ID View-компонентов, в которые будут вставлять данные
                                int[] to = {R.id.name_part};


                                for (int i = 0; i < audiosSelection.size(); i++) {
                                    Audio audio = audiosSelection.get(i);
                                    m = new HashMap<String, Object>();
                                    if (audio.getModel().equals("author")) {
                                        checkSelection.clear();
                                        JSONArray dataauthor = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/authors?salt=df90sdfgl9854gjs54os59gjsogsdf"));
                                        Gson gsonauthor = new Gson();
                                        checkSelection = gsonauthor.fromJson(dataauthor.toString(), new TypeToken<List<Audio>>() {
                                        }.getType());

                                        if (checkSelection != null) {

                                            for (int j = 0; j < checkSelection.size(); j++) {
                                                if (audio.getModel_id().equals(checkSelection.get(j).getId())) {
                                                    m.put("text", checkSelection.get(j).getName_book() + " - Автор");
                                                    selection.add(m);
                                                }
                                            }

                                        }
                                    }
                                    if (audio.getModel().equals("reader")) {
                                        checkSelection.clear();
                                        JSONArray datareader = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/readers?salt=df90sdfgl9854gjs54os59gjsogsdf"));
                                        Gson gsonreader = new Gson();
                                        checkSelection = gsonreader.fromJson(datareader.toString(), new TypeToken<List<Audio>>() {
                                        }.getType());

                                        if (checkSelection != null) {

                                            for (int j = 0; j < checkSelection.size(); j++) {
                                                if (audio.getModel_id().equals(checkSelection.get(j).getId())) {
                                                    m.put("text", checkSelection.get(j).getName_book() + " - Чтец");
                                                    selection.add(m);
                                                }
                                            }

                                        }
                                    }

                                    if (audio.getModel().equals("collection")) {
                                        checkSelection.clear();
                                        JSONArray datacollection = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/customCollections?salt=df90sdfgl9854gjs54os59gjsogsdf"));
                                        Gson gsoncollection = new Gson();
                                        checkSelection = gsoncollection.fromJson(datacollection.toString(), new TypeToken<List<Audio>>() {
                                        }.getType());

                                        if (checkSelection != null) {

                                            for (int j = 0; j < checkSelection.size(); j++) {
                                                if (audio.getModel_id().equals(checkSelection.get(j).getId())) {
                                                    m.put("text", checkSelection.get(j).getName_book() + " - Подборка");
                                                    selection.add(m);
                                                }
                                            }

                                        }
                                    }
                                    if (audio.getModel().equals("publisher")) {
                                        checkSelection.clear();
                                        JSONArray datapublisher = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/publishers?salt=df90sdfgl9854gjs54os59gjsogsdf"));
                                        Gson gsonpublisher = new Gson();
                                        checkSelection = gsonpublisher.fromJson(datapublisher.toString(), new TypeToken<List<Audio>>() {
                                        }.getType());

                                        if (checkSelection != null) {

                                            for (int j = 0; j < checkSelection.size(); j++) {
                                                if (audio.getModel_id().equals(checkSelection.get(j).getId())) {
                                                    m.put("text", checkSelection.get(j).getName_book() + " - Издательство");
                                                    selection.add(m);
                                                }
                                            }

                                        }
                                    }
                                }


                                SimpleAdapter sAdapter = new SimpleAdapter(getActivity(), selection, R.layout.item_for_playlist,
                                        from, to);
                                list_selection.setAdapter(sAdapter);
                                list_selection.setOnTouchListener(new View.OnTouchListener() {
                                    // Setting on Touch Listener for handling the touch inside ScrollView
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        // Disallow the touch request for parent scroll on touch of child view
                                        v.getParent().requestDisallowInterceptTouchEvent(true);
                                        return false;
                                    }
                                });
                                list_selection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String[] name_sel = selection.get(i).get("text").toString().split("-");

                                        editorsubscription.putString("name_sel", name_sel[0].trim());
                                        editorsubscription.putString("nameCollection", audiosSelection.get(i).getModel());
                                        editorsubscription.putString("put_selection", audiosSelection.get(i).getModel_id()).apply();
                                        editor.apply();
                                        fragment = new FragmentSubscription();
                                        if (fragment != null) {
                                            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                            fragmentManager.beginTransaction()
                                                    .replace(R.id.main_frame, fragment).commit();
                                        }
                                    }
                                });

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(getActivity(),
                            "Вы не зарегистрированы, либо не вошли в свой аккаунт!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        variant = (RecyclerView) view.findViewById(R.id.recycler_view_variant);
        news = (RecyclerView) view.findViewById(R.id.recycler_view_news);
        top_sale = (RecyclerView) view.findViewById(R.id.recycler_view_top_sale);
        sale = (RecyclerView) view.findViewById(R.id.recycler_view_sale);
        choice_editor = (RecyclerView) view.findViewById(R.id.recycler_view_choice_editor);
        soon_be = (RecyclerView) view.findViewById(R.id.recycler_view_soon_be);

        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        variant.setHasFixedSize(true);
        news.setHasFixedSize(true);
        top_sale.setHasFixedSize(true);
        sale.setHasFixedSize(true);
        choice_editor.setHasFixedSize(true);
        soon_be.setHasFixedSize(true);

        // используем linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mnews = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mtop_sale = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        msale = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mchoice_editor = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        msoon_be = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        variant.setLayoutManager(mLayoutManager);
        news.setLayoutManager(mnews);
        top_sale.setLayoutManager(mtop_sale);
        sale.setLayoutManager(msale);
        choice_editor.setLayoutManager(mchoice_editor);
        soon_be.setLayoutManager(msoon_be);
        itemData.clear();
        // создаем адаптер

        new getVariant().execute();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.trim();
                if (newText.toCharArray().length > 2) {
                    try {
                        JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/autocomplete?salt=df90sdfgl9854gjs54os59gjsogsdf&search=" + newText));
                        if (data.toString().equals("[{}]"))
                        {
                            search_variant.setVisibility(View.GONE);
                            Toast toast = Toast.makeText(getActivity(),
                                    "Совпадений не обнаружено, повторте запрос!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else {
                            search_variant.setVisibility(View.VISIBLE);
                            Gson gson = new Gson();
                            auto_compliet = gson.fromJson(data.toString(), new TypeToken<List<Audio>>() {
                            }.getType());
                            Log.d("MyLog", auto_compliet.toString());
                            if (auto_compliet != null) {
                                final String words[] = new String[auto_compliet.size()];
                                for (int i = 0; i < auto_compliet.size(); i++) {
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
                                            SharedPreferences search_line = getActivity().getSharedPreferences("Search", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor serch_edit = search_line.edit();
                                            serch_edit.putString("search", words[i]).apply();
                                            fragment = new ListSearchBook();
                                            if (fragment != null) {
                                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.main_frame, fragment).commit();
                                            }
                                        }
                                    });
                                } else {
                                    search_variant.setVisibility(View.GONE);
                                }
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

        variant.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                itemView.setBackgroundColor(getResources().getColor(R.color.back));
                clickCardVariant(position);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        news.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                itemView.setBackgroundColor(getResources().getColor(R.color.back));
                clickCard(position);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        top_sale.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                itemView.setBackgroundColor(getResources().getColor(R.color.back));
                clickCard(position);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        sale.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                itemView.setBackgroundColor(getResources().getColor(R.color.back));
                clickCard(position);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        choice_editor.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                itemView.setBackgroundColor(getResources().getColor(R.color.back));
                clickCard(position);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        soon_be.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                itemView.setBackgroundColor(getResources().getColor(R.color.back));
                clickCard(position);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        return view;
    }
    class getVariant extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Загрузка...");
            pdia.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.d("myLogs","in ");
                JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/newbooks?salt=df90sdfgl9854gjs54os59gjsogsdf"));
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
            try {
                Log.d("myLogs","in ");
                JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/customCollections?salt=df90sdfgl9854gjs54os59gjsogsdf"));
                Gson gson = new Gson();
                audiosVariant = gson.fromJson(data.toString(),  new TypeToken<List<Audio>>(){}.getType());

                if (audiosVariant!= null) {

                    for (int i=0; i<audiosVariant.size(); i++)
                    {
                        Audio audio = audiosVariant.get(i);
                        itemDataVariant.add(audio);
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
            mAdapter = new RecyclerAdapter(getActivity(), itemData);
            mAdapterVariant = new RecyclerAdapterVariant(getActivity(), itemDataVariant);

            variant.setAdapter(mAdapterVariant);
            news.setAdapter(mAdapter);
            top_sale.setAdapter(mAdapter);
            sale.setAdapter(mAdapter);
            choice_editor.setAdapter(mAdapter);
            soon_be.setAdapter(mAdapter);

            pdia.dismiss();
        }
    }
    public void clickVariant(String text)
    {
        editor.putString("var", text).apply();
        fragment = new ClickOnMainPart();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame, fragment).commit();
        }
    }
    public void clickTextVariant(String text)
    {
        editor.putString("var", text).apply();
        fragment = new Variant();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame, fragment).commit();
        }
    }
    public void clickCardVariant(int position)
    {
        editorsubscription.putString("nameCollection", itemDataVariant.get(position).getName_book());
        editorsubscription.putString("collection", itemDataVariant.get(position).getId()).apply();
        editor.apply();
        fragment = new FragmentSubscription();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame, fragment).commit();
        }
    }
    public void clickCard(int position)
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Category", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idbook", itemData.get(position).getId());
        editor.putString("intent", "Main");
        editor.apply();
        fragment = new CardBook();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame, fragment).commit();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.variant:
                variant_text.setPaintFlags(variant_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                clickTextVariant(variant_text.getText().toString());
                return;
            case R.id.text_news:
                text_news.setPaintFlags(text_news.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                clickVariant(text_news.getText().toString());
                return;
            case R.id.text_top_sale:
                text_top_sale.setPaintFlags(text_top_sale.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                clickVariant(text_top_sale.getText().toString());
                return;
            case R.id.text_sale:
                text_sale.setPaintFlags(text_sale.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                clickVariant(text_sale.getText().toString());
                return;
            case R.id.text_choice_editor:
                text_choice_editor.setPaintFlags(text_choice_editor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                clickVariant(text_choice_editor.getText().toString());
                return;
            case R.id.text_soon_be:
                text_soon_be.setPaintFlags(text_soon_be.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                clickVariant(text_soon_be.getText().toString());
                return;
        }

    }
    class NewsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            if (sig.contains("id")) {
                try {
                    JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getUserSubscribe?salt=df90sdfgl9854gjs54os59gjsogsdf&user_id=" + sig.getString("id", "")));
                    if (data.toString().equals("[{}]")) {

                    } else {
                        Gson gson = new Gson();
                        audiosSelection = gson.fromJson(data.toString(), new TypeToken<List<Audio>>() {
                        }.getType());

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");

                        if (audiosSelection != null) {
                            for (int i = 0; i < audiosSelection.size(); i++) {
                                Audio audio = audiosSelection.get(i);

                                dateSubscription = audiosSelection.get(i).getCreated_at();
                                dateSubscription = dateSubscription.replace("-", ".");
                                try {
                                    dateSub = dateFormat.parse(dateSubscription);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                JSONArray data_selection = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getCollectionBooks?salt=df90sdfgl9854gjs54os59gjsogsdf&collection_id=" + audio.getModel_id() + "&model=" + audio.getModel()));
                                Gson gson_selection = new Gson();
                                audios = gson_selection.fromJson(data_selection.toString(), new TypeToken<List<Audio>>() {
                                }.getType());

                                if (audios != null) {

                                    for (int j = audios.size() - 1; j >= 0; j--) {
                                        dateBook = audios.get(j).getCreated_at();
                                        dateBook = dateBook.replace("-", ".");
                                        try {
                                            dateB = dateFormat.parse(dateBook);
                                        } catch (ParseException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        if (dateSub.compareTo(dateB) == -1) {
                                            newsList.add(audios.get(j));
                                        }
                                    }
                                    audios = null;

                                }
                            }
                            edit_sign.putInt("News", newsList.size()).apply();
                            newsListAdapter = new ChoiceListAdapter(getActivity(), newsList);
                        }
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
            else
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

}
