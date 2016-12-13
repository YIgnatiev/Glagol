package net.pixeltk.glagol.player;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import net.pixeltk.glagol.api.Audio;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


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


        String path = Environment.getExternalStorageDirectory().toString() + "/Glagol/" + book_name + "/";

        File f = new File(path);
        File file[] = f.listFiles();
        // return songs list array
        Collections.sort(Arrays.asList(file), new Comparator<File>(){
            public int compare(File emp1, File emp2) {
                 return Integer.valueOf(emp1.getName()).compareTo(Integer.valueOf(emp2.getName())); // To compare integer values
            }
        });

        if (!f.equals("")) {
            for (int i = 0; i < file.length; i++) {
                HashMap<String, String> song = new HashMap<String, String>();
                song.put("songTitle", file[i].getName());
                song.put("songPath", file[i].getPath());
                songsList.add(song);
            }
        }
        return songsList;
    }


}

