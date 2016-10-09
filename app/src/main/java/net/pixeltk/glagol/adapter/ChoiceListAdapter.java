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

import java.util.ArrayList;


public class ChoiceListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ChoiceItemFromCatalog> choiceItemFromCatalogs;

	public ChoiceListAdapter(Context context, ArrayList<ChoiceItemFromCatalog> choiceItemFromCatalogs){
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
            convertView = mInflater.inflate(R.layout.layout_for_choise_item, null);
        }
         
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.cover);
        TextView name_author = (TextView) convertView.findViewById(R.id.name_author);
		TextView name_book = (TextView) convertView.findViewById(R.id.book_name);
		TextView reader = (TextView) convertView.findViewById(R.id.name_reader);
		TextView price = (TextView) convertView.findViewById(R.id.price);

		Glide.with(context).load(choiceItemFromCatalogs.get(position).getIcon()).into(imgIcon);
      	name_author.setText(choiceItemFromCatalogs.get(position).getAuthor());
		name_book.setText(choiceItemFromCatalogs.get(position).getBook_name());
		reader.setText("Чтец: " + choiceItemFromCatalogs.get(position).getName_reader());
		price.setText(choiceItemFromCatalogs.get(position).getPrice() + " p.");



		return convertView;
	}

}
