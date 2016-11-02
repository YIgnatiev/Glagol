package net.pixeltk.glagol.activity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.fargment_catalog.ListFragmentGlagol;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;
import net.pixeltk.glagol.fragment.MainFragment;
import net.pixeltk.glagol.fragment.MyBooks;
import net.pixeltk.glagol.fragment.OtherInfoFragment;
import net.pixeltk.glagol.fragment.PlayerFragment;

import java.io.File;

import static net.pixeltk.glagol.fargment_catalog.CardBook.changeProgress;
import static net.pixeltk.glagol.fargment_catalog.CardBook.changeVisibility;
import static net.pixeltk.glagol.fargment_catalog.CardBook.count;
import static net.pixeltk.glagol.fargment_catalog.CardBook.size;

public class  TabActivity extends AppCompatActivity {


    LinearLayout main, catalog, player, book, other;
    static TabLayout tabLayout;
    SharedPreferences playing;
    static SharedPreferences idbook;
    SharedPreferences.Editor editor;
    static SharedPreferences.Editor editbook;
    static DownloadManager manager;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

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
        book = (LinearLayout) tabLayout.findViewById(R.id.book);
        other = (LinearLayout) tabLayout.findViewById(R.id.other);

        replaceFragment(new MainFragment());

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new MainFragment());
                tabLayout.getTabAt(0).select();
                editor.remove("play").apply();
            }
        });

        catalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new ListFragmentGlagol());
                tabLayout.getTabAt(1).select();
                editor.remove("play").apply();
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
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new MyBooks());
                tabLayout.getTabAt(3).select();
                editor.remove("play").apply();
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new OtherInfoFragment());
                tabLayout.getTabAt(4).select();
                editor.remove("play").apply();
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

    public static void load(String book_name, String file_name, String url_file, final Context c) throws Exception {


        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        File dir = new File(Environment.getExternalStorageDirectory() + "/Music/" + book_name + "/");
        dir.mkdir();
        String destination = Environment.getExternalStorageDirectory() + "/Music/" + book_name + "/";

        destination += file_name;
        Log.d("MyLog", "destin "  + destination);
        final Uri uri = Uri.parse("file://" + destination);

        //set downloadmanager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url_file.replace(" ", "%20")));
        request.setTitle("Идет загрузка...");

        //set destination
        request.setDestinationUri(uri);


        // get download service and enqueue file
        manager = (DownloadManager) c.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        //set BroadcastReceiver to install app when .apk is downloaded
        final BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {

                long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                if(referenceId == downloadId) {

                    editbook.putInt("download", changeProgress()).apply();
                    count++;
                    Log.d("MyLog", "download " + idbook.getInt("download", 0));
                    if (count == size)
                    {
                        changeVisibility();
                        editbook.remove("download").apply();
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
                    Log.d("MyLog", "Complete");
                }
                else  {

                    Log.d("MyLog", "not");
                }
            }
        };
        //register receiver for when .apk download is compete
        Log.d("MyLog", String.valueOf(onComplete));
        c.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }
}

