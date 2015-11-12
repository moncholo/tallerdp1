package ar.uba.fi.taller2.mensajerocliente.utilidades;

import android.content.Context;
import android.content.SharedPreferences;

import ar.uba.fi.taller2.mensajerocliente.manejadores.APIConstantes;

/**
 * Maneja la persistencia del token.
 */
public class ManejadorDeToken {
    /**
     *
     * @param context Activity en el que se encuentra
     * @return Token almacenado por la aplicacion
     */
    public static String obtenerToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences(APIConstantes.PERSISTENCIA_DATOS, 0);

        return settings.getString(APIConstantes.PERSISTENCIA_TOKEN, null);
    }

    /**
     *
     * @param context Activity en el que se encuentra
     * @return Nombre del usuario
     */
    public static String obtenerUsuario(Context context) {
        SharedPreferences settings = context.getSharedPreferences(APIConstantes.PERSISTENCIA_DATOS, 0);

        return settings.getString(APIConstantes.PERSISTENCIA_USUARIO, null);
    }

    /***
     * Guarda el token en la memoria
     * @param context Activity en el que se encuentra
     * @param valor Token a guardar
     */
    public static void persistirToken(Context context, String valor) {
        SharedPreferences settings = context.getSharedPreferences(APIConstantes.PERSISTENCIA_DATOS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(APIConstantes.PERSISTENCIA_TOKEN, valor);

        editor.commit();
    }

    /**
     * Guarda el nombre de usuario en la memoria
     * @param context Activity en el que se encuentra
     * @param valor Nombre a guardar
     */
    public static void persistirUsuario(Context context, String valor) {
        SharedPreferences settings = context.getSharedPreferences(APIConstantes.PERSISTENCIA_DATOS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(APIConstantes.PERSISTENCIA_USUARIO, valor);

        editor.commit();
    }

    /**
     * Borra ele nombre de usuario de la memoria
     * @param context Activity en el que se encuentra
     */
    public static void borrarUsuario(Context context) {
        SharedPreferences settings = context.getSharedPreferences(APIConstantes.PERSISTENCIA_DATOS, 0);
        settings.edit().remove(APIConstantes.PERSISTENCIA_USUARIO).commit();
    }

    /**
     * Borra el token de la memoria
     * @param context Activity en el que se encuentra
     */
    public static void borrarToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences(APIConstantes.PERSISTENCIA_DATOS, 0);
        settings.edit().remove(APIConstantes.PERSISTENCIA_TOKEN).commit();
    }
}
