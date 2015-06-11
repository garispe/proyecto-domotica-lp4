package ar.edu.untref.lp4.proyectodomotica.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;

import java.util.List;

/**
 * Created by HoracioAlejo on 11/06/2015.
 */
public class ComandoDeVoz {

    private static final int REQUEST_CODE = 1234;
    Activity activity;

    public ComandoDeVoz (Activity activity) {
        this.activity = activity;
    }

    // Verifica que este instalado en el celular el paquete de reconocimiento de voz. En caso contrario, devuelve false.
    public boolean verificarExisteReconocimientoVoz () {
        PackageManager pm = this.activity.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void empezarReconocimientoDeVoz()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Nombre artefacto + encender/apagar");
        this.activity.startActivityForResult(intent, REQUEST_CODE);
    }

}
