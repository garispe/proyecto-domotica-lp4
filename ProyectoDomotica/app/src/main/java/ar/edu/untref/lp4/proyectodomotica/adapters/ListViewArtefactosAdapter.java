package ar.edu.untref.lp4.proyectodomotica.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBaseDatos;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBluetooth;
import ar.edu.untref.lp4.proyectodomotica.modelos.Artefacto;
import ar.edu.untref.lp4.proyectodomotica.utils.DUPreferences;

public class ListViewArtefactosAdapter extends BaseAdapter {

    private ArtefactosActivity artefactosActivity;
    private List<Artefacto> artefactos;
    private ControladorBluetooth controladorBluetooth = ControladorBluetooth.getInstance();
    private boolean estaConectado = false;

    public ListViewArtefactosAdapter(ArtefactosActivity artefactosActivity, List<Artefacto> artefactos) {

        this.artefactosActivity = artefactosActivity;
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

            convertView = LayoutInflater.from(artefactosActivity).inflate(R.layout.item_list_artefactos, null);
            viewHolder.texto = (TextView) convertView.findViewById(R.id.item_text);
            viewHolder.switchArtefacto = (Switch) convertView.findViewById(R.id.switch_artefacto);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Artefacto artefacto = getItem(position);

        viewHolder.texto.setText(artefacto.getNombre());
        viewHolder.texto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                borrarArtefacto(artefacto);
                return true;
            }
        });

        boolean estadoAlmacenado = ControladorBaseDatos.getEstadoArtefacto(artefacto);
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

                  // FixMe:
                  // ACA NO LEE BIEN LOS DATOS DEL getIdPin()
                  Integer pin = artefacto.getIdPin();
                  String dato = pin.toString();

                  if(pin < 10){

                      dato = "0" + dato;
                  }

                  if(viewHolder.switchArtefacto.isChecked()){

                      controladorBluetooth.enviarDato(dato + "1");

                  } else {

                      controladorBluetooth.enviarDato(dato + "0");
                  }

                  artefacto.setActivo(viewHolder.switchArtefacto.isChecked());

                  ControladorBaseDatos.actualizarEstadoArtefacto(artefacto);
              }
          }
        );

        return convertView;
    }

    public boolean getEstaConectado(){

        return this.estaConectado;
    }

    private void borrarArtefacto(final Artefacto artefacto) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(artefactosActivity);
        alertDialog.setTitle(artefactosActivity.getString(R.string.eliminar_artefacto_titulo));
        alertDialog.setMessage(artefactosActivity.getString(R.string.eliminar_artefacto_mensaje));


        alertDialog.setPositiveButton(artefactosActivity.getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ControladorBaseDatos.eliminarArtefacto(artefacto);
                        artefactosActivity.refresh();
                    }
                });

        alertDialog.setNegativeButton(artefactosActivity.getString(R.string.cancelar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private class ViewHolder {

        TextView texto;
        Switch switchArtefacto;
    }
}
