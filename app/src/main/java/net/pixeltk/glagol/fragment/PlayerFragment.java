package net.pixeltk.glagol.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.stetho.Stetho;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.activity.TabActivity;
import net.pixeltk.glagol.adapter.BookMarksHelper;
import net.pixeltk.glagol.adapter.DataBasesHelper;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;
import net.pixeltk.glagol.player.SongsManager;
import net.pixeltk.glagol.player.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 04.10.16.
 */

public class PlayerFragment extends Fragment implements OnBackPressedListener, MediaPlayer.OnCompletionListener{

    public PlayerFragment() {
        // Required empty public constructor
    }
    ImageView back_arrow, logo, cover;
    TextView name_frag, now_time, all_time, name_book, name_author, part, left_30, right_30;
    String ATTRIBUTE_NAME_TEXT = "text";
    ListView playList;
    private int currentSongIndex = 0;
    int positon = 0;
    SeekBar seekBar;
    private MediaPlayer mp;
    private SongsManager songManager;
    private Utilities utils;
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    SharedPreferences idbook, playing;
    SharedPreferences.Editor editor;
    Fragment fragment = null;
    Button play_pause, back_track, next_track;
    private Handler mHandler = new Handler();
    int now_listening, total_duration, duration;
    int old_listen = 0;
    DataBasesHelper dataBasesHelper;
    BookMarksHelper listenHelper;
    ArrayList ListenId = new ArrayList();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
       // Stetho.initializeWithDefaults(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.player_fragment, container, false);

        idbook = getActivity().getSharedPreferences("Category", Context.MODE_PRIVATE);
        playing = getActivity().getSharedPreferences("Playing", Context.MODE_PRIVATE);
        editor = playing.edit();
        Bundle bundle = this.getArguments();
        String check_play =  bundle.getString("open");

        //Log.d("MyLog",  bundle.getString("open"));

        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);
        cover = (ImageView) view.findViewById(R.id.cover);

        playList = (ListView) view.findViewById(R.id.playlist);

        seekBar = (SeekBar) view.findViewById(R.id.seekBar);

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

        play_pause = (Button) view.findViewById(R.id.play_pause);
        back_track = (Button) view.findViewById(R.id.back_track);
        next_track = (Button) view.findViewById(R.id.next_track);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (idbook.contains("book_name")) {
            name_book.setText(idbook.getString("book_name", ""));
            name_author.setText(idbook.getString("name_author", ""));
            Glide.with(getActivity()).load(idbook.getString("url_img", "")).into(cover);
            createPlayList(idbook.getString("book_name", ""));
        }



        init();
        songManager = new SongsManager();
        utils = new Utilities();
        mp.setOnCompletionListener(this);

        songsList = songManager.getPlayList(idbook.getString("book_name", ""));

        if (idbook.contains("intent")) {
            if (check_play.equals("play")) {

                if (ListenId.size() != 0) {
                    for (int i = 0; i < ListenId.size(); i++) {
                        listenHelper = dataBasesHelper.getProductListen(ListenId.get(i).toString());
                        if (idbook.getString("book_name", "").equals(listenHelper.getName_book())) {
                            positon = Integer.parseInt(listenHelper.getCurrent_position());
                            old_listen = Integer.parseInt(listenHelper.getSeekbar_value());
                            duration = Integer.parseInt(listenHelper.getTotal_duration());
                            currentSongIndex = positon;
                            playSong(positon);
                            mp.seekTo(old_listen);
                            updateProgressBar();
                            editor.putString("play", "play").apply();
                            play_pause.setBackgroundResource(R.mipmap.pause_button);

                            break;
                        }
                    }
                } else {
                    playSong(0);
                    play_pause.setBackgroundResource(R.mipmap.pause_button);
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
                            String songTitle = songsList.get(currentSongIndex).get("songTitle");
                            part.setText(songTitle);
                            break;
                        }
                    }
                } else {
                    playSong(0);
                    play_pause.setBackgroundResource(R.mipmap.pause_button);
                }
            }
        }


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                updateProgressBar();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mp.getDuration();
                int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                mp.seekTo(currentPosition);

                // update timer progress again
                updateProgressBar();
            }
        });




        back_track.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (currentSongIndex > 0) {
                    playSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                } else {
                    // play last song
                    playSong(songsList.size() - 1);
                    currentSongIndex = songsList.size() - 1;
                }

            }
        });

        next_track.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if (currentSongIndex < (songsList.size() - 1)) {
                    playSong(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                } else {
                    // play first song
                    playSong(0);
                    play_pause.setBackgroundResource(R.mipmap.pause_button);
                    currentSongIndex = 0;
                }

            }
        });
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp.isPlaying()) {
                    if (mp != null) {
                        play_pause.setBackgroundResource(R.mipmap.play_button_player);
                        total_duration = mp.getDuration();
                        now_listening = utils.progressToTimer(seekBar.getProgress(), total_duration);
                        Log.d("MyLog", " now_listening" + now_listening + " old_listen"  + old_listen +  " duration" + duration);
                        double procent = ((old_listen + now_listening)/1000) / duration;

                        if (currentSongIndex == 0) {

                            dataBasesHelper.updatedetails(idbook.getString("idbook", ""), String.valueOf(procent), String.valueOf(positon), String.valueOf(old_listen + now_listening));
                        }
                        else
                        {
                            dataBasesHelper.updatedetails(idbook.getString("idbook", ""), String.valueOf(procent), String.valueOf(currentSongIndex), String.valueOf(old_listen + now_listening));
                        }
                            mp.pause();
                        editor.clear().apply();

                    }
                } else {
                    // Resume song
                    if (mp != null) {
                        //mp.start();
                        if (ListenId.size() != 0) {
                            for (int i = 0; i < ListenId.size(); i++) {
                                listenHelper = dataBasesHelper.getProductListen(ListenId.get(i).toString());
                                if (idbook.getString("book_name", "").equals(listenHelper.getName_book())) {
                                    positon = Integer.parseInt(listenHelper.getCurrent_position());
                                    old_listen = Integer.parseInt(listenHelper.getSeekbar_value());
                                    duration = Integer.parseInt(listenHelper.getTotal_duration());
                                    currentSongIndex = positon;
                                    playSong(positon);
                                    mp.seekTo(old_listen);
                                    updateProgressBar();
                                    editor.putString("play", "play").apply();
                                    play_pause.setBackgroundResource(R.mipmap.pause_button);

                                    break;
                                }
                            }
                        } else {
                            playSong(0);
                            play_pause.setBackgroundResource(R.mipmap.pause_button);
                        }
                        play_pause.setBackgroundResource(R.mipmap.pause_button);
                        editor.putString("play", "play").apply();
                    }
                }

            }
        });

        left_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mp.getDuration();

                int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

                Log.d("MyLog", " current " + currentPosition);

                // forward or backward to certain seconds
                mp.seekTo(currentPosition - 30000);
                updateProgressBar();
            }
        });
        right_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mp.getDuration();
                int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                mp.seekTo(currentPosition + 30000);

                // update timer progress again
                updateProgressBar();
            }
        });
        return view;
    }

    public MediaPlayer playSong(int songIndex) {
        // Play song
        try {
            mp.stop();
            mp.reset();
            mp.setDataSource(songsList.get(songIndex).get("songPath"));
            mp.prepare();
            mp.start();
            // Displaying Song title
            String songTitle = songsList.get(songIndex).get("songTitle");
            part.setText(songTitle);
            // set Progress bar values
            seekBar.setProgress(0);
            seekBar.setMax(100);

            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mp;
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
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            seekBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }

    };
    public MediaPlayer init() {
        mp = new MediaPlayer();
        Log.d("myLogs", "mp " + mp);
        return mp;
    }
    public void createPlayList(String book_name) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/Music/" + book_name);
        Log.d("Files", "f: " + dir);
        if (dir.exists()) {


            String path = Environment.getExternalStorageDirectory().toString() + "/Music/" + book_name;

            File f = new File(path);
            File file[] = f.listFiles();

            Log.d("Files", "Path: " + path);
            Log.d("Files", "f: " + f);

            if (!f.equals("")) {


                ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                        file.length);
                Map<String, Object> m;
                for (int i = 0; i < file.length; i++) {
                    m = new HashMap<String, Object>();
                    m.put(ATTRIBUTE_NAME_TEXT, file[i].getName());
                    data.add(m);
                }

                String[] from = {ATTRIBUTE_NAME_TEXT};
                // массив ID View-компонентов, в которые будут вставлять данные
                int[] to = {R.id.name_part};
                SimpleAdapter sAdapter = new SimpleAdapter(getActivity(), data, R.layout.item_for_playlist,
                        from, to);

                playList.setAdapter(sAdapter);
                playList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        playSong(position);
                        play_pause.setBackgroundResource(R.mipmap.pause_button);
                        currentSongIndex = position;
                    }
                });
            }

        } else {
            try {
                if (dir.mkdir()) {

                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
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
        } else {
            // play first song
            playSong(0);
            currentSongIndex = 0;
        }
    }
}
