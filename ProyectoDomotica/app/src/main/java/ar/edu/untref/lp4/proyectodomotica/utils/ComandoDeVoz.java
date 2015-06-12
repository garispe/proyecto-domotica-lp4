package ar.edu.untref.lp4.proyectodomotica.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HoracioAlejo on 11/06/2015.
 */
public class ComandoDeVoz extends Activity {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    Activity activity;
    private ListView listaDePalabras;
    public String palabra="vacio";

    public ComandoDeVoz (Activity activity) {
        this.activity = activity;
    }

    public int getRequestCode () {
        return VOICE_RECOGNITION_REQUEST_CODE;
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
        this.activity.startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    /*
    El metodo de abajo es el que no logro entender como funciona ni como usarlo...
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == this.activity.RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            listaDePalabras.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, matches));
            if(matches.size()>0){
                palabra=matches.get(0).toString();
            }
            else{
                palabra="default";
            }
        }
        else{
            palabra="default";
        }

    }

    public String getPalabra(){
        return palabra;
    }

}