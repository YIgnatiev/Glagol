package net.pixeltk.glagol.player;

import android.app.Activity;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class SongsManager {
    // SDCard Path

    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    // Constructor
    public SongsManager() {

    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     */
    public ArrayList<HashMap<String, String>> getPlayList(String book_name) {


        String path = Environment.getExternalStorageDirectory().toString() + "/Music/" + book_name + "/";

        File f = new File(path);
        File file[] = f.listFiles();
        // return songs list array
        if (!f.equals("")) {

            for (int i = 0; i < file.length; i++) {
                HashMap<String, String> song = new HashMap<String, String>();
                song.put("songTitle", file[i].getName());
                song.put("songPath", file[i].getPath());

                // Adding each song to SongList
                songsList.add(song);
            }

        }
        return songsList;
    }


}

