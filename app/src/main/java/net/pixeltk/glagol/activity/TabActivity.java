package net.pixeltk.glagol.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;
import net.pixeltk.glagol.fragment.CatalogFragment;
import net.pixeltk.glagol.fragment.MainContentFragment;
import net.pixeltk.glagol.fragment.MyBooks;
import net.pixeltk.glagol.fragment.OtherInfoFragment;
import net.pixeltk.glagol.fragment.PlayerFragment;

public class TabActivity extends AppCompatActivity {

    static Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.glagollogogrn);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_main));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_catalog));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_player));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_mybook));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_icon_other));

        replaceFragment(new CatalogFragment());

        tabLayout.getTabAt(3).select();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0:
                        toolbar.setLogo(R.drawable.glagollogogrn);
                        toolbar.setTitle("");
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        replaceFragment(new MainContentFragment());
                        return;
                    case 1:
                        toolbar.setLogo(null);
                        toolbar.setTitle("Каталог");
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });
                        replaceFragment(new CatalogFragment());
                        return;
                    case 2:
                        toolbar.setLogo(null);
                        toolbar.setTitle("Плеер");
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });
                        replaceFragment(new PlayerFragment());
                        return;
                    case 3:
                        toolbar.setLogo(null);
                        toolbar.setTitle("Мои книги");
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });
                        replaceFragment(new MyBooks());
                        return;
                    case 4:
                        toolbar.setLogo(null);
                        toolbar.setTitle("Еще");
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });
                        replaceFragment(new OtherInfoFragment());
                        return;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

    public static void setTitleToolbar(String title)
    {
        toolbar.setLogo(null);
        toolbar.setTitle(title);
    }

}

