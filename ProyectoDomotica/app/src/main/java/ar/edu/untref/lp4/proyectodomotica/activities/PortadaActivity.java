package ar.edu.untref.lp4.proyectodomotica.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ar.edu.untref.lp4.proyectodomotica.R;

public class PortadaActivity extends Activity {

    private static final int DURACION = 2500; // 2.5 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portada);

        Handler h = new Handler();

        h.postDelayed(new Runnable() {

            @Override
            public void run() {

                abrirMenu();
            }
        }, DURACION);
    }

    private void abrirMenu(){

        Intent intent = new Intent(this, HabitacionesActivity.class);
        startActivity(intent);
        finish();
    }
}
