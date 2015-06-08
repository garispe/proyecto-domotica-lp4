package ar.edu.untref.lp4.proyectodomotica.activities;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

import ar.edu.untref.lp4.proyectodomotica.BuildConfig;
import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBaseDatos;
import ar.edu.untref.lp4.proyectodomotica.modelos.Artefacto;

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

        bDomotica.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ConfiguracionActivity.this);
                builder.setMessage(R.string.mensaje_domotica)
                        .setTitle(getString(R.string.domotica))
                        .setCancelable(false)
                        .setNeutralButton(getString(R.string.entendido),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });

        bVaciarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Borra la base de datos y cambia la imagen
                if (!tachoLleno) {

                    bVaciarBD.setImageResource(R.drawable.tacho_lleno);
                    tachoLleno = true;

                } else {

                    bVaciarBD.setImageResource(R.drawable.tacho);
                    tachoLleno = false;
                }
            }
        });

        bEspanol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ConfiguracionActivity.this, "Proximamente lenguaje en espanol", Toast.LENGTH_SHORT).show();

            }
        });

        bIngles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ConfiguracionActivity.this, "Proximamente lenguaje en ingles", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
