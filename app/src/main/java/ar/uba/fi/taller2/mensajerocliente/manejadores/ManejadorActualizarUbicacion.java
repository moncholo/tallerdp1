package ar.uba.fi.taller2.mensajerocliente.manejadores;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *Maneja el servicio de actualizar ubicacion a partir de un Json
 *
 */
public class ManejadorActualizarUbicacion {

    String estado;


    /**
     * Constructor con la informacion de la ubicacion
     * @param json Contiene la informacion de ubicacion en formato JSon
     */
    public ManejadorActualizarUbicacion(String json){
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
     * @return Devuelve estado del servicio
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
