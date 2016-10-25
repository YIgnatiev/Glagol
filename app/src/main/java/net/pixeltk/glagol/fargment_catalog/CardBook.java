package net.pixeltk.glagol.fargment_catalog;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.activity.TabActivity;
import net.pixeltk.glagol.adapter.BookMarksHelper;
import net.pixeltk.glagol.adapter.DataBasesHelper;
import net.pixeltk.glagol.adapter.DrawItemBookMarks;
import net.pixeltk.glagol.api.Audio;
import net.pixeltk.glagol.api.getHttpGet;
import net.pixeltk.glagol.fragment.ClickOnMainPart;
import net.pixeltk.glagol.fragment.FragmentPayment;
import net.pixeltk.glagol.fragment.ListSearchBook;
import net.pixeltk.glagol.fragment.MainFragment;
import net.pixeltk.glagol.fragment.PlayerFragment;
import net.pixeltk.glagol.fragment_my_book.Sigin;
import net.pixeltk.glagol.fragment_my_book.SuccessfulEnter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yaroslav on 09.10.2016.
 */

public class CardBook extends Fragment implements OnBackPressedListener {

    public CardBook() {
        // Required empty public constructor
    }

    Button buy, download, listen, book_marks, del_marks;
    Fragment fragment = null;
    SharedPreferences sharedPreferences, idbook, subscription, checklogin;
    SharedPreferences.Editor editor, editorsubscription;
    ImageView cover;
    TextView name_author, name_book, text_reader, text_publisher, text_time, text_teg, description;
    private ArrayList<Audio> audios = new ArrayList<>();
    getHttpGet request = new getHttpGet();
    LinearLayout content_line1, content_line2, content_line3;
    TextView name_frag;
    ImageView back_arrow, logo;
    DataBasesHelper dataBookMarks, dataHistory;
    ArrayList IdList = new ArrayList();
    ArrayList HistoryListId = new ArrayList();
    BookMarksHelper historyHelper, bookMarksHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.lauoyt_card_book, container, false);

        sharedPreferences = getActivity().getSharedPreferences("Payment", Context.MODE_PRIVATE);
        idbook = getActivity().getSharedPreferences("Category", Context.MODE_PRIVATE);
        subscription = getActivity().getSharedPreferences("Subscription", Context.MODE_PRIVATE);
        checklogin = getActivity().getSharedPreferences("Sign", Context.MODE_PRIVATE);
        editorsubscription = subscription.edit();
        editor = idbook.edit();

        dataBookMarks = new DataBasesHelper(getActivity());
        dataHistory = new DataBasesHelper(getActivity());

        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        name_frag = (TextView) view.findViewById(R.id.name_frag);
        name_frag.setText("О книге");

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buy = (Button) view.findViewById(R.id.buy);
        download = (Button) view.findViewById(R.id.download);
        listen = (Button) view.findViewById(R.id.listen);
        book_marks = (Button) view.findViewById(R.id.book_marks);
        del_marks = (Button) view.findViewById(R.id.del_marks);

        name_author = (TextView) view.findViewById(R.id.name_author);
        name_book = (TextView) view.findViewById(R.id.name_book);
        text_reader = (TextView) view.findViewById(R.id.text_reader);
        text_publisher = (TextView) view.findViewById(R.id.text_publisher);
        text_time = (TextView) view.findViewById(R.id.text_time);
        text_teg = (TextView) view.findViewById(R.id.text_teg);
        description = (TextView) view.findViewById(R.id.description);

        content_line1 = (LinearLayout) view.findViewById(R.id.content_line1);
        content_line2 = (LinearLayout) view.findViewById(R.id.content_line2);
        content_line3 = (LinearLayout) view.findViewById(R.id.content_line10);

        cover = (ImageView) view.findViewById(R.id.cover);

        IdList = dataBookMarks.getidRow();
        HistoryListId = dataHistory.getIdHistory();
        Log.d("MyLog", " id history list " + String.valueOf(HistoryListId.size()));


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getbook?salt=df90sdfgl9854gjs54os59gjsogsdf&book_id=" + idbook.getString("idbook", "")));

            Gson gson = new Gson();
            audios = gson.fromJson(data.toString(),  new TypeToken<List<Audio>>(){}.getType());

            if (audios!= null) {
                name_author.setText("Автор: " + audios.get(0).getName_authors());
                name_book.setText(audios.get(0).getName_book());
                text_reader.setText("Чтец: " + audios.get(0).getReaders());
                text_publisher.setText("Издательство: " + audios.get(0).getPublisher());
                if (audios.get(0).getDuration() != null && audios.get(0).getSize() != null) {
                    text_time.setText(getTime(Long.valueOf(audios.get(0).getDuration())) + " " + Integer.parseInt(String.valueOf(Integer.parseInt(audios.get(0).getSize()) / 1000)) + " мб.");
                }
                else
                {
                    text_time.setText(0 + " ч. " + 0 + " мин. " + 0+ " сек. " + " " + 0 + " мб.");
                }
                text_teg.setText(audios.get(0).getCategorys());
                text_teg.setPaintFlags(text_teg.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                description.setText(audios.get(0).getDescription());
                Glide.with(getActivity()).load(audios.get(0).getIcon()).into(cover);
                buy.setText("Купить за " + audios.get(0).getPrice() + " p.");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!checklogin.contains("id"))
        {
        }
        else {
            if (HistoryListId.size() == 0)
            {
                dataHistory.insertHistory(audios.get(0).getName_authors(), audios.get(0).getName_book(), audios.get(0).getReaders(), audios.get(0).getPrice(), audios.get(0).getIcon(), audios.get(0).getId());
                dataBookMarks.close();
            }
            else {
                int check = 0;
                for (int i = 0; i < HistoryListId.size(); i++) {
                    historyHelper = dataHistory.getProduct(HistoryListId.get(i).toString(), "History");

                    if (audios.get(0).getId().equals(historyHelper.getId_book())) {
                        check=1;
                        break;
                    }
                }
                if (check == 0)
                {
                    dataHistory.insertHistory(audios.get(0).getName_authors(), audios.get(0).getName_book(), audios.get(0).getReaders(), audios.get(0).getPrice(), audios.get(0).getIcon(), audios.get(0).getId());
                    dataHistory.close();
                }
            }
        }

        text_teg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("CategoryName").apply();
                editor.putString("CategoryName", text_teg.getText().toString()).apply();
                fragment = new ChoiceItemList();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, fragment).commit();

                }
            }
        });
        if (sharedPreferences.contains("Pay"))
        {
            buy.setVisibility(View.INVISIBLE);
            download.setVisibility(View.VISIBLE);
        }
        if (audios.get(0).getPrice() == null)
        {
            fragment = new MainFragment();
            if (fragment != null) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame, fragment).commit();

            }
        }
        for (int i = 0; i < IdList.size(); i++)
        {

            bookMarksHelper = dataBookMarks.getProduct(IdList.get(i).toString(), "Bookmarks");
            if (audios.get(0).getId().equals(bookMarksHelper.getId_book()))
            {
                book_marks.setVisibility(View.INVISIBLE);
                del_marks.setVisibility(View.VISIBLE);
            }
            bookMarksHelper = null;
        }

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download.setVisibility(View.INVISIBLE);
                listen.setVisibility(View.VISIBLE);
                setDownload();

            }
        });

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checklogin.contains("id"))
                {
                    Toast toast = Toast.makeText(getActivity(),
                            "Для выполнения этого действия нужно авторизироватся!", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });

        del_marks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book_marks.setVisibility(View.VISIBLE);
                del_marks.setVisibility(View.INVISIBLE);
                SQLiteDatabase db = dataBookMarks.getWritableDatabase();
                db.delete("Bookmarks", "price = " + audios.get(0).getPrice(), null);
                db.close();
            }
        });

        book_marks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checklogin.contains("id"))
                {
                    Toast toast = Toast.makeText(getActivity(),
                            "Для выполнения этого действия нужно авторизироватся!", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    del_marks.setVisibility(View.VISIBLE);
                    book_marks.setVisibility(View.INVISIBLE);
                    dataBookMarks.insertBookMarks(audios.get(0).getName_authors(), audios.get(0).getName_book(), audios.get(0).getReaders(), audios.get(0).getPrice(), audios.get(0).getIcon(), audios.get(0).getId());
                    dataBookMarks.close();
                }
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checklogin.contains("id"))
                {

                    fragment = new Sigin();
                }
                else {
                    fragment = new FragmentPayment();

                }
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, fragment).commit();
                }
            }
        });

        content_line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editorsubscription.putString("Reader", audios.get(0).getReaders()).apply();
                fragment = new FragmentSubscription();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, fragment).commit();

                }
            }
        });

        content_line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editorsubscription.putString("Publisher", audios.get(0).getPublisher()).apply();
                fragment = new FragmentSubscription();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, fragment).commit();

                }
            }
        });

        content_line3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editorsubscription.putString("Author", audios.get(0).getName_authors()).apply();
                fragment = new FragmentSubscription();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, fragment).commit();

                }
            }
        });
        return  view;


    }
    public void setDownload ( )
    {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getbookfiles?salt=df90sdfgl9854gjs54os59gjsogsdf&book_id=" + idbook.getString("idbook", "")));

            Gson gson = new Gson();
            audios = gson.fromJson(data.toString(),  new TypeToken<List<Audio>>(){}.getType());

            if (audios!= null) {
               /* for (int i=0; i<audios.size(); i++)
                {*/
                    Audio audio = audios.get(0);
                    new DownloadFileFromURL().execute(audio.getPath_audio(), audio.getName_audio());
                //}
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String getTime(Long s){
        Long hours = s / 3600;
        Long minutes = (s % 3600) / 60;
        Long seconds = s % 60;
        return  hours + " ч. " + minutes + " мин. " + seconds+ " сек. ";
    }
    @Override
    public void onBackPressed() {
        if (idbook.getString("intent", "").equals("Successful"))
        {
            editor.remove("intent").apply();
            fragment = new SuccessfulEnter();
        }
        else if (idbook.getString("intent", "").equals("Main"))
        {
            fragment = new MainFragment();
            editor.remove("intent").apply();
        }
        else if (idbook.getString("intent", "").equals("mainpart"))
        {
            fragment = new ClickOnMainPart();
            editor.remove("intent").apply();
        }
        else if (idbook.getString("intent", "").equals("Subscription"))
        {
            fragment = new FragmentSubscription();
            editor.remove("intent").apply();
        }
        else if (idbook.getString("intent", "").equals("ListSearch"))
        {
            fragment = new ListSearchBook();
            editor.remove("intent").apply();
        }

        else {
            fragment = new ChoiceItemList();
            editor.remove("intent").apply();
        }
        editor.remove("idbook").apply();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame, fragment).commit();

        }


    }
    class DownloadFileFromURL extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            URL u = null;
            InputStream is = null;

            try {
                u = new URL(f_url[0]);
                is = u.openStream();
                HttpURLConnection huc = (HttpURLConnection)u.openConnection();//to know the size of video
                int size = huc.getContentLength();

                if(huc != null){
                    String fileName = f_url[1];
                    String storagePath = Environment.getExternalStorageDirectory().toString();
                    File f = new File(storagePath,fileName);

                    FileOutputStream fos = new FileOutputStream(f);
                    byte[] buffer = new byte[1024];
                    long total = 0;
                    int len1 = 0;
                    if(is != null){
                        while ((len1 = is.read(buffer)) > 0) {
                            total+=len1;
                            publishProgress((int)((total*100)/size));
                            fos.write(buffer,0, len1);
                        }
                    }
                    if(fos != null){
                        fos.close();
                    }
                }
            }catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    if(is != null){
                        is.close();
                    }
                }catch (IOException ioe) {
                    // just going to ignore this one
                }
            }
            return "";
        }


        @Override
        protected void onPostExecute(String file_url) {


        }

    }

}
