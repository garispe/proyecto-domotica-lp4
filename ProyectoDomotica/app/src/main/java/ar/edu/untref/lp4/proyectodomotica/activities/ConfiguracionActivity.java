package ar.edu.untref.lp4.proyectodomotica.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;

import ar.edu.untref.lp4.proyectodomotica.R;

public class ConfiguracionActivity extends ActionBarActivity {

    private ImageButton bVaciarBD;
    private ImageButton bDomotica;
    private ImageButton bEspanol;
    private ImageButton bIngles;

    private boolean tachoLleno = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        bVaciarBD = (ImageButton) findViewById(R.id.boton_vaciar_bd);
        bDomotica = (ImageButton) findViewById(R.id.boton_domotica);
        bEspanol = (ImageButton) findViewById(R.id.boton_espanol);
        bIngles = (ImageButton) findViewById(R.id.boton_ingles);

        bEspanol.setImageResource(R.drawable.bandera_spa);
        bIngles.setImageResource(R.drawable.bandera_ing);
        bVaciarBD.setImageResource(R.drawable.tacho);
        bDomotica.setImageResource(R.drawable.info_domotica);

        bVaciarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Borra la base de datos y cambia la imagen
                if(!tachoLleno){

                    bVaciarBD.setImageResource(R.drawable.tacho_lleno);
                    tachoLleno = true;

                } else {

                    bVaciarBD.setImageResource(R.drawable.tacho);
                    tachoLleno = false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
