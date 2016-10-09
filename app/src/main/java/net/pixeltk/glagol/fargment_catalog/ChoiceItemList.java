package net.pixeltk.glagol.fargment_catalog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.adapter.ChoiceItemFromCatalog;
import net.pixeltk.glagol.adapter.ChoiceListAdapter;
import net.pixeltk.glagol.fragment_my_book.CheckLoginSign;

import java.util.ArrayList;

/**
 * Created by root on 07.10.16.
 */

public class ChoiceItemList extends Fragment implements OnBackPressedListener {

    public ChoiceItemList() {
        // Required empty public constructor
    }

    Fragment fragment = null;
    String[] sort = {"Сортировка", "По цене", "По имени"};
    Spinner sort_spinner;
    private ArrayList<ChoiceItemFromCatalog> choiceItemFromCatalogs;
    private ChoiceListAdapter choiceListAdapter;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choise_item, container, false);

        sort_spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sort);
        sort_spinner.setAdapter(adapter);

        listView = (ListView)  view.findViewById(R.id.list_choice_item);

        choiceItemFromCatalogs = new ArrayList<ChoiceItemFromCatalog>();

        for (int i = 0; i < 5; i++) {
            choiceItemFromCatalogs.add(new ChoiceItemFromCatalog(R.drawable.cpver, "Ларри Кинг", "Как разговаривать нормально", "Иванов иван", "149"));
        }

        choiceListAdapter = new ChoiceListAdapter(getActivity(), choiceItemFromCatalogs);
        listView.setAdapter(choiceListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
    @Override
    public void onBackPressed() {
        fragment = new ListFragmentGlagol();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.catalog_frame, fragment).commit();
        }

    }


}
