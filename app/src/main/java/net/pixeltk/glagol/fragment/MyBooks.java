package net.pixeltk.glagol.fragment;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.fragment_my_book.CheckLoginSign;
import net.pixeltk.glagol.fragment_my_book.SuccessfulEnter;

/**
 * Created by root on 04.10.16.
 */

public class MyBooks extends Fragment {

    public MyBooks() {
        // Required empty public constructor
    }

    SharedPreferences sharedPreferences;

    Fragment fragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.mybooks_fragment, container, false);

        sharedPreferences = getActivity().getSharedPreferences("Sign", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("id"))
        {
            fragment = new SuccessfulEnter();
        }
        else
        {
            fragment = new CheckLoginSign();
        }

        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_my_book, fragment).commit();
        }

        return view;

    }

}
