package net.pixeltk.glagol.adapter;

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
import net.pixeltk.glagol.api.Audio;

import java.util.ArrayList;


public class ChoiceListAdapterVariant extends BaseAdapter {

	private Context context;
	private ArrayList<Audio> choiceItemFromCatalogs;

	public ChoiceListAdapterVariant(Context context, ArrayList<Audio> choiceItemFromCatalogs){
		this.context = context;
		this.choiceItemFromCatalogs = choiceItemFromCatalogs;
	}

	@Override
	public int getCount() {
		return choiceItemFromCatalogs.size();
	}

	@Override
	public Object getItem(int position) {
		return choiceItemFromCatalogs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.layout_for_choise_item_variant, null);
        }
         
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.cover);
		TextView name_book = (TextView) convertView.findViewById(R.id.book_name);


		Glide.with(context).load(choiceItemFromCatalogs.get(position).getIcon()).placeholder(R.drawable.notcover).into(imgIcon);
		name_book.setText(choiceItemFromCatalogs.get(position).getName_book());



		return convertView;
	}

}
