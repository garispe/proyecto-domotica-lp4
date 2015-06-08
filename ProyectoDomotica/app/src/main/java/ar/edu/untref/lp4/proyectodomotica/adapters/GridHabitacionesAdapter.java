package ar.edu.untref.lp4.proyectodomotica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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

    //modifique este metodo para que no devuelva null
    @Override
    public Object getItem(int position) {
        return nombresHabitaciones.get(position);
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

        return R.drawable.cuadro128;
    }
}
