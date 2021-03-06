package net.pixeltk.glagol.fragment_my_book;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.activity.TabActivity;
import net.pixeltk.glagol.adapter.BookMarksHelper;
import net.pixeltk.glagol.adapter.DataBasesHelper;
import net.pixeltk.glagol.adapter.DrawItemBookMarks;
import net.pixeltk.glagol.adapter.DrawerListBookMarks;
import net.pixeltk.glagol.adapter.DrawerListBuy;
import net.pixeltk.glagol.adapter.DrawerListHistory;
import net.pixeltk.glagol.adapter.DrawerListListening;
import net.pixeltk.glagol.api.Audio;
import net.pixeltk.glagol.api.getHttpGet;
import net.pixeltk.glagol.fargment_catalog.CardBook;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;
import net.pixeltk.glagol.fragment.MainFragment;
import net.pixeltk.glagol.fragment.PlayerFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static net.pixeltk.glagol.activity.TabActivity.hasConnection;

/**
 * Created by root on 07.10.16.
 */

public class SuccessfulEnter extends Fragment implements OnBackPressedListener{

    public SuccessfulEnter() {
        // Required empty public constructor
    }
    LinearLayout listen_incl, buy_incl, my_tab_incl, history_incl;
    Button listen, buy, my_tab, history;
    private ArrayList<DrawItemBookMarks> navDrawerItems;
    private ArrayList<DrawItemBookMarks> navDrawerHistoryItems;
    private ArrayList<DrawItemBookMarks> navDrawerBuyItems;
    private ArrayList<DrawItemBookMarks> navDrawerListenItems;
    public static DrawerListBookMarks adapter;
    private DrawerListHistory history_adapter;
    private DrawerListBuy buy_adapter;
    private DrawerListListening listen_adapter;
    ListView listView, listHistory, listBuy, listlisten;
    DataBasesHelper dataBookMarks, dataHistory, dataBuy, dataDownload, dataListen;
    BookMarksHelper bookMarksHelper, historyHelper, buyHelper, listenHelper;
    DataBasesHelper dataBasesHelper;
    ArrayList IdList = new ArrayList();
    ArrayList HistoryListId = new ArrayList();
    ArrayList BuyListId = new ArrayList();
    ArrayList ListenId = new ArrayList();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView name_frag;
    ImageView back_arrow, logo;
    public ArrayList<Audio> books = new ArrayList<>();
    public ArrayList<Audio> audios = new ArrayList<>();
    getHttpGet request = new getHttpGet();
    SharedPreferences checklogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_successful_enter, container, false);
        checklogin = getActivity().getSharedPreferences("Sign", Context.MODE_PRIVATE);

        listen_incl = (LinearLayout) view.findViewById(R.id.listen_incl);
        buy_incl = (LinearLayout) view.findViewById(R.id.buy_incl);
        my_tab_incl = (LinearLayout) view.findViewById(R.id.my_tab_incl);
        history_incl = (LinearLayout) view.findViewById(R.id.history_incl);

        listen = (Button) view.findViewById(R.id.listen);
        buy = (Button) view.findViewById(R.id.buy);
        my_tab = (Button) view.findViewById(R.id.my_tab);
        history = (Button) view.findViewById(R.id.history);

        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        name_frag = (TextView) view.findViewById(R.id.name_frag);
        name_frag.setText("Мои книги");

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                back_arrow.setBackgroundResource(R.drawable.bakground_arrow);
                onBackPressed();
            }
        });

        sharedPreferences = getActivity().getSharedPreferences("Category", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        listView = (ListView) view.findViewById(R.id.list_view);
        listHistory = (ListView) view.findViewById(R.id.list_history);
        listBuy = (ListView) view.findViewById(R.id.list_buy);
        listlisten = (ListView) view.findViewById(R.id.list_listen);

        navDrawerItems = new ArrayList<DrawItemBookMarks>();
        navDrawerHistoryItems = new ArrayList<DrawItemBookMarks>();
        navDrawerBuyItems = new ArrayList<DrawItemBookMarks>();
        navDrawerListenItems = new ArrayList<DrawItemBookMarks>();

        dataBookMarks = new DataBasesHelper(getActivity());
        dataHistory = new DataBasesHelper(getActivity());
        dataBuy = new DataBasesHelper(getActivity());
        dataListen = new DataBasesHelper(getActivity());
        dataBasesHelper = new DataBasesHelper(getActivity());

        IdList = dataBookMarks.getidRow();
        HistoryListId = dataHistory.getIdHistory();
        BuyListId = dataBuy.getIdBuy();
        ListenId = dataListen.getIdListen();

        navDrawerListenItems.clear();
        for (int i = 0; i < ListenId.size(); i++)
        {
            listenHelper = dataListen.getProductListen(ListenId.get(i).toString());
            navDrawerListenItems.add(new DrawItemBookMarks(listenHelper.getName_author(), listenHelper.getName_book(), listenHelper.getName_reader(), listenHelper.getNow_listening(), listenHelper.getImg_url(), listenHelper.getId_book()));
            listenHelper = null;
        }
        listen_adapter = new DrawerListListening(getActivity(), navDrawerListenItems);
        listlisten.setAdapter(listen_adapter);

        listlisten.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editor.putString("id_book", navDrawerListenItems.get(i).getId());
                editor.putString("book_name", navDrawerListenItems.get(i).getName_book());
                editor.putString("name_author", navDrawerListenItems.get(i).getName_author());
                editor.putString("intent", "open");
                editor.putString("url_img", navDrawerListenItems.get(i).getImg_url()).apply();
                editor.putString("intent", "Successful").apply();

                Fragment fragment = new PlayerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("open", "play");
                fragment.setArguments(bundle);
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, fragment).commit();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                dataBasesHelper.insertBackPressed("Successful", navDrawerItems.get(i).getId());
                dataBasesHelper.close();
                Fragment fragment = new CardBook();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, fragment).commit();
                }
            }
        });
        listBuy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (hasConnection(getActivity())) {
                    dataBasesHelper.insertBackPressed("Successful", navDrawerBuyItems.get(i).getId());
                    dataBasesHelper.close();
                    Fragment fragment = new CardBook();
                    if (fragment != null) {
                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_frame, fragment).commit();
                    }
                }
            }
        });
        listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                dataBasesHelper.insertBackPressed("Successful", navDrawerHistoryItems.get(i).getId());
                dataBasesHelper.close();
                Fragment fragment = new CardBook();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, fragment).commit();
                }
            }
        });





        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen.setTextColor(Color.parseColor("#000000"));
                buy.setTextColor(Color.parseColor("#F1F1F1"));
                my_tab.setTextColor(Color.parseColor("#F1F1F1"));
                history.setTextColor(Color.parseColor("#F1F1F1"));

                listen.setBackgroundResource(R.drawable.background);
                buy.setBackgroundResource(R.drawable.backsearch);
                my_tab.setBackgroundResource(R.drawable.backsearch);
                history.setBackgroundResource(R.drawable.backsearch);

                listen_incl.setVisibility(View.VISIBLE);
                buy_incl.setVisibility(View.GONE);
                my_tab_incl.setVisibility(View.GONE);
                history_incl.setVisibility(View.GONE);

                navDrawerListenItems.clear();
                for (int i = 0; i < ListenId.size(); i++)
                {
                    listenHelper = dataListen.getProductListen(ListenId.get(i).toString());
                    navDrawerListenItems.add(new DrawItemBookMarks(listenHelper.getName_author(), listenHelper.getName_book(), listenHelper.getName_reader(), listenHelper.getNow_listening(), listenHelper.getImg_url(), listenHelper.getId_book()));
                    listenHelper = null;
                }
                listen_adapter = new DrawerListListening(getActivity(), navDrawerListenItems);
                listlisten.setAdapter(listen_adapter);
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen.setTextColor(Color.parseColor("#F1F1F1"));
                buy.setTextColor(Color.parseColor("#000000"));
                my_tab.setTextColor(Color.parseColor("#F1F1F1"));
                history.setTextColor(Color.parseColor("#F1F1F1"));

                listen.setBackgroundResource(R.drawable.backsearch);
                buy.setBackgroundResource(R.drawable.background);
                my_tab.setBackgroundResource(R.drawable.backsearch);
                history.setBackgroundResource(R.drawable.backsearch);

                listen_incl.setVisibility(View.GONE);
                buy_incl.setVisibility(View.VISIBLE);
                my_tab_incl.setVisibility(View.GONE);
                history_incl.setVisibility(View.GONE);

                navDrawerBuyItems.clear();

                if (hasConnection(getActivity()))
                {
                    if (android.os.Build.VERSION.SDK_INT > 9) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                    }
                    try {
                        JSONArray dataBooksBuy = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/UserBooks/" + checklogin.getString("id", "") + "?salt=df90sdfgl9854gjs54os59gjsogsdf"));
                        if (!dataBooksBuy.toString().equals("[{}]")) {
                            Gson gsonBooksBuy = new Gson();
                            books = gsonBooksBuy.fromJson(dataBooksBuy.toString(), new TypeToken<List<Audio>>() {
                            }.getType());

                            if (books != null) {
                                if (BuyListId.size() < books.size()) {
                                    for (int i = 0; i < BuyListId.size(); i++) {
                                        SQLiteDatabase db = dataBuy.getWritableDatabase();
                                        db.delete("Buy", "id = " + BuyListId.get(i), null);
                                        db.close();
                                    }
                                    BuyListId.clear();
                                    for (int j = 0; j < books.size(); j++) {
                                        if (android.os.Build.VERSION.SDK_INT > 9) {
                                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                            StrictMode.setThreadPolicy(policy);
                                        }
                                        try {
                                            JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getbook?salt=df90sdfgl9854gjs54os59gjsogsdf&book_id=" + books.get(j).getBook_id()));

                                            Gson gson = new Gson();
                                            audios = gson.fromJson(data.toString(), new TypeToken<List<Audio>>() {
                                            }.getType());

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        dataBuy.insertBuy(audios.get(0).getName_authors(), audios.get(0).getName_book(), audios.get(0).getReaders(), audios.get(0).getPrice(), audios.get(0).getIcon(), audios.get(0).getId());
                                        audios.clear();
                                    }
                                    navDrawerBuyItems.clear();
                                    BuyListId = dataBuy.getIdBuy();
                                    for (int i = 0; i < books.size(); i++) {
                                        buyHelper = dataBuy.getProduct(BuyListId.get(i).toString(), "Buy");
                                        navDrawerBuyItems.add(new DrawItemBookMarks(buyHelper.getName_author(), buyHelper.getName_book(), buyHelper.getName_reader(), buyHelper.getPrice(), buyHelper.getImg_url(), buyHelper.getId_book()));
                                        buyHelper = null;
                                    }
                                } else {
                                    navDrawerBuyItems.clear();
                                    for (int i = 0; i < BuyListId.size(); i++) {
                                        Log.d("MyLog", " id " + BuyListId.get(i));
                                        buyHelper = dataBuy.getProduct(BuyListId.get(i).toString(), "Buy");
                                        navDrawerBuyItems.add(new DrawItemBookMarks(buyHelper.getName_author(), buyHelper.getName_book(), buyHelper.getName_reader(), buyHelper.getPrice(), buyHelper.getImg_url(), buyHelper.getId_book()));
                                        buyHelper = null;
                                    }
                                }
                            }

                        }
                    }catch(JSONException e){
                            e.printStackTrace();
                    }
                }
                else {
                    navDrawerBuyItems.clear();
                    for (int i = 0; i < BuyListId.size(); i++) {
                        Log.d("MyLog", " id " + BuyListId.get(i));
                        buyHelper = dataBuy.getProduct(BuyListId.get(i).toString(), "Buy");
                        navDrawerBuyItems.add(new DrawItemBookMarks(buyHelper.getName_author(), buyHelper.getName_book(), buyHelper.getName_reader(), buyHelper.getPrice(), buyHelper.getImg_url(), buyHelper.getId_book()));
                        buyHelper = null;
                    }
                }

                buy_adapter = new DrawerListBuy(getActivity(), navDrawerBuyItems);
                listBuy.setAdapter(buy_adapter);
            }
        });

        my_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen.setTextColor(Color.parseColor("#F1F1F1"));
                buy.setTextColor(Color.parseColor("#F1F1F1"));
                my_tab.setTextColor(Color.parseColor("#000000"));
                history.setTextColor(Color.parseColor("#F1F1F1"));

                listen.setBackgroundResource(R.drawable.backsearch);
                buy.setBackgroundResource(R.drawable.backsearch);
                my_tab.setBackgroundResource(R.drawable.background);
                history.setBackgroundResource(R.drawable.backsearch);

                listen_incl.setVisibility(View.GONE);
                buy_incl.setVisibility(View.GONE);
                my_tab_incl.setVisibility(View.VISIBLE);
                history_incl.setVisibility(View.GONE);

                navDrawerItems.clear();
                for (int i = 0; i < IdList.size(); i++)
                {
                    bookMarksHelper = dataBookMarks.getProduct(IdList.get(i).toString(), "Bookmarks");
                    navDrawerItems.add(new DrawItemBookMarks(bookMarksHelper.getName_author(), bookMarksHelper.getName_book(), bookMarksHelper.getName_reader(), bookMarksHelper.getPrice(), bookMarksHelper.getImg_url(), bookMarksHelper.getId_book()));
                    bookMarksHelper = null;
                }
                adapter = new DrawerListBookMarks(getActivity(), navDrawerItems);
                listView.setAdapter(adapter);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen.setTextColor(Color.parseColor("#F1F1F1"));
                buy.setTextColor(Color.parseColor("#F1F1F1"));
                my_tab.setTextColor(Color.parseColor("#F1F1F1"));
                history.setTextColor(Color.parseColor("#000000"));

                listen.setBackgroundResource(R.drawable.backsearch);
                buy.setBackgroundResource(R.drawable.backsearch);
                my_tab.setBackgroundResource(R.drawable.backsearch);
                history.setBackgroundResource(R.drawable.background);

                listen_incl.setVisibility(View.GONE);
                buy_incl.setVisibility(View.GONE);
                my_tab_incl.setVisibility(View.GONE);
                history_incl.setVisibility(View.VISIBLE);

                navDrawerHistoryItems.clear();
                for (int i = 0; i < HistoryListId.size(); i++)
                {

                    historyHelper = dataHistory.getProduct(HistoryListId.get(i).toString(), "History");
                    navDrawerHistoryItems.add(new DrawItemBookMarks(historyHelper.getName_author(), historyHelper.getName_book(), historyHelper.getName_reader(), historyHelper.getPrice(), historyHelper.getImg_url(), historyHelper.getId_book()));
                    bookMarksHelper = null;

                }

                history_adapter = new DrawerListHistory(getActivity(), navDrawerHistoryItems);
                listHistory.setAdapter(history_adapter);

            }
        });

        return view;
    }
    @Override
    public void onBackPressed() {
        Fragment fragment = new MainFragment();
        TabActivity tabActivity = new TabActivity();
        tabActivity.changePage();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame, fragment).commit();

        }


    }
    public static void dataChanged()
    {
        adapter.notifyDataSetChanged();
    }
}