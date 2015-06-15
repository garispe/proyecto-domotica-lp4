package ar.edu.untref.lp4.proyectodomotica.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import java.util.Locale;

import ar.edu.untref.lp4.proyectodomotica.R;

public class PortadaActivity extends Activity {

    private static final int DURACION = 2500; // 2.5 segundos

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portada);

        preferences = getSharedPreferences("du_preferences", Context.MODE_PRIVATE);

        String idioma = preferences.getString("idioma", null);

        if (idioma != null) {

            if (idioma.equals("ingles")) {

                Resources res = this.getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.locale = new Locale(Locale.US.getLanguage());
                res.updateConfiguration(conf, dm);

            } else {

                Resources res = this.getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.locale = new Locale(Locale.getDefault().getLanguage());
                res.updateConfiguration(conf, dm);
            }
        }

        Handler h = new Handler();

        h.postDelayed(new Runnable() {

            @Override
            public void run() {

                abrirMenu();
            }
        }, DURACION);
    }

    private void abrirMenu() {

        Intent intent = new Intent(this, HabitacionesActivity.class);
        startActivity(intent);
        finish();
    }
}
