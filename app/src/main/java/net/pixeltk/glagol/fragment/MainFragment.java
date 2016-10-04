package net.pixeltk.glagol.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.SearchView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.activity.TabActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 04.10.16.
 */

public class MainFragment extends Fragment{

    public MainFragment() {
        // Required empty public constructor
    }

    SearchView searchView;
    ViewPager viewPager;
    private SmartTabLayout viewPagerTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        searchView=(SearchView) view.findViewById(R.id.searchView);
        searchView.setQueryHint("Поиск");
        searchView.setHovered(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getActivity(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getActivity(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });


        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        setupViewPager(viewPager);

        viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);

        inflater = LayoutInflater.from(viewPagerTab.getContext());
        final LayoutInflater finalInflater = inflater;
        viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup viewGroup, int i, PagerAdapter pagerAdapter) {
                LinearLayout view = null;
                switch (i) {
                    case 0: // Страница главная
                        view = (LinearLayout) finalInflater.inflate(R.layout.layout_tab_text_variant, viewGroup, false);
                        break;
                    case 1: // Страница каталога
                        view = (LinearLayout) finalInflater.inflate(R.layout.layout_tab_text_news, viewGroup, false);
                        break;
                }
                return view;
            }
        });


        viewPagerTab.setViewPager(viewPager);
        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new VariantFragment(), "");
        adapter.addFrag(new NewsFragment(), "");
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
                    return new VariantFragment();
                case 1:
                    return new NewsFragment();
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
