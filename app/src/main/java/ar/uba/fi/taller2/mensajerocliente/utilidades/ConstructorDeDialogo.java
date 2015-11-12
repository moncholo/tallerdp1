package ar.uba.fi.taller2.mensajerocliente.utilidades;

import android.app.ProgressDialog;
import android.content.Context;

import ar.uba.fi.taller2.mensajerocliente.R;

/**
 * Maneja dialogos de Android
 */
public class ConstructorDeDialogo {
    /**
     * Obtiene un ProgressDialog a partir del activity en el que se encuentra
     * @param context Activity en el que se encuentra
     * @return ProgressDialog creado
     */
    public static ProgressDialog obtener(Context context) {
        ProgressDialog dialogo = new ProgressDialog(context);
        dialogo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogo.setMessage(context.getResources().getString(R.string.cargando));
        dialogo.setIndeterminate(true);
        dialogo.setCanceledOnTouchOutside(false);

        return dialogo;
    }
}
