package net.pixeltk.glagol.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.adapter.ItemData;
import net.pixeltk.glagol.adapter.RecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by Yaroslav on 04.10.2016.
 */

public class VariantFragment  extends Fragment {

    public VariantFragment() {
        // Required empty public constructor
    }
    private RecyclerView variant, news, top_sale, sale, choice_editor, soon_be;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager, mnews, mtop_sale, msale, mchoice_editor, msoon_be;
    private ArrayList<ItemData> itemData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.variant_fragment, container, false);


        variant = (RecyclerView) view.findViewById(R.id.recycler_view_variant);
        news = (RecyclerView) view.findViewById(R.id.recycler_view_news);
        top_sale = (RecyclerView) view.findViewById(R.id.recycler_view_top_sale);
        sale = (RecyclerView) view.findViewById(R.id.recycler_view_sale);
        choice_editor = (RecyclerView) view.findViewById(R.id.recycler_view_choice_editor);
        soon_be = (RecyclerView) view.findViewById(R.id.recycler_view_soon_be);

        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        variant.setHasFixedSize(true);
        news.setHasFixedSize(true);
        top_sale.setHasFixedSize(true);
        sale.setHasFixedSize(true);
        choice_editor.setHasFixedSize(true);
        soon_be.setHasFixedSize(true);

        // используем linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mnews = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mtop_sale = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        msale = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mchoice_editor = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        msoon_be = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        variant.setLayoutManager(mLayoutManager);
        news.setLayoutManager(mnews);
        top_sale.setLayoutManager(mtop_sale);
        sale.setLayoutManager(msale);
        choice_editor.setLayoutManager(mchoice_editor);
        soon_be.setLayoutManager(msoon_be);

        // создаем адаптер
        for (int i = 0; i < 5; i++) {
            itemData.add(new ItemData("Как разговаривать с кем угодно, когда угодно и где угодно", "Ларри КИнг", R.drawable.cpver));
        }
        mAdapter = new RecyclerAdapter(getActivity(), itemData);

        variant.setAdapter(mAdapter);
        news.setAdapter(mAdapter);
        top_sale.setAdapter(mAdapter);
        sale.setAdapter(mAdapter);
        choice_editor.setAdapter(mAdapter);
        soon_be.setAdapter(mAdapter);

        return view;
    }



}
