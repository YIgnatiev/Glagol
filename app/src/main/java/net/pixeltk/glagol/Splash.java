package net.pixeltk.glagol;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.pixeltk.glagol.activity.TabActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static net.pixeltk.glagol.fragment.PlayerFragment.part;
import static net.pixeltk.glagol.fragment.PlayerFragment.seekBar;
import static net.pixeltk.glagol.fragment.PlayerFragment.track_name;

public class Splash extends AppCompatActivity {


    public static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    public static MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MyTask().execute();
        init();

    }
    public static MediaPlayer init() {
        mp = new MediaPlayer();
        Log.d("myLogs", "mp " + mp);
        return mp;
    }
    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent intent = new Intent(Splash.this, TabActivity.class);
            startActivity(intent);
        }
    }
    public static MediaPlayer playSong(int songIndex) {
        // Play song
        try {
            mp.stop();
            mp.reset();
            mp.setDataSource(songsList.get(songIndex).get("songPath"));
            mp.prepare();
            mp.start();
            // Displaying Song title
            String songTitle = String.valueOf(track_name.get(songIndex));
            part.setText(songTitle);
            // set Progress bar values

            seekBar.setProgress(0);
            seekBar.setMax(mp.getDuration());

            // Updating progress bar

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mp;
    }
}
