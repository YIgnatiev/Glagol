package net.pixeltk.glagol.fragment_my_book;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.pixeltk.glagol.R;

/**
 * Created by root on 07.10.16.
 */

public class ResetPass extends Fragment {

    public ResetPass() {
        // Required empty public constructor
    }

    Button get_pass;
    Fragment fragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resetpass, container, false);

        get_pass = (Button) view.findViewById(R.id.get_pass);

        get_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new Login();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_my_book, fragment).commit();
                }
            }
        });


        return view;
    }
}
