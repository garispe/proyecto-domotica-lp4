package ar.edu.untref.lp4.proyectodomotica.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBluetooth;
import ar.edu.untref.lp4.proyectodomotica.utils.MenuFlotante;

public class ConfiguracionActivity extends Activity {


    private MenuFlotante menu;
    private ControladorBluetooth controladorBluetooth = ControladorBluetooth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
//        inicializarMenu();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuracion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void inicializarMenu() {

        menu = new MenuFlotante(this);

        //BOTON LOGOFF
        //Implementar!     <-----
        menu.logoff.setClickable(true);
        menu.logoff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.botonAccionMenu.close(true);
                Toast.makeText(getApplicationContext(), "Implementar", Toast.LENGTH_SHORT).show();
            }
        });


        //BOTON ACERCA DE
        menu.about.setClickable(true);
        menu.about.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.botonAccionMenu.close(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracionActivity.this);
                builder.setMessage("Version 1.0.0")
                        .setTitle("DomUntref")
                        .setCancelable(false)
                        .setNeutralButton("Entendido",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //BOTON ESTADISTICAS
        menu.estadisticas.setClickable(true);
        menu.estadisticas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.botonAccionMenu.close(true);
                Intent intent = new Intent(ConfiguracionActivity.this, EstadisticasActivity.class);
                startActivity(intent);
            }
        });

        //BOTON CONFIGURACION
        menu.configuracion.setClickable(true);
        menu.configuracion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.botonAccionMenu.close(true);
                Intent intent = new Intent(ConfiguracionActivity.this, ConfiguracionActivity.class);
                startActivity(intent);
            }
        });

    }


    /*
    *Acción de los botones fisicos BACK y MENU
    *
    *MENU:
    *    En caso de que el menú esté cerrado, lo abre.
    *    En caso de que el menú esté abierto, lo cierra.
    *BACK:
    *    En caso de que el menú este cerado, vuelve a la pantalla anterior.
    *    En caso de que el menú esté abierto, lo cierra.
    */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (!this.menu.botonAccionMenu.isOpen()) {

                this.menu.botonAccionMenu.open(true);


            }else{

                this.menu.botonAccionMenu.close(true);

            }


        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {


            if (this.menu.botonAccionMenu.isOpen()){

                this.menu.botonAccionMenu.close(true);

            }else {

                super.onBackPressed();
            }

        }

        return true;

    }


}
