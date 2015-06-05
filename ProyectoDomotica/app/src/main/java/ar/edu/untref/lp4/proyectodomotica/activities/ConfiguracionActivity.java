package ar.edu.untref.lp4.proyectodomotica.activities;

import android.app.Activity;
import android.os.Bundle;

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

    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
