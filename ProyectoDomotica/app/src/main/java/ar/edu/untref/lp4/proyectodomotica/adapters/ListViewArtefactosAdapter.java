package ar.edu.untref.lp4.proyectodomotica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Switch;
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
    private boolean estaConectado = false;

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
            viewHolder.switchArtefacto = (Switch) convertView.findViewById(R.id.switch_artefacto);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Artefacto artefacto = getItem(position);

        viewHolder.texto.setText(artefacto.getNombre());

        boolean estadoAlmacenado = DUPreferences.getBoolean(context, ArtefactosActivity.nombreHabitacion + "_" + artefacto.getNombre(), false);
        viewHolder.switchArtefacto.setChecked(estadoAlmacenado);

        if (controladorBluetooth.estaConectado()) {

            viewHolder.switchArtefacto.setEnabled(true);
            this.estaConectado = true;

        } else {

            viewHolder.switchArtefacto.setEnabled(false);
            this.estaConectado = false;

        }

        viewHolder.switchArtefacto.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  if(viewHolder.switchArtefacto.isChecked()){

                      controladorBluetooth.enviarDato("0");

                  } else {

                      controladorBluetooth.enviarDato("1");
                  }

                  artefacto.setActivo(viewHolder.switchArtefacto.isChecked());

                  DUPreferences.guardarBoolean(context, ArtefactosActivity.nombreHabitacion + "_" + artefacto.getNombre(), artefacto.isActivo());
              }
          }
        );

        return convertView;
    }

    public boolean getEstaConectado(){

        return this.estaConectado;
    }

    private class ViewHolder {

        TextView texto;
        Switch switchArtefacto;
    }
}
