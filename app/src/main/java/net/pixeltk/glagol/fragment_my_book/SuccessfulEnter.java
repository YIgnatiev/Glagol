package net.pixeltk.glagol.fragment_my_book;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import net.pixeltk.glagol.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 07.10.16.
 */

public class SuccessfulEnter extends Fragment {

    public SuccessfulEnter() {
        // Required empty public constructor
    }
    ViewPager viewPager;
    private SmartTabLayout viewPagerTab;
    ImageView back_arrow, logo;
    TextView name_frag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_successful_enter, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        setupViewPager(viewPager);


        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        name_frag = (TextView) view.findViewById(R.id.name_frag);
        name_frag.setText("Мои Книги");

        viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);

        inflater = LayoutInflater.from(viewPagerTab.getContext());
        final LayoutInflater finalInflater = inflater;
        viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup viewGroup, int i, PagerAdapter pagerAdapter) {
                LinearLayout view = null;
                switch (i) {
                    case 0: // Страница главная
                        view = (LinearLayout) finalInflater.inflate(R.layout.layout_tab_listening, viewGroup, false);
                        break;
                    case 1: // Страница каталога
                        view = (LinearLayout) finalInflater.inflate(R.layout.layout_tab_buy, viewGroup, false);
                        break;
                    case 2: // Страница главная
                        view = (LinearLayout) finalInflater.inflate(R.layout.layout_tab_my, viewGroup, false);
                        break;
                    case 3: // Страница каталога
                        view = (LinearLayout) finalInflater.inflate(R.layout.layout_tab_history, viewGroup, false);
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
        adapter.addFrag(new Listening(), "");
        adapter.addFrag(new Buy(), "");
        adapter.addFrag(new MyTab(), "");
        adapter.addFrag(new History(), "");
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
                    return new Listening();
                case 1:
                    return new Buy();
                case 2:
                    return new MyTab();
                case 3:
                    return new History();
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