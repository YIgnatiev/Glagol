package net.pixeltk.glagol.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.fargment_catalog.ListFragmentGlagol;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;
import net.pixeltk.glagol.fragment.MainFragment;
import net.pixeltk.glagol.fragment.MyBooks;
import net.pixeltk.glagol.fragment.OtherInfoFragment;
import net.pixeltk.glagol.fragment.PlayerFragment;

public class  TabActivity extends AppCompatActivity {

    LinearLayout main, catalog, player, book, other;
    static TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_main));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_catalog));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_player));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_mybook));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_other));

        main = (LinearLayout) tabLayout.findViewById(R.id.line1);
        catalog = (LinearLayout) tabLayout.findViewById(R.id.catalog);
        player = (LinearLayout) tabLayout.findViewById(R.id.player);
        book = (LinearLayout) tabLayout.findViewById(R.id.book);
        other = (LinearLayout) tabLayout.findViewById(R.id.other);

        replaceFragment(new MainFragment());

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new MainFragment());
                tabLayout.getTabAt(0).select();
            }
        });

        catalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new ListFragmentGlagol());
                tabLayout.getTabAt(1).select();
            }
        });

        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new PlayerFragment());
                tabLayout.getTabAt(2).select();
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new MyBooks());
                tabLayout.getTabAt(3).select();
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new OtherInfoFragment());
                tabLayout.getTabAt(4).select();
            }
        });

    }
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment: fm.getFragments()) {
            if (fragment instanceof  OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
        }
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment);

        transaction.commit();
    }
    public static void changePage()
    {
        tabLayout.getTabAt(0).select();
    }

}

