package net.pixeltk.glagol.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.activity.TabActivity;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;

/**
 * Created by root on 04.10.16.
 */

public class OtherInfoFragment extends Fragment implements OnBackPressedListener{

    public OtherInfoFragment() {
        // Required empty public constructor
    }
    ImageView back_arrow, logo;
    TextView name_frag;
    Fragment fragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.otherinfo_fragment, container, false);

        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        name_frag = (TextView) view.findViewById(R.id.name_frag);
        name_frag.setText("Еще");
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_arrow.setBackgroundResource(R.drawable.bakground_arrow);
                onBackPressed();
            }
        });
        return view;
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
