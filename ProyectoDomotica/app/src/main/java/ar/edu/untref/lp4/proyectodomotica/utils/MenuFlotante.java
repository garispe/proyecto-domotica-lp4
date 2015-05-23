package ar.edu.untref.lp4.proyectodomotica.utils;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import ar.edu.untref.lp4.proyectodomotica.R;

public class MenuFlotante {

    private Activity activity;

    public MenuFlotante(Activity activity) {

        this.activity = activity;

        // Se crean las instancias del menu.
        final ImageView iconNew = new ImageView(activity);
        iconNew.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_settings));

        final FloatingActionButton botonMenu = new FloatingActionButton.Builder(activity)
                .setContentView(iconNew)
                .setBackgroundDrawable(R.drawable.button_action_blue_selector)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_LEFT)
                .build();

        SubActionButton.Builder subActBuilder = new SubActionButton.Builder(activity);
        subActBuilder.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.button_action_blue_selector));

        //Declaro los iconos que voy a usar en el menu
        ImageView icono1 = new ImageView(activity);
        ImageView icono2 = new ImageView(activity);
        ImageView icono3 = new ImageView(activity);
        ImageView icono4 = new ImageView(activity);

        //Seteo las imagenes que contendras los iconos del sub menu
        icono1.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_settings));
        icono2.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_settings));
        icono3.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_settings));
        icono4.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_settings));

        // Se setan 4 botones en el botonMenu
        final FloatingActionMenu botonAccionMenu = new FloatingActionMenu.Builder(activity)
                .addSubActionView(subActBuilder.setContentView(icono1).build())
                .addSubActionView(subActBuilder.setContentView(icono2).build())
                .addSubActionView(subActBuilder.setContentView(icono3).build())
                .addSubActionView(subActBuilder.setContentView(icono4).build())
                .setRadius(140) //Define el radio de despligue del menu
                .setStartAngle(0) //Define a donde arranca el menu que se despliega.
                .setEndAngle(-90) //Define para donde se va a abrir el abanico de opciones.
                .attachTo(botonMenu)
                .build();

        // Aca estan los eventos de las animaciones para abrir y cerrar el botonMenu.
        botonAccionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rota el dibujo que se encuentra en el botonMenu 45 grados a favor del reloj
                iconNew.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(iconNew, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rota el dibujo que se encuentra en el botonMenu 45 grados contra reloj
                iconNew.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(iconNew, pvhR);
                animation.start();
            }
        });
    }
}
