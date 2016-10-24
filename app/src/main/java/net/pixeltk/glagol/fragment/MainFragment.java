package net.pixeltk.glagol.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.MotionEvent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import net.pixeltk.glagol.R;
import net.pixeltk.glagol.activity.TabActivity;
import net.pixeltk.glagol.adapter.ChoiceListAdapter;
import net.pixeltk.glagol.adapter.RecyclerAdapter;
import net.pixeltk.glagol.adapter.RecyclerClickListener;
import net.pixeltk.glagol.api.Audio;
import net.pixeltk.glagol.api.getHttpGet;
import net.pixeltk.glagol.fargment_catalog.CardBook;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

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
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager, mnews, mtop_sale, msale, mchoice_editor, msoon_be;
    private ArrayList<Audio> itemData = new ArrayList<>();
    private ArrayList<Audio> audios = new ArrayList<>();
    private ArrayList<Audio> auto_compliet = new ArrayList<>();
    getHttpGet request = new getHttpGet();
    Fragment fragment = null;
    LinearLayout variant_frag, news_frag, search_variant;
    ImageView back_arrow;
    ListView list_variant;
    TextView variant_text, text_news, text_top_sale, text_sale, text_choice_editor, text_soon_be;
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
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        searchView=(SearchView) view.findViewById(R.id.searchView);
        searchView.setQueryHint("Поиск");
        searchView.setHovered(false);

        sharedPreferences = getActivity().getSharedPreferences("ClickMain", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        variant_frag = (LinearLayout) view.findViewById(R.id.variant_frag);
        news_frag = (LinearLayout) view.findViewById(R.id.news_frag);
        search_variant = (LinearLayout) view.findViewById(R.id.search_variant);

        list_variant = (ListView) view.findViewById(R.id.list_variant);


        my_news = (Button) view.findViewById(R.id.my_news);
        my_variant = (Button) view.findViewById(R.id.my_variant);

        back_arrow = (ImageView) view.findViewById(R.id.back);

        variant_text = (TextView) view.findViewById(R.id.variant);
        text_news = (TextView) view.findViewById(R.id.text_news);
        text_top_sale = (TextView) view.findViewById(R.id.text_top_sale);
        text_sale = (TextView) view.findViewById(R.id.text_sale);
        text_choice_editor = (TextView) view.findViewById(R.id.text_choice_editor);
        text_soon_be = (TextView) view.findViewById(R.id.text_soon_be);

        variant_text.setOnClickListener(this);
        text_news.setOnClickListener(this);
        text_top_sale.setOnClickListener(this);
        text_sale.setOnClickListener(this);
        text_choice_editor.setOnClickListener(this);
        text_soon_be.setOnClickListener(this);

        back_arrow.setVisibility(View.INVISIBLE);

        my_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_news.setTextColor(Color.parseColor("#000000"));
                my_variant.setTextColor(Color.parseColor("#F1F1F1"));
                my_news.setBackgroundResource(R.drawable.background);
                my_variant.setBackgroundResource(R.drawable.backsearch);
                news_frag.setVisibility(View.GONE);
                variant_frag.setVisibility(View.VISIBLE);
            }
        });
        my_variant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_news.setTextColor(Color.parseColor("#F1F1F1"));
                my_variant.setTextColor(Color.parseColor("#000000"));
                my_news.setBackgroundResource(R.drawable.backsearch);
                my_variant.setBackgroundResource(R.drawable.background);
                variant_frag.setVisibility(View.GONE);
                news_frag.setVisibility(View.VISIBLE);
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
                    Audio audio = audios.get(i);
                    itemData.add(audio);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter = new RecyclerAdapter(getActivity(), itemData);

        variant.setAdapter(mAdapter);
        news.setAdapter(mAdapter);
        top_sale.setAdapter(mAdapter);
        sale.setAdapter(mAdapter);
        choice_editor.setAdapter(mAdapter);
        soon_be.setAdapter(mAdapter);

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
                        Log.d("MyLog", auto_compliet.toString());
                        if (auto_compliet != null) {
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

        variant.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
              clickCard(position);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        news.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                clickCard(position);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        top_sale.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                clickCard(position);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        sale.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                clickCard(position);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        choice_editor.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                clickCard(position);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        soon_be.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                clickCard(position);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        return view;
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
                clickVariant(variant_text.getText().toString());
                return;
            case R.id.text_news:
                clickVariant(text_news.getText().toString());
                return;
            case R.id.text_top_sale:
                clickVariant(text_top_sale.getText().toString());
                return;
            case R.id.text_sale:
                clickVariant(text_sale.getText().toString());
                return;
            case R.id.text_choice_editor:
                clickVariant(text_choice_editor.getText().toString());
                return;
            case R.id.text_soon_be:
                clickVariant(text_soon_be.getText().toString());
                return;
        }

    }
}
