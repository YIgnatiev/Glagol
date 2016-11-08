package net.pixeltk.glagol.adapter;

/**
 * Created by yaroslav on 21.06.16.
 */

import android.app.Activity;
import android.content.Context;
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

public class DrawerListListening extends BaseAdapter {

    private Context context;
    private ArrayList<DrawItemBookMarks> navDrawerItems;
    SuccessfulEnter myTab;

    public DrawerListListening(Context context, ArrayList<DrawItemBookMarks> navDrawerItems){
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
            convertView = mInflater.inflate(R.layout.layout_for_listening_item, null);
        }
        TextView name_author = (TextView) convertView.findViewById(R.id.name_author);
        TextView book_name = (TextView) convertView.findViewById(R.id.book_name);
        TextView name_reader = (TextView) convertView.findViewById(R.id.name_reader);
        TextView price = (TextView) convertView.findViewById(R.id.now_listen);
        ImageView cover = (ImageView) convertView.findViewById(R.id.cover);

        myTab = new SuccessfulEnter();

        name_author.setText(navDrawerItems.get(position).getName_author());
        book_name.setText(navDrawerItems.get(position).getName_book());
        name_reader.setText("Чтец: " + navDrawerItems.get(position).getName_reader());
        price.setText("  Прослушано " + String.format("%.1f", Double.parseDouble(navDrawerItems.get(position).getPrice())) + "% ");

        Glide.with(context).load(navDrawerItems.get(position).getImg_url()).into(cover);

        return convertView;
    }

}
