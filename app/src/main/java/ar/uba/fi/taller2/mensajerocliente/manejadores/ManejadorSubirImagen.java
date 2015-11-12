package ar.uba.fi.taller2.mensajerocliente.manejadores;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Maneja el servicio de subida de imagen.
 */
public class ManejadorSubirImagen {

    String estado;

    /**
     * Construye un manejador a partir de un JSon
     * @param json Contenido
     */
    public ManejadorSubirImagen(String json){
        JSONObject objeto = null;

        try {
            objeto = new JSONObject(json);
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
