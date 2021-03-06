package net.pixeltk.glagol.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mukesh.tinydb.TinyDB;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.activity.TabActivity;
import net.pixeltk.glagol.adapter.BookMarksHelper;
import net.pixeltk.glagol.adapter.DataBasesHelper;
import net.pixeltk.glagol.api.Audio;
import net.pixeltk.glagol.api.getHttpGet;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;
import net.pixeltk.glagol.player.SongsManager;
import net.pixeltk.glagol.player.Utilities;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.pixeltk.glagol.Splash.mp;
import static net.pixeltk.glagol.Splash.playSong;
import static net.pixeltk.glagol.Splash.songsList;
import static net.pixeltk.glagol.activity.TabActivity.hasConnection;

/**
 * Created by root on 04.10.16.
 */

public class PlayerFragment extends Fragment implements OnBackPressedListener, MediaPlayer.OnCompletionListener{

    public PlayerFragment() {
        // Required empty public constructor
    }
    ImageView back_arrow, logo, cover;
    TextView name_frag, now_time, all_time, name_book, name_author, left_30, right_30;
    public static TextView part;
    String ATTRIBUTE_NAME_TEXT = "text";
    getHttpGet request = new getHttpGet();
    private ArrayList<Audio> audios = new ArrayList<>();
    ListView playList;
    private int currentSongIndex = 0;
    int positon = 0;
    double old_procent = 0.0;
    public static SeekBar seekBar;

    private SongsManager songManager;
    private Utilities utils;

    SharedPreferences idbook, playing;
    SharedPreferences.Editor editor;
    Fragment fragment = null;
    Button back_track, next_track;
    CheckBox play_pause;
    private Handler mHandler = new Handler();
    int now_listening, total_duration, duration;
    int old_listen;
    double seek_value;
    DataBasesHelper dataBasesHelper;
    BookMarksHelper listenHelper;
    ArrayList ListenId = new ArrayList();
    public static ArrayList<String> track_name = new ArrayList<>();

    final String DIR_SD = "MyFiles";
    String readFileStr = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Stetho.initializeWithDefaults(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.player_fragment, container, false);

        idbook = getActivity().getSharedPreferences("Category", Context.MODE_PRIVATE);
        playing = getActivity().getSharedPreferences("Playing", Context.MODE_PRIVATE);
        editor = playing.edit();

