package ar.edu.untref.lp4.proyectodomotica.adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;

import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.activities.ArtefactosActivity;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBaseDatos;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBluetooth;
import ar.edu.untref.lp4.proyectodomotica.modelos.Artefacto;

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

                final int pos = position;

                new BottomSheet.Builder(artefactosActivity).title(R.string.opcion).sheet(R.menu.menu_opciones_artefacto).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case R.id.eliminar:
                                artefactosActivity.borrarArtefacto(artefacto);
                                break;

                            case R.id.editar:
                                artefactosActivity.editarArtefacto(artefacto);
                                break;
                        }
                    }
                }).show();

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

                                                              if (pin < 10) {

                                                                  dato = "0" + dato;
                                                              }

                                                              if (viewHolder.switchArtefacto.isChecked()) {

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

    public boolean getEstaConectado() {

        return this.estaConectado;
    }


    private class ViewHolder {

        TextView texto;
        Switch switchArtefacto;
    }
}
