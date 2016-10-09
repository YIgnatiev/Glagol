package net.pixeltk.glagol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableRow;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;
import net.pixeltk.glagol.fragment.CatalogFragment;
import net.pixeltk.glagol.fragment.MainFragment;
import net.pixeltk.glagol.fragment.MyBooks;
import net.pixeltk.glagol.fragment.OtherInfoFragment;
import net.pixeltk.glagol.fragment.PlayerFragment;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    private SmartTabLayout viewPagerTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.glagollogogrn);
        setSupportActionBar(toolbar);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        setupViewPager(viewPager);
       viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);

        final LayoutInflater inflater = LayoutInflater.from(viewPagerTab.getContext());
        viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup viewGroup, int i, PagerAdapter pagerAdapter) {
                LinearLayout view = null;
                switch (i) {
                    case 0: // Страница главная
                        view = (LinearLayout) inflater.inflate(R.layout.layout_tab_icon_main, viewGroup, false);
                        break;
                    case 1: // Страница каталога
                        view = (LinearLayout) inflater.inflate(R.layout.layout_tab_icon_catalog, viewGroup, false);
                        break;
                    case 2: // Страница плеера
                        view = (LinearLayout) inflater.inflate(R.layout.layout_tab_icon_player, viewGroup, false);
                        break;
                    case 3: // Страница моих книг
                        view = (LinearLayout) inflater.inflate(R.layout.layout_tab_icon_mybook, viewGroup, false);
                        break;
                    case 4: // Страница доп инф
                        view = (LinearLayout) inflater.inflate(R.layout.layout_tab_icon_other, viewGroup, false);
                        break;
                }
                return view;
            }
        });

        viewPagerTab.setViewPager(viewPager);

        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Log.d("MyLog", String.valueOf(position));
                switch (position) {
                    case 0:
                        Log.d("MyLog", String.valueOf(position));
                        toolbar.setLogo(R.drawable.glagollogogrn);
                        toolbar.setTitle("");
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        return;
                    case 1:
                        Log.d("MyLog", String.valueOf(position));
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


                        return;
                    case 2:
                        Log.d("MyLog", String.valueOf(position));
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
                        return;
                    case 4:
                        Log.d("MyLog", String.valueOf(position));
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
                        return;
                    default:
                        return;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
            viewPager.setCurrentItem(0);
        }
    }

    public void player()
    {
        viewPager.setCurrentItem(2);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MainFragment(), "");
        adapter.addFrag(new CatalogFragment(), "");
        adapter.addFrag(new PlayerFragment(), "");
        adapter.addFrag(new MyBooks(), "");
        adapter.addFrag(new OtherInfoFragment(), "");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new MainFragment();
                case 1:
                    return new CatalogFragment();
                case 2:
                    return new PlayerFragment();
                case 3:
                    return new MyBooks();
                case 4:
                    return new OtherInfoFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}

