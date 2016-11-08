package net.pixeltk.glagol.adapter;

/**
 * Created by yaroslav on 21.06.16.
 */
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.fragment_my_book.SuccessfulEnter;

import java.util.ArrayList;

public class DrawerListBookMarks extends BaseAdapter {

    private Context context;
    private ArrayList<DrawItemBookMarks> navDrawerItems;
    SuccessfulEnter myTab;

    public DrawerListBookMarks(Context context, ArrayList<DrawItemBookMarks> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.layout_for_bookmarks_item, null);
        }
        TextView name_author = (TextView) convertView.findViewById(R.id.name_author);
        TextView book_name = (TextView) convertView.findViewById(R.id.book_name);
        TextView name_reader = (TextView) convertView.findViewById(R.id.name_reader);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        ImageView cover = (ImageView) convertView.findViewById(R.id.cover);
        ImageView dellete = (ImageView) convertView.findViewById(R.id.dellete);
        myTab = new SuccessfulEnter();

        name_author.setText(navDrawerItems.get(position).getName_author());
        book_name.setText(navDrawerItems.get(position).getName_book());
        name_reader.setText("Чтец: " + navDrawerItems.get(position).getName_reader());
        price.setText("  " + navDrawerItems.get(position).getPrice() + " .p ");

        Glide.with(context).load(navDrawerItems.get(position).getImg_url()).into(cover);

        dellete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MyLog", navDrawerItems.get(position).getName_book());
                DataBasesHelper databaseshelper = new DataBasesHelper(context);
                SQLiteDatabase db = databaseshelper.getWritableDatabase();
                db.delete("Bookmarks", "id_book = " + navDrawerItems.get(position).getId(), null);
                navDrawerItems.remove(position);
                myTab.dataChanged();
                databaseshelper.close();
            }
        });

        return convertView;
    }

}
