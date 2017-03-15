package com.example.catalina.thefastcooker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListAdapterComidas extends BaseAdapter {
    private List<ComidasList> comidas;
    private LayoutInflater inflater;


    ListAdapterComidas(List<ComidasList> co, Context context){
        comidas = co;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return comidas.size();
    }

    @Override
    public Object getItem(int position) {
        return comidas.get(position);
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
            layout = inflater.inflate(R.layout.row_listado_comidas, parent, false);
            viewHolder = new HeroViewHolder(layout);
            layout.setTag(viewHolder);
        }
        else {
            //Log.i("ListAdapter", "Reciclo una view que ha desaparecido, :)" + "donante: " + ((TextView)convertView.findViewById(R.id.text_row)).getText().toString());
            viewHolder = (HeroViewHolder) layout.getTag();
        }

        viewHolder.texto.setText(comidas.get(position).name);
        viewHolder.desc.setText(comidas.get(position).description);
        viewHolder.thumbnail.setImageBitmap(comidas.get(position).thumbnail);

        return layout;
    }

    public class HeroViewHolder {
        public TextView texto;
        public TextView desc;
        public ImageView thumbnail;

        HeroViewHolder (View view){
            texto = (TextView) view.findViewById(R.id.text_row);
            desc = (TextView) view.findViewById(R.id.text_row2);
            thumbnail = (ImageView) view.findViewById(R.id.image_row);

        }
    }
}



