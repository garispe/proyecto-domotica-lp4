package ar.edu.untref.lp4.proyectodomotica.activities;

import android.app.Activity;
import android.os.Bundle;

import ar.edu.untref.lp4.proyectodomotica.R;

public class ConfiguracionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
