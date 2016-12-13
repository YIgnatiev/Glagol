package net.pixeltk.glagol.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.adapter.BookMarksHelper;
import net.pixeltk.glagol.adapter.DataBasesHelper;
import net.pixeltk.glagol.api.Audio;
import net.pixeltk.glagol.api.getHttpGet;
import net.pixeltk.glagol.fargment_catalog.CardBook;
import net.pixeltk.glagol.fargment_catalog.ChoiceItemList;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yaroslav on 09.10.2016.
 */

public class FragmentPayment extends Fragment implements OnBackPressedListener{

    public FragmentPayment() {
        // Required empty public constructor
    }

    Button buy;
    Fragment fragment = null;
    SharedPreferences idbook, sharedPreferences;
    DataBasesHelper dataBuy, dataBookMarks;
    ArrayList BuiId = new ArrayList();
    ArrayList BookMarksId = new ArrayList();
    ImageView back_arrow, logo;
    TextView name_frag;
    BookMarksHelper buyHelper, bookMarksHelper;
    private ArrayList<Audio> audios = new ArrayList<>();
    getHttpGet request = new getHttpGet();
    String idBook;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_fragment_payment, container, false);

        idbook = getActivity().getSharedPreferences("Category", Context.MODE_PRIVATE);

        sharedPreferences = getActivity().getSharedPreferences("Sign", Context.MODE_PRIVATE);


        dataBuy = new DataBasesHelper(getActivity());
        dataBookMarks = new DataBasesHelper(getActivity());

        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        name_frag = (TextView) view.findViewById(R.id.name_frag);
        name_frag.setText("Оплата");
        Bundle bundle = getArguments();
        if (bundle != null) {
            idBook = bundle.getString("idbook");
        }

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                back_arrow.setBackgroundResource(R.drawable.bakground_arrow);
            }
        });

        BuiId = dataBuy.getIdBuy();
        BookMarksId = dataBookMarks.getidRow();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getbook?salt=df90sdfgl9854gjs54os59gjsogsdf&book_id=" + idBook));
            Log.d("MyLog", String.valueOf(data));
            Gson gson = new Gson();
            audios = gson.fromJson(data.toString(),  new TypeToken<List<Audio>>(){}.getType());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        buy = (Button) view.findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                request.getHttpGet("http://www.glagolapp.ru/api/buyBook?salt=df90sdfgl9854gjs54os59gjsogsdf&user_id=" + sharedPreferences.getString("id","") + "&book_id=" + idBook);


                if (BuiId.size() == 0)
                {
                    for (int i = 0; i < BookMarksId.size(); i++) {
                        bookMarksHelper = dataBookMarks.getProduct(BookMarksId.get(i).toString(), "Bookmarks");

                        if (audios.get(0).getId().equals(bookMarksHelper.getId_book())) {
                            SQLiteDatabase db = dataBookMarks.getWritableDatabase();
                            db.delete("Bookmarks", "id_book = " + bookMarksHelper.getId_book(), null);
                            break;
                        }
                    }
                    dataBuy.insertBuy(audios.get(0).getName_authors(), audios.get(0).getName_book(), audios.get(0).getReaders(), audios.get(0).getPrice(), audios.get(0).getIcon(), audios.get(0).getId());
                    dataBuy.close();
                }
                else {
                    int check = 0;
                    for (int i = 0; i < BuiId.size(); i++) {
                        buyHelper = dataBuy.getProduct(BuiId.get(i).toString(), "Buy");
                        if (audios.get(0).getId().equals(buyHelper.getId_book())) {
                            check=1;
                            break;
                        }
                    }
                    if (check == 0)
                    {
                        for (int i = 0; i < BookMarksId.size(); i++) {
                            bookMarksHelper = dataBookMarks.getProduct(BookMarksId.get(i).toString(), "Bookmarks");

                            if (audios.get(0).getId().equals(bookMarksHelper.getId_book())) {
                                SQLiteDatabase db = dataBookMarks.getWritableDatabase();
                                db.delete("Bookmarks", "id_book = " + bookMarksHelper.getId_book(), null);
                                break;
                            }
                        }
                        dataBuy.insertBuy(audios.get(0).getName_authors(), audios.get(0).getName_book(), audios.get(0).getReaders(), audios.get(0).getPrice(), audios.get(0).getIcon(), audios.get(0).getId());
                        dataBuy.close();
                    }
                }
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
        fragment = new CardBook();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame, fragment).commit();

        }


    }

}
