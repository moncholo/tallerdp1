package ar.uba.fi.taller2.mensajerocliente.manejadores;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by matias on 13/11/15.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PickerDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DateSetting dateSetting = new DateSetting(getActivity());
        Calendar calendar  = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog;
        dialog = new DatePickerDialog(getActivity(), dateSetting, year, month, day);

        return dialog;

    }
}
