package net.pixeltk.glagol.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.activity.TabActivity;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 04.10.16.
 */

public class PlayerFragment extends Fragment implements OnBackPressedListener{

    public PlayerFragment() {
        // Required empty public constructor
    }
    ImageView back_arrow, logo;
    TextView name_frag;
    String ATTRIBUTE_NAME_TEXT = "text";
    ListView playList;
    SharedPreferences idbook;
    Fragment fragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.player_fragment, container, false);

        idbook = getActivity().getSharedPreferences("Category", Context.MODE_PRIVATE);

        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);

        playList = (ListView) view.findViewById(R.id.playlist);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        name_frag = (TextView) view.findViewById(R.id.name_frag);
        name_frag.setText("Плеер");

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (idbook.contains("book_name")) {
            createPlayList(idbook.getString("book_name", ""));
        }
        return view;
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


                        SharedPreferences preferences = getActivity().getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_WORLD_WRITEABLE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("position", String.valueOf(position));
                        editor.apply();

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

}
