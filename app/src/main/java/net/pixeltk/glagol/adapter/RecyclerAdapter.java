package net.pixeltk.glagol.adapter;

/**
 * Created by root on 05.10.16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.api.Audio;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Audio> itemsData;
    private Context context;


    // класс view holder-а с помощью которого мы получаем ссылку на каждый элемент
    // отдельного пункта списка
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // наш пункт состоит только из одного TextView
        TextView book_name, author;
        ImageView cover;

        public ViewHolder(View v) {
            super(v);
            book_name = (TextView) v.findViewById(R.id.book_name);
            author = (TextView) v.findViewById(R.id.author);
            cover = (ImageView) v.findViewById(R.id.cover);
        }
    }

    // Конструктор
    public RecyclerAdapter(Context context, ArrayList<Audio> itemDatas)  {
        this.context = context;
        this.itemsData = itemDatas;
    }

    // Создает новые views (вызывается layout manager-ом)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recycle_item, parent, false);

        // тут можно программно менять атрибуты лэйаута (size, margins, paddings и др.)

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.book_name.setText(itemsData.get(position).getName_book());
        holder.author.setText(itemsData.get(position).getName_authors());
        Glide.with(context).load(itemsData.get(position).getIcon()).placeholder(R.drawable.notcover).into(holder.cover);


    }

    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}