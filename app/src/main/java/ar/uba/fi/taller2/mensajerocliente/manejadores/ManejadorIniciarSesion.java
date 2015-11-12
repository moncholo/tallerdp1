package ar.uba.fi.taller2.mensajerocliente.manejadores;

import org.json.JSONException;
import org.json.JSONObject;

/**
*Maneja el servicio de inicio de sesion
*/
public class ManejadorIniciarSesion {

    String jSonInicioSesion;

    /**
     * Construye un manejador a partir de un JSon
     * @param jSonInicioSesion Contenido
     */
    public ManejadorIniciarSesion(String jSonInicioSesion){
        this.jSonInicioSesion = jSonInicioSesion;
    }

    /**
     *
     * @return Devuelve el estado del servicio
     */
    public String obtenerEstado(){
        String estado = "";
        JSONObject objeto = null;

        try {
            objeto = new JSONObject(this.jSonInicioSesion);
            estado = objeto.getString(APIConstantes.ESTADO);
        } catch (JSONException e) {
            e.printStackTrace();
            estado = "";
        }

        return estado;
    }

    /**
     *
     * @return Devuelve si el estado es valido
     */
    public boolean estadoValido(){
        return (this.obtenerEstado().compareTo(APIConstantes.ESTADO_VALIDO) == 0);
    }

    /**
     * Obtiene el token a partir del JSon y lo devuelve
     * @return El token obtenido
     */
    public String obtenerToken(){
        String token = "";
        JSONObject objeto = null;

        try {
            objeto = new JSONObject(this.jSonInicioSesion);
            token = objeto.getString(APIConstantes.CAMPO_TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
            token = "";
        }

        return token;
    }
}
