package ar.edu.untref.lp4.proyectodomotica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.modelos.Artefacto;

public class ListViewArtefactosAdapter extends BaseAdapter {

    private Context context;
    private List<Artefacto> artefactos;

    public ListViewArtefactosAdapter(Context context, List<Artefacto> artefactos) {

        this.context = context;
        this.artefactos = artefactos;
    }

    @Override
    public int getCount() {
        return this.artefactos.size();
    }

    @Override
    public Artefacto getItem(int position) {
        return this.artefactos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_artefactos, null);
            viewHolder.texto = (TextView) convertView.findViewById(R.id.item_text);
            viewHolder.imagen = (ImageView) convertView.findViewById(R.id.item_image);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.texto.setText(getItem(position).getNombre());
        viewHolder.imagen.setImageResource(getImagenArtefacto(artefactos.get(position)));

        return convertView;
    }

    private int getImagenArtefacto(Artefacto artefacto) {

        int imagen;

        if (artefacto.isActivo()) {

            imagen = R.drawable.imagen_on;

        } else {

            imagen = R.drawable.imagen_off;
        }

        return imagen;
    }

    private class ViewHolder {

        TextView texto;
        ImageView imagen;
    }
}
