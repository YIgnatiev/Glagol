package net.pixeltk.glagol.fargment_catalog;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.activity.TabActivity;
import net.pixeltk.glagol.adapter.BookMarksHelper;
import net.pixeltk.glagol.adapter.DataBasesHelper;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.pixeltk.glagol.activity.TabActivity.calculat_total_size;
import static net.pixeltk.glagol.activity.TabActivity.load;

/**
 * Created by Yaroslav on 09.10.2016.
 */

public class CardBook extends Fragment implements OnBackPressedListener {

    public CardBook() {
        // Required empty public constructor
    }

    Button buy, download, book_marks, del_marks, delete_audio, listen_demo, stoping_demo;
    static Button listen;

    Fragment fragment = null;
    SharedPreferences sharedPreferences, idbook, subscription, checklogin;
    SharedPreferences.Editor editor, editorsubscription;

    TabActivity tabActivity = new TabActivity();

    TextView name_author, name_book, text_reader, text_publisher, text_time, text_teg, description;
    TextView name_frag;
    public static TextView tv_progress_horizontal;

    getHttpGet request = new getHttpGet();

    LinearLayout content_line1, content_line2, content_line3;
    static RelativeLayout progressbar_line;

    ImageView back_arrow, logo;
    ImageView cover;
    String id_book, file_path, url_img, total_duration, author, reader, url_demo;
    DataBasesHelper dataBookMarks, dataHistory, dataBuy, dataDownload, dataListen;
    ArrayList IdList = new ArrayList();
    ArrayList HistoryListId = new ArrayList();
    ArrayList BuyListId = new ArrayList();
    ArrayList DownloadListId = new ArrayList();
    ArrayList ListenId = new ArrayList();
    public ArrayList<Audio> audios = new ArrayList<>();
    public String book;
    public static int total_size = 0;

    MediaPlayer mediaPlayer = new MediaPlayer();

