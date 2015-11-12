package ar.uba.fi.taller2.mensajerocliente.manejadores;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Maneja el servicio de cierre de sesion
 */
public class ManejadorCerrarSesion {

    String estado;

    /**
     * Construye un manejador a partir de un JSon
     * @param jSonCierreSesion Contenido
     */
    public ManejadorCerrarSesion(String jSonCierreSesion){
        JSONObject objeto = null;

        try {
            objeto = new JSONObject(jSonCierreSesion);
            this.estado = objeto.getString(APIConstantes.ESTADO);
        } catch (JSONException e) {
            e.printStackTrace();
            this.estado = "";
        }
    }

    /**
     *
     * @return Devuelve el estado del servicio
     */
    public String obtenerEstado(){
        return this.estado;
    }

    /**
     *
     * @return Devuelve si el estado es valido
     */
    public boolean estadoValido(){
        return (this.estado.compareTo(APIConstantes.ESTADO_VALIDO) == 0);
    }

}
