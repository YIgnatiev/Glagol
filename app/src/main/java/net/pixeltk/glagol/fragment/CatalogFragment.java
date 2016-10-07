package net.pixeltk.glagol.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.fargment_catalog.ListFragmentGlagol;

/**
 * Created by root on 04.10.16.
 */

public class CatalogFragment extends Fragment{

    public CatalogFragment() {
        // Required empty public constructor
    }
    Fragment fragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.catalog_fragment, container, false);

        fragment = new ListFragmentGlagol();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.catalog_frame, fragment).commit();
        }
        return view;
    }

}
