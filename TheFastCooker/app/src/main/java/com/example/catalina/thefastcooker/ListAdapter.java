package com.example.catalina.thefastcooker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

public class ListAdapter extends BaseAdapter {
    private List<Heroes> heroes;
    private LayoutInflater inflater;


    ListAdapter (List<Heroes> hero, Context context){
        heroes = hero;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return heroes.size();
    }

    @Override
    public Object getItem(int position) {
        return heroes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = convertView;
        HeroViewHolder viewHolder = null;

        if (layout == null) {
            Log.i("ListAdapter", "No reciclo ninguna vista, tengo que hacer inflate, :(");
            layout = inflater.inflate(R.layout.row_layout, parent, false);
            viewHolder = new HeroViewHolder(layout);
            layout.setTag(viewHolder);
        }
        else {
            //Log.i("ListAdapter", "Reciclo una view que ha desaparecido, :)" + "donante: " + ((TextView)convertView.findViewById(R.id.text_row)).getText().toString());
            viewHolder = (HeroViewHolder) layout.getTag();
        }

       // int chef=heroes.get(position).getId();
        viewHolder.texto.setText(heroes.get(position).name);
        viewHolder.thumbnail.setImageBitmap(heroes.get(position).thumbnail);

        return layout;
    }

    public class HeroViewHolder {
        public TextView texto;
        public ImageView thumbnail;

        HeroViewHolder (View view){
            texto = (TextView) view.findViewById(R.id.text_row);
            thumbnail = (ImageView) view.findViewById(R.id.image_row);

        }
    }
}



