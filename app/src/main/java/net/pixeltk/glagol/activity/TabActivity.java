package net.pixeltk.glagol.activity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.api.Audio;
import net.pixeltk.glagol.api.getHttpGet;
import net.pixeltk.glagol.fargment_catalog.ListFragmentGlagol;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;
import net.pixeltk.glagol.fragment.MainFragment;
import net.pixeltk.glagol.fragment.MyBooks;
import net.pixeltk.glagol.fragment.OtherInfoFragment;
import net.pixeltk.glagol.fragment.PlayerFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static net.pixeltk.glagol.fargment_catalog.CardBook.changeVisibility;
import static net.pixeltk.glagol.fargment_catalog.CardBook.tv_progress_horizontal;

public class  TabActivity extends AppCompatActivity {


    LinearLayout main, catalog, player, my_book, other;
    static TabLayout tabLayout;
    SharedPreferences playing;
    static SharedPreferences idbook, sharedPreferences;
    SharedPreferences.Editor editor, subedit;
    static SharedPreferences.Editor editbook;
    static DownloadManager manager;
    static Context context;
    static int dl_progress = 0, progress = 0;
    static int bytes_downloaded = 0;
    private static Handler h1;
    static getHttpGet request = new getHttpGet();
    public static ArrayList<Audio> audios = new ArrayList<>();
    public static int size=0, count = 0, calculat_total_size = 0, change_mb = 0, download_mb = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        sharedPreferences = getSharedPreferences("Subscription", Context.MODE_PRIVATE);
        subedit = sharedPreferences.edit();
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_main));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_catalog));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_player));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_mybook));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_other));

        tabLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        playing = getSharedPreferences("Playing", Context.MODE_PRIVATE);
        idbook = getSharedPreferences("Category", Context.MODE_PRIVATE);
        editbook = idbook.edit();
        editor = playing.edit();

        main = (LinearLayout) tabLayout.findViewById(R.id.line1);
        catalog = (LinearLayout) tabLayout.findViewById(R.id.catalog);
        player = (LinearLayout) tabLayout.findViewById(R.id.player);
        my_book = (LinearLayout) tabLayout.findViewById(R.id.book);
        other = (LinearLayout) tabLayout.findViewById(R.id.other);

        h1 = new Handler() {
            public void handleMessage(android.os.Message msg) {
                // обновляем TextView
                tv_progress_horizontal.setText("Скачано: " + msg.what + "МБ из " + calculat_total_size + "МБ");
                editbook.putInt("download", msg.what).apply();
            }
        };


        replaceFragment(new MainFragment());

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new MainFragment());
                tabLayout.getTabAt(0).select();
                subedit.remove("play").apply();
                subedit.remove("author").apply();
                subedit.remove("reader").apply();
                subedit.remove("nameCollection");
                subedit.remove("collection").apply();
                subedit.remove("name_sel");
                subedit.remove("nameCollection");
                subedit.remove("put_selection").apply();
                subedit.remove("publisher").apply();
            }
        });

        catalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new ListFragmentGlagol());
                tabLayout.getTabAt(1).select();
                editor.remove("play").apply();
                subedit.remove("play").apply();
                subedit.remove("author").apply();
                subedit.remove("reader").apply();
                subedit.remove("nameCollection");
                subedit.remove("collection").apply();
                subedit.remove("name_sel");
                subedit.remove("nameCollection");
                subedit.remove("put_selection").apply();
                subedit.remove("publisher").apply();
            }
        });

        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new PlayerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("open", "not play");
                fragment.setArguments(bundle);
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_frame, fragment);
                    transaction.commit();

                }
                tabLayout.getTabAt(2).select();
                subedit.remove("play").apply();
                subedit.remove("author").apply();
                subedit.remove("reader").apply();
                subedit.remove("nameCollection");
                subedit.remove("collection").apply();
                subedit.remove("name_sel");
                subedit.remove("nameCollection");
                subedit.remove("put_selection").apply();
                subedit.remove("publisher").apply();
            }
        });

        my_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new MyBooks());
                tabLayout.getTabAt(3).select();
                editor.remove("play").apply();
                subedit.remove("play").apply();
                subedit.remove("author").apply();
                subedit.remove("reader").apply();
                subedit.remove("nameCollection");
                subedit.remove("collection").apply();
                subedit.remove("name_sel");
                subedit.remove("nameCollection");
                subedit.remove("put_selection").apply();
                subedit.remove("publisher").apply();
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new OtherInfoFragment());
                tabLayout.getTabAt(4).select();
                editor.remove("play").apply();
                subedit.remove("play").apply();
                subedit.remove("author").apply();
                subedit.remove("reader").apply();
                subedit.remove("nameCollection");
                subedit.remove("collection").apply();
                subedit.remove("name_sel");
                subedit.remove("nameCollection");
                subedit.remove("put_selection").apply();
                subedit.remove("publisher").apply();
            }
        });

    }
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment: fm.getFragments()) {
            if (fragment instanceof  OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
        }
    }
    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment);

        transaction.commit();
    }
    public static void changePage()
    {
        tabLayout.getTabAt(0).select();
    }

    public static void load(final String id_book, final String book_name, final int total_size, final Context c) throws Exception {
        String name_audio = null;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getbookfiles?salt=df90sdfgl9854gjs54os59gjsogsdf&book_id=" + id_book));

            Gson gson = new Gson();
            audios = gson.fromJson(data.toString(),  new TypeToken<List<Audio>>(){}.getType());

            if (audios!= null) {
                Audio audio = audios.get(count);
                size = audios.size();
                    name_audio = audio.getTrack_number();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File dir = new File(Environment.getExternalStorageDirectory() + "/Music/" + book_name + "/");
        dir.mkdir();
        String destination = Environment.getExternalStorageDirectory() + "/Music/" + book_name + "/";

        destination += name_audio;
        Log.d("MyLog", "destin "  + destination);
        final Uri uri = Uri.parse("file://" + destination);

        //set downloadmanager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(audios.get(count).getPath_audio().replace(" ", "%20")));
        request.setTitle("Идет загрузка...");


        //set destination
        request.setDestinationUri(uri);


        // get download service and enqueue file
        manager = (DownloadManager) c.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        new Thread(new Runnable() {

            @Override
            public void run() {

                boolean downloading = true;
                change_mb = 0;
                while (downloading) {

                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadId);

                    Cursor cursor = manager.query(q);
                    cursor.moveToFirst();
                    bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }
                    calculat_total_size = Integer.parseInt(String.valueOf(total_size / 1000));
                    if (change_mb < bytes_downloaded)
                    {
                        download_mb = download_mb + (bytes_downloaded - change_mb);
                        change_mb = bytes_downloaded;
                        h1.sendEmptyMessage(Integer.parseInt(String.valueOf(download_mb/1000000)));
                    }
                    cursor.close();
                }

            }
        }).start();

        //set BroadcastReceiver to install app when .apk is downloaded
        final BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {

                long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                if(referenceId == downloadId) {
                    count++;
                    try {
                            load(id_book, book_name, total_size, c);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (count == size)
                    {
                        changeVisibility();
                        editbook.remove("download");
                        editbook.remove("download_book").apply();
                        final Dialog dialog = new Dialog(c);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.complete_download);

                        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.customDialogCancel);
                        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }
                }
            }
        };
        //register receiver for when .apk download is compete
        Log.d("MyLog", String.valueOf(onComplete));
        c.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }

}

