package ar.edu.untref.lp4.proyectodomotica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_artefactos, null);
            viewHolder.texto = (TextView) convertView.findViewById(R.id.item_text);
            viewHolder.boton = (ImageButton) convertView.findViewById(R.id.item_boton);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.texto.setText(getItem(position).getNombre());
        viewHolder.boton.setImageResource(R.drawable.imagen_off);
        viewHolder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEstadoImagen(artefactos.get(position), v);
            }
        });
        return convertView;
    }

    private void setEstadoImagen(Artefacto artefacto, View view){

        int imagen;
        ImageButton boton = (ImageButton) view;

        if(artefacto.isActivo()){

            imagen = R.drawable.imagen_on;
            boton.setImageResource(imagen);

        } else {

            imagen = R.drawable.imagen_off;
            boton.setImageResource(imagen);
        }
    }

    private class ViewHolder {

        TextView texto;
        ImageButton boton;
    }
}
