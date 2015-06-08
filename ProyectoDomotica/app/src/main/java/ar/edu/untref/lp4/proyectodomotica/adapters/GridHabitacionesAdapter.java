package ar.edu.untref.lp4.proyectodomotica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.modelos.Habitacion;

/**
 * Adapter para GridView personalizado con imagen y texto.
 */
public class GridHabitacionesAdapter extends BaseAdapter {

    private Context context;
    private final List<Habitacion> nombresHabitaciones;

    public GridHabitacionesAdapter(Context context, List<Habitacion> nombres) {

        this.context = context;
        this.nombresHabitaciones = nombres;
    }

    @Override
    public int getCount() {
        return nombresHabitaciones.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = inflater.inflate(R.layout.item_grid_habitaciones, null);

            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);

            textView.setText(nombresHabitaciones.get(position).getNombre());
            imageView.setImageResource(getImagenAleatoria());

        } else {

            grid = convertView;
        }

        return grid;
    }


    private int getImagenAleatoria() {


        Double color = Math.random() * 10;
        int id = 0;

        if (color.intValue() > 0 && color.intValue() <= 6) {

            switch (color.intValue()) {

                case 1:
                    id = R.drawable.color_amarillo;
                    break;
                case 2:
                    id = R.drawable.color_azul;
                    break;

                case 3:
                    id = R.drawable.color_gris;
                    break;

                case 4:
                    id = R.drawable.color_purpura;
                    break;

                case 5:
                    id = R.drawable.color_rojo;
                    break;

                case 6:
                    id = R.drawable.color_verde;
                    break;
            }
        } else {

            id = R.drawable.color_verde;
        }

        return id;
    }
}
