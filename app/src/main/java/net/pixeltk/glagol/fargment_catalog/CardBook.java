package net.pixeltk.glagol.fargment_catalog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.activity.TabActivity;
import net.pixeltk.glagol.fragment.FragmentPayment;
import net.pixeltk.glagol.fragment.PlayerFragment;

/**
 * Created by Yaroslav on 09.10.2016.
 */

public class CardBook extends Fragment implements OnBackPressedListener {

    public CardBook() {
        // Required empty public constructor
    }

    Button buy, download, listen;
    Fragment fragment = null;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.lauoyt_card_book, container, false);

        sharedPreferences = getActivity().getSharedPreferences("Payment", Context.MODE_PRIVATE);

        buy = (Button) view.findViewById(R.id.buy);
        download = (Button) view.findViewById(R.id.download);
        listen = (Button) view.findViewById(R.id.listen);

        if (sharedPreferences.contains("Pay"))
        {
            buy.setVisibility(View.INVISIBLE);
            download.setVisibility(View.VISIBLE);
        }

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download.setVisibility(View.INVISIBLE);
                listen.setVisibility(View.VISIBLE);

            }
        });

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new FragmentPayment();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.catalog_frame, fragment).commit();
                }
            }
        });
        return  view;


    }
    @Override
    public void onBackPressed() {
        fragment = new ChoiceItemList();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.catalog_frame, fragment).commit();
        }

    }

}
