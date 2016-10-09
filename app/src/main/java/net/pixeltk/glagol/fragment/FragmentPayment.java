package net.pixeltk.glagol.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.fargment_catalog.CardBook;

/**
 * Created by Yaroslav on 09.10.2016.
 */

public class FragmentPayment extends Fragment {

    public FragmentPayment() {
        // Required empty public constructor
    }

    Button buy;
    Fragment fragment = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_fragment_payment, container, false);

        sharedPreferences = getActivity().getSharedPreferences("Payment", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        buy = (Button) view.findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("Pay", 1).apply();
                fragment = new CardBook();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.catalog_frame, fragment).commit();
                }
            }
        });
        return view;
    }
}