    BookMarksHelper historyHelper, bookMarksHelper, buyHelper, downloadHelper, listenHelper;

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
        dataBuy = new DataBasesHelper(getActivity());
        dataDownload = new DataBasesHelper(getActivity());
        dataListen = new DataBasesHelper(getActivity());

        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        name_frag = (TextView) view.findViewById(R.id.name_frag);
        name_frag.setText("О книге");

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_arrow.setBackgroundResource(R.drawable.bakground_arrow);
                onBackPressed();
            }
        });

        buy = (Button) view.findViewById(R.id.buy);
        download = (Button) view.findViewById(R.id.download);
        listen = (Button) view.findViewById(R.id.listen);
        book_marks = (Button) view.findViewById(R.id.book_marks);
        del_marks = (Button) view.findViewById(R.id.del_marks);
        delete_audio = (Button) view.findViewById(R.id.delete_audio);
        listen_demo = (Button) view.findViewById(R.id.listen_demo);
        stoping_demo = (Button) view.findViewById(R.id.stop_demo);

        name_author = (TextView) view.findViewById(R.id.name_author);
        name_book = (TextView) view.findViewById(R.id.name_book);
        text_reader = (TextView) view.findViewById(R.id.text_reader);
        text_publisher = (TextView) view.findViewById(R.id.text_publisher);
        text_time = (TextView) view.findViewById(R.id.text_time);
        text_teg = (TextView) view.findViewById(R.id.text_teg);
        description = (TextView) view.findViewById(R.id.description);
        tv_progress_horizontal = (TextView) view.findViewById(R.id.tv_progress_horizontal);

        content_line1 = (LinearLayout) view.findViewById(R.id.content_line1);
        content_line2 = (LinearLayout) view.findViewById(R.id.content_line2);
        content_line3 = (LinearLayout) view.findViewById(R.id.content_line10);

        progressbar_line = (RelativeLayout) view.findViewById(R.id.progress_line);

        cover = (ImageView) view.findViewById(R.id.cover);

        IdList = dataBookMarks.getidRow();
        HistoryListId = dataHistory.getIdHistory();
        BuyListId = dataBuy.getIdBuy();
        DownloadListId = dataDownload.getIdDownload();
        ListenId = dataListen.getIdListen();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getbook?salt=df90sdfgl9854gjs54os59gjsogsdf&book_id=" + idbook.getString("idbook", "")));

            Gson gson = new Gson();
            audios = gson.fromJson(data.toString(),  new TypeToken<List<Audio>>(){}.getType());

            if (audios!= null) {
                id_book = audios.get(0).getId();
                url_demo = audios.get(0).getDemo();
                total_size = Integer.parseInt(audios.get(0).getSize());
                file_path = audios.get(0).getName_book();
                author = audios.get(0).getName_authors();
                reader = audios.get(0).getReaders();
                name_author.setText("Автор: " + audios.get(0).getName_authors());
                name_book.setText(audios.get(0).getName_book());
                text_reader.setText("Чтец: " + audios.get(0).getReaders());
                text_publisher.setText("Издательство: " + audios.get(0).getPublisher());
                total_duration = audios.get(0).getDuration();
                if (audios.get(0).getDuration() != null && audios.get(0).getSize() != null) {
                    text_time.setText(getTime(Long.valueOf(audios.get(0).getDuration())) + " " + Integer.parseInt(String.valueOf(Integer.parseInt(audios.get(0).getSize()) / 1000)) + " мб.");
                    download.setText("Скачать " + Integer.parseInt(String.valueOf(Integer.parseInt(audios.get(0).getSize()) / 1000)) + " мб.");
                }
                else
                {
                    text_time.setText(0 + " ч. " + 0 + " мин. " + 0+ " сек. " + " " + 0 + " мб.");
                    download.setText("Скачать " + 0 + " мб.");
                }
                text_teg.setText(audios.get(0).getCategorys());
                text_teg.setPaintFlags(text_teg.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                description.setText(audios.get(0).getDescription());
                url_img = audios.get(0).getIcon();
                Glide.with(getActivity()).load(audios.get(0).getIcon()).placeholder(R.drawable.notcover).into(cover);
                buy.setText("Купить за " + audios.get(0).getPrice() + " p.");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        book = name_book.getText().toString();
        if (!url_demo.equals("0")) {
            try {
                mediaPlayer.setDataSource(url_demo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (checklogin.contains("id"))
        {
            if (HistoryListId.size() == 0)
            {
                dataHistory.insertHistory(audios.get(0).getName_authors(), audios.get(0).getName_book(), audios.get(0).getReaders(), audios.get(0).getPrice(), audios.get(0).getIcon(), audios.get(0).getId());
                dataHistory.close();
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
        for (int i = 0; i < BuyListId.size(); i++) {
            buyHelper = dataBuy.getProduct(BuyListId.get(i).toString(), "Buy");

            if (audios.get(0).getId().equals(buyHelper.getId_book())) {
                buy.setVisibility(View.INVISIBLE);
                progressbar_line.setVisibility(View.INVISIBLE);
                download.setVisibility(View.VISIBLE);
                del_marks.setVisibility(View.GONE);
                book_marks.setVisibility(View.GONE);
                delete_audio.setVisibility(View.GONE);
                break;
            }
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
                book_marks.setVisibility(View.GONE);
                del_marks.setVisibility(View.VISIBLE);
            }
            bookMarksHelper = null;
        }
        for (int i = 0; i < DownloadListId.size(); i++)
        {

            downloadHelper = dataDownload.getProductDownload(DownloadListId.get(i).toString());
            Log.d("MyLog", " check " + downloadHelper.getId_book());
            if (id_book.equals(downloadHelper.getId_book()))
            {
                download.setVisibility(View.INVISIBLE);
                book_marks.setVisibility(View.GONE);
                del_marks.setVisibility(View.GONE);
                delete_audio.setVisibility(View.VISIBLE);
                listen.setVisibility(View.VISIBLE);
            }
            downloadHelper = null;
        }
        if (idbook.contains("download_book")) {
            if (idbook.getString("download_book", "").equals(id_book)) {
                if (idbook.contains("download")) {
                    download.setVisibility(View.INVISIBLE);
                    listen.setVisibility(View.INVISIBLE);
                    buy.setVisibility(View.INVISIBLE);
                    progressbar_line.setVisibility(View.VISIBLE);
                    delete_audio.setVisibility(View.VISIBLE);
                    book_marks.setVisibility(View.GONE);
                    del_marks.setVisibility(View.GONE);
                    tv_progress_horizontal.setText("Скачано:" + idbook.getInt("download", 0) + "МБ из " + calculat_total_size + "МБ");
                }
            }
        }

        delete_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download.setVisibility(View.VISIBLE);
                listen.setVisibility(View.INVISIBLE);
                progressbar_line.setVisibility(View.GONE);
                delete_audio.setVisibility(View.GONE);

                File dir = new File(Environment.getExternalStorageDirectory()+"/Music/" + file_path);
                if (dir.isDirectory())
                {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        new File(dir, children[i]).delete();
                    }
                }

                SQLiteDatabase db = dataDownload.getWritableDatabase();
                db.delete("Download", "id_book = " + id_book, null);
                db.delete("Listen", "id_book = " + id_book, null);
                db.close();
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download.setVisibility(View.INVISIBLE);
                progressbar_line.setVisibility(View.VISIBLE);
                listen.setVisibility(View.INVISIBLE);

                del_marks.setVisibility(View.GONE);
                book_marks.setVisibility(View.GONE);
                delete_audio.setVisibility(View.VISIBLE);

                setDownload();

                editor.putString("download_book", id_book).apply();
                if (DownloadListId.size() == 0)
                {
                    dataDownload.insertDownload(id_book);
                    dataDownload.close();
                }
                else {
                    int check = 0;
                    for (int i = 0; i < DownloadListId.size(); i++) {
                        downloadHelper = dataDownload.getProductDownload(DownloadListId.get(i).toString());
                        if (id_book.equals(downloadHelper.getId_book())) {
                            check=1;
                            break;
                        }
                    }
                    if (check == 0)
                    {
                        dataDownload.insertDownload(id_book);
                        dataDownload.close();
                    }
                }

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
                editor.putString("book_name", file_path);
                editor.putString("name_author", name_author.getText().toString());
                editor.putString("url_img", url_img).apply();
                if (ListenId.size() == 0)
                {
                    dataListen.insertListen(author, file_path, reader, url_img, "0", "0", total_duration, "0", id_book);
                    dataListen.close();
                }
                else {
                    int check = 0;
                    for (int i = 0; i < ListenId.size(); i++) {
                        listenHelper = dataListen.getProductListen(ListenId.get(i).toString());

                        if (audios.get(0).getId().equals(listenHelper.getId_book())) {
                            check=1;
                            break;
                        }
                    }
                    if (check == 0)
                    {
                        dataListen.insertListen(author, file_path, reader, url_img, "0", "0", total_duration, "0", id_book);
                        dataListen.close();
                    }
                }

                fragment = new PlayerFragment();
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

        del_marks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book_marks.setVisibility(View.VISIBLE);
                del_marks.setVisibility(View.GONE);
                delete_audio.setVisibility(View.GONE);
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
                    book_marks.setVisibility(View.GONE);
                    delete_audio.setVisibility(View.GONE);
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
                content_line1.setBackgroundColor(getResources().getColor(R.color.back));
                editorsubscription.putString("reader", audios.get(0).getReaders()).apply();
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
                content_line2.setBackgroundColor(getResources().getColor(R.color.back));
                editorsubscription.putString("publisher", audios.get(0).getPublisher()).apply();
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
                content_line3.setBackgroundColor(getResources().getColor(R.color.back));
                editorsubscription.putString("author", audios.get(0).getName_authors()).apply();
                fragment = new FragmentSubscription();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, fragment).commit();

                }
            }
        });

        listen_demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!url_demo.equals("0"))
                {
                        listen_demo.setVisibility(View.GONE);
                        stoping_demo.setVisibility(View.VISIBLE);
                        mediaPlayer.start();
                }
            }
        });

        stoping_demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer!=null)
                {
                    mediaPlayer.pause();
                    stoping_demo.setVisibility(View.GONE);
                    listen_demo.setVisibility(View.VISIBLE);
                }
            }
        });
        return  view;


    }
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
    public void setDownload ()
    {
        try {
            load(idbook.getString("idbook", ""), book, total_size, getActivity());
        } catch (Exception e) {
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
    public  static void changeVisibility()
    {
        progressbar_line.setVisibility(View.GONE);
        listen.setVisibility(View.VISIBLE);
    }

}
