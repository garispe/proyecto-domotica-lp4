package ar.edu.untref.lp4.proyectodomotica.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.activities.ArtefactosActivity;
import ar.edu.untref.lp4.proyectodomotica.activities.HabitacionesActivity;
import ar.edu.untref.lp4.proyectodomotica.utils.Constantes;

public class Temporizador extends Service {

    Activity activity;

    public Temporizador(ArtefactosActivity activity) {
        this.activity = activity;
    }

    public Temporizador (HabitacionesActivity activity) {
        this.activity = activity;
    }

    public void alertaTemporizador () {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.activity);
        alertDialog.setTitle(R.string.temporizador);
        alertDialog.setMessage(R.string.mensaje_temporizador);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        final EditText hora = new EditText(this.activity);
        hora.setHint(R.string.hora);
        hora.setLayoutParams(lp);
        hora.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constantes.MAX_ANCHO_HORA)});
        final EditText minuto = new EditText(this.activity);
        minuto.setHint(R.string.minutos);
        minuto.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constantes.MAX_ANCHO_MINUTOS)});
        minuto.setLayoutParams(lp);
        hora.setInputType(InputType.TYPE_CLASS_NUMBER);
        minuto.setInputType(InputType.TYPE_CLASS_NUMBER);

        LinearLayout lay = new LinearLayout(this.activity);
        lay.setOrientation(LinearLayout.HORIZONTAL);
        lay.addView(hora);
        lay.addView(minuto);
        alertDialog.setView(lay);

        alertDialog.setPositiveButton(R.string.aceptar,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.setNegativeButton(R.string.cancelar,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY; // El service no se reinicia una vez terminado
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }
}