package ar.uba.fi.taller2.mensajerocliente.manejadores;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by matias on 13/11/15.
 */
public class DateSetting implements DatePickerDialog.OnDateSetListener {


    Context context;

    public DateSetting(Context ctx){
        this.context = ctx;
    }

    /**
     * @param view        The view associated with this listener.
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility
     *                    with {@link Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


        int month = dayOfMonth + 1;
        Toast.makeText(this.context, "Fecha seleccionada: " + month + "/" + monthOfYear + "/" + year ,
                Toast.LENGTH_LONG).show();

    }
}
