package ar.edu.untref.lp4.proyectodomotica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.activities.ArtefactosActivity;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBluetooth;
import ar.edu.untref.lp4.proyectodomotica.modelos.Artefacto;
import ar.edu.untref.lp4.proyectodomotica.utils.DUPreferences;

public class ListViewArtefactosAdapter extends BaseAdapter {

    private Context context;
    private List<Artefacto> artefactos;
    private ControladorBluetooth controladorBluetooth = ControladorBluetooth.getInstance();

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

        // SE CONSULTA EL ESTADO ALMACENADO PARA CARGAR LA IMAGEN AL CREAR LA ACTIVIDAD
        if (DUPreferences.getBoolean(context, ArtefactosActivity.nombreHabitacion + "_" + getItem(position).getNombre(), false)) {

            viewHolder.boton.setImageResource(R.drawable.imagen_on);

        } else {

            viewHolder.boton.setImageResource(R.drawable.imagen_off);
        }

        viewHolder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (controladorBluetooth.estaConectado()) {

                    Artefacto artefacto = getItem(position);

                    if (!artefacto.isActivo()) {

                        artefacto.setActivo(true);
                        controladorBluetooth.enviarDato("1");

                    } else {

                        artefacto.setActivo(false);
                        controladorBluetooth.enviarDato("0");
                    }

                    setEstadoImagen(artefacto, viewHolder.boton);

                    // SE ALMACENA EL VALOR EN LAS PREFERENCIAS
                    DUPreferences.guardarBoolean(context, ArtefactosActivity.nombreHabitacion + "_" + artefacto.getNombre(), artefacto.isActivo());

                } else {

                    Toast.makeText(context, context.getString(R.string.verificar_conexion), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    private void setEstadoImagen(Artefacto artefacto, ImageButton boton) {

        int imagen;

        if (artefacto.isActivo()) {

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