        track_name.clear();

        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);
        cover = (ImageView) view.findViewById(R.id.cover);

        playList = (ListView) view.findViewById(R.id.playlist);
        playList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setProgress(0);

        now_time = (TextView) view.findViewById(R.id.now_time);
        all_time = (TextView) view.findViewById(R.id.all_time);
        name_book = (TextView) view.findViewById(R.id.name_book);
        name_author = (TextView) view.findViewById(R.id.name_author);
        part = (TextView) view.findViewById(R.id.part);
        left_30 = (TextView) view.findViewById(R.id.left_30);
        right_30 = (TextView) view.findViewById(R.id.right_30);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        dataBasesHelper = new DataBasesHelper(getActivity());

        ListenId = dataBasesHelper.getIdListen();

        name_frag = (TextView) view.findViewById(R.id.name_frag);
        name_frag.setText("Плеер");

        play_pause = (CheckBox) view.findViewById(R.id.play_pause);
        back_track = (Button) view.findViewById(R.id.back_track);
        next_track = (Button) view.findViewById(R.id.next_track);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                back_arrow.setBackgroundResource(R.drawable.bakground_arrow);
                onBackPressed();
            }
        });

        songManager = new SongsManager();
        utils = new Utilities();

        seekBar.setMax(mp.getDuration());


        if (idbook.contains("download_book"))
        {
            if (idbook.getString("download_book","").equals(idbook.getString("id_book", ""))) {
                Toast toast = Toast.makeText(getActivity(),
                        "Дождитесь загрузки!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else {
            if (idbook.contains("book_name")) {
                mp.setOnCompletionListener(this);
                name_book.setText(idbook.getString("book_name", ""));
                name_author.setText(idbook.getString("name_author", ""));
                Glide.with(getActivity()).load(idbook.getString("url_img", "")).placeholder(R.drawable.notcover).into(cover);
                createPlayList(idbook.getString("book_name", ""));
                songsList = songManager.getPlayList(idbook.getString("book_name", ""));
                play_pause.setEnabled(true);
                if (!mp.isPlaying()) {
                        play_pause.setChecked(true);
                        if (ListenId.size() != 0) {
                            for (int i = 0; i < ListenId.size(); i++) {
                                listenHelper = dataBasesHelper.getProductListen(ListenId.get(i).toString());
                                if (idbook.getString("book_name", "").equals(listenHelper.getName_book())) {
                                    positon = Integer.parseInt(listenHelper.getCurrent_position());
                                    old_listen = Integer.parseInt(listenHelper.getSeekbar_value());
                                    duration = Integer.parseInt(listenHelper.getTotal_duration());
                                    String songTitle = String.valueOf(track_name.get(currentSongIndex));
                                    part.setText(songTitle);
                                    currentSongIndex = positon;
                                    playSong(positon);
                                    updateProgressBar();
                                    mp.seekTo((int) old_listen);
                                    updateProgressBar();
                                    editor.putString("play", "play").apply();
                                    positon = 0;
                                    old_listen = 0;

                                    break;
                                }
                            }
                        } else {
                            playSong(0);
                            updateProgressBar();
                        }
                }
                else
                {
                    if (ListenId.size() != 0) {
                        for (int i = 0; i < ListenId.size(); i++) {
                            listenHelper = dataBasesHelper.getProductListen(ListenId.get(i).toString());
                            if (idbook.getString("book_name", "").equals(listenHelper.getName_book())) {
                                positon = Integer.parseInt(listenHelper.getCurrent_position());
                                currentSongIndex = positon;
                                Log.d("MyLog", "seek "  + seekBar.getProgress() + " max seek " + seekBar.getMax());
                                String songTitle = String.valueOf(track_name.get(currentSongIndex));
                                part.setText(songTitle);
                                updateProgressBar();
                                Log.d("MyLog", "seek1 "  + seekBar.getProgress() + " max seek1 " + seekBar.getMax());
                                positon = 0;
                                break;
                            }
                        }
                    }
                    play_pause.setChecked(true);
                }

            } else {
                play_pause.setEnabled(false);
                Toast toast = Toast.makeText(getActivity(),
                        "Для прослушивание выбирете книгу в каталоге!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        if (playing.contains("checked"))
        {
            play_pause.setChecked(true);
            mHandler.removeCallbacks(mUpdateTimeTask);
            seekBar.setProgress(mp.getCurrentPosition());
            updateProgressBar();
        }
        play_pause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true)
                {
                    if (!mp.isPlaying())
                    {
                        if (mp != null) {
                            mp.start();
                            if (ListenId.size() != 0) {
                                for (int i = 0; i < ListenId.size(); i++) {
                                    listenHelper = dataBasesHelper.getProductListen(ListenId.get(i).toString());
                                    if (idbook.getString("book_name", "").equals(listenHelper.getName_book())) {
                                        positon = Integer.parseInt(listenHelper.getCurrent_position());
                                        old_listen = Integer.parseInt(listenHelper.getSeekbar_value());
                                        old_procent = Double.parseDouble(listenHelper.getNow_listening());
                                        duration = Integer.parseInt(listenHelper.getTotal_duration());
                                        currentSongIndex = positon;
                                        seek_value = old_listen;
                                        editor.putBoolean("checked", true).apply();
                                        positon = 0;
                                        old_listen = 0;
                                        break;
                                    }
                                }
                            } else {
                                playSong(0);
                            }

                            editor.putBoolean("checked", true).apply();
                        }
                    }
                }
                else
                {
                    if (mp != null) {
                        double procent = 0;
                        total_duration = mp.getDuration();
                        now_listening = seekBar.getProgress();

                        if (currentSongIndex == 0) {

                            dataBasesHelper.updatedetails(idbook.getString("id_book", ""), String.valueOf(procent), String.valueOf(positon), String.valueOf(now_listening));
                        }
                        else
                        {
                            dataBasesHelper.updatedetails(idbook.getString("id_book", ""), String.valueOf(procent), String.valueOf(currentSongIndex), String.valueOf(now_listening));
                        }
                        mp.pause();
                        editor.clear().apply();
                        total_duration = 0;
                        duration = 0;

                    }
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (seekBar.getProgress() == mp.getDuration())
                {
                    double now_lsten = 0.0;
                    int total = 0;
                    int seek= 0;
                    if (ListenId.size() != 0) {
                        for (int j = 0; j < ListenId.size(); j++) {
                            listenHelper = dataBasesHelper.getProductListen(ListenId.get(j).toString());
                            if (idbook.getString("book_name", "").equals(listenHelper.getName_book())) {
                                duration = Integer.parseInt(listenHelper.getTotal_duration());
                                now_lsten = Double.parseDouble(listenHelper.getNow_listening());
                                seek =  Integer.parseInt(listenHelper.getSeekbar_value());
                                break;
                            }
                        }
                    }
                    total = mp.getDuration();
                    Log.d("MyLog", "total dur " + total + "duration " + duration + " now " + now_lsten);
                    double procent;
                    total = total - seek;
                    if (duration == 0)
                    {
                        procent = (total/1000);
                        procent += now_lsten;
                    }
                    else
                    {
                        procent = total / 1000;
                        procent = procent / duration;
                        procent = procent + now_lsten;
                        Log.d("MyLog", "procent " + procent);
                    }
                    dataBasesHelper.updatedetails(idbook.getString("id_book", ""), String.valueOf(procent), String.valueOf(currentSongIndex), String.valueOf(0));

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                updateProgressBar();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);

                mp.seekTo(seekBar.getProgress());

                // update timer progress again
                updateProgressBar();
            }
        });




        back_track.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (currentSongIndex > 0) {
                    playSong(currentSongIndex - 1);
                    updateProgressBar();
                    currentSongIndex = currentSongIndex - 1;
                    playList.setItemChecked(currentSongIndex, true);
                    playList.setSelection(currentSongIndex);
                } else {
                    // play last song
                    playSong(songsList.size() - 1);
                    updateProgressBar();
                    currentSongIndex = songsList.size() - 1;
                }
                play_pause.setChecked(true);

            }
        });

        next_track.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if (currentSongIndex < (songsList.size() - 1)) {
                    playSong(currentSongIndex + 1);
                    updateProgressBar();
                    currentSongIndex = currentSongIndex + 1;
                } else {
                    playSong(0);
                    updateProgressBar();
                    currentSongIndex = 0;
                }
                play_pause.setChecked(true);

            }
        });

        left_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int currentPosition = seekBar.getProgress() - 15000;
                mp.seekTo(currentPosition);
                updateProgressBar();
                play_pause.setChecked(true);
            }
        });
        right_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int currentPosition = seekBar.getProgress() + 15000;
                mp.seekTo(currentPosition);
                updateProgressBar();
                play_pause.setChecked(true);
            }
        });
        return view;
    }


    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();


            // Displaying Total Duration time
            all_time.setText("" + utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            now_time.setText("" + utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (currentDuration);
            //Log.d("Progress", ""+progress);
            seekBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }

    };
    void writeFileSD(ArrayList arrayList) {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d("MyLog", "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, idbook.getString("id_book", " "));
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write(String.valueOf(arrayList));
            // закрываем поток
            bw.close();
            Log.d("MyLog", "Файл записан на SD: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFileSD() {
        String str = "";
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d("MyLog", "SD-карта не доступна: " + Environment.getExternalStorageState());
            return null;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, idbook.getString("id_book"," "));
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                Log.d("MyLog", str);
                String str1 = str.replace("[", "");
                String str2 = str1.replace("]", "");
                String[] data = str2.split(",");
                for (String aData : data) {
                    Log.d("MyLog", aData);
                    track_name.add(aData);
                }
                Log.d("MyLog", "str " + readFileStr + " trak " + track_name);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }
    public void createPlayList(String book_name) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/Glagol/" + book_name);
        Log.d("Files", "f: " + dir);
        if (dir.exists()) {
            if(!hasConnection(getActivity()))
            {
               readFileSD();

            }
            else {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                try {
                    JSONArray data = new JSONArray(request.getHttpGet("http://www.glagolapp.ru/api/getbookfiles?salt=df90sdfgl9854gjs54os59gjsogsdf&book_id=" + idbook.getString("id_book", "")));

                    Gson gson = new Gson();
                    audios = gson.fromJson(data.toString(), new TypeToken<List<Audio>>() {
                    }.getType());
                    if (audios != null) {

                        for (int i = 0; i < audios.size(); i++) {
                            Audio audio = audios.get(i);
                            track_name.add(audio.getDescription());
                        }
                        writeFileSD(track_name);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            final String path = Environment.getExternalStorageDirectory().toString() + "/Glagol/" + book_name;

            File f = new File(path);
            File file[] = f.listFiles();

            if (!f.equals("")) {

                ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(file.length);
                Map<String, Object> m;
                for (int i = 0; i < track_name.size(); i++) {
                    m = new HashMap<String, Object>();
                    m.put(ATTRIBUTE_NAME_TEXT, track_name.get(i));
                    data.add(m);
                }

                String[] from = {ATTRIBUTE_NAME_TEXT};
                // массив ID View-компонентов, в которые будут вставлять данные
                int[] to = {R.id.name_part};
                SimpleAdapter sAdapter = new SimpleAdapter(getActivity(), data, R.layout.item_for_playlist,
                        from, to);

                playList.setAdapter(sAdapter);
                playList.setOnTouchListener(new View.OnTouchListener() {
                    // Setting on Touch Listener for handling the touch inside ScrollView
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Disallow the touch request for parent scroll on touch of child view
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });
                playList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        playSong(position);
                        play_pause.setChecked(true);
                        updateProgressBar();
                        currentSongIndex = position;
                        if (ListenId.size() != 0) {
                            for (int i = 0; i < ListenId.size(); i++) {
                                listenHelper = dataBasesHelper.getProductListen(ListenId.get(i).toString());
                                if (idbook.getString("book_name", "").equals(listenHelper.getName_book())) {
                                    positon = Integer.parseInt(listenHelper.getCurrent_position());
                                    old_listen = Integer.parseInt(listenHelper.getSeekbar_value());
                                    old_procent = Double.parseDouble(listenHelper.getNow_listening());
                                    duration = Integer.parseInt(listenHelper.getTotal_duration());
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        fragment = new MainFragment();
        TabActivity tabActivity = new TabActivity();
        tabActivity.changePage();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame, fragment).commit();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (currentSongIndex < (songsList.size() - 1)) {
            playSong(currentSongIndex + 1);
            currentSongIndex = currentSongIndex + 1;
            playList.setItemChecked(currentSongIndex, true);
            playList.setSelection(currentSongIndex);
           } else {
            // play first song
            playSong(0);
            currentSongIndex = 0;
        }
    }
}
