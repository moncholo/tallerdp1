package ar.uba.fi.taller2.mensajerocliente.manejadores;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ar.uba.fi.taller2.mensajerocliente.Excepciones.ImposibleCrearConversacionException;
import ar.uba.fi.taller2.mensajerocliente.Excepciones.ImposibleLeerObjetoJsonException;

/**
* Maneja el modelo de conversaciones a partir de un JSon
 */
public class ManejadorConversacion {

    private final String CLAVE_CONVERSACION = "conversacion";
    private final int POSICION_FECHA = 0;
    private final int POSICION_USUARIO = 1;
    private final int POSICION_MENSAJE = 2;
    private Conversacion conversacion;
    private String JSonDeConversacion;
    private String conexion;

    /**
     * Crea una conversacion vacia
     */
    public ManejadorConversacion(){
        this.conversacion = null;
        this.JSonDeConversacion = "";
    }

    /**
     * Crea una conversacion a partir de un JSon
     * @param JSonDeConversacion Conversacion en formato Json
     */
    public ManejadorConversacion(String JSonDeConversacion){
        this.JSonDeConversacion = JSonDeConversacion;
        this.conversacion = new Conversacion();
    }

    /**
     * Agrega los ultimos mensajes recibidos a la conversacion
     * @return Conversacion
     * @throws ImposibleLeerObjetoJsonException
     */
    public Conversacion crearUltimosMensajes() throws ImposibleLeerObjetoJsonException {
        JSONObject objeto = null;
        JSONArray mensaje = null;
        JSONArray mensajes = null;

        try {
            objeto = new JSONObject(this.JSonDeConversacion);
            Log.i("Json", this.JSonDeConversacion);
            if(objeto.getString(APIConstantes.ESTADO).compareTo(APIConstantes.ESTADO_VALIDO)==0){
                conexion = objeto.getString(APIConstantes.CAMPO_CONEXION);
                mensajes = objeto.getJSONArray(APIConstantes.CONTENIDO);
                for (int i = 0; i < mensajes.length(); i++) {
                    mensaje = mensajes.getJSONArray(i);
                    this.conversacion.agregarMensaje(this.convertirJSonAMensaje(mensaje));
                }
            }
        } catch (JSONException e) {
            throw new ImposibleLeerObjetoJsonException();
        }

        return this.conversacion;
    }

    /**
     * Crea la conversacion a partir del JSon pasado en el constructor
     * @return Conversacion
     * @throws ImposibleCrearConversacionException
     * @throws ImposibleLeerObjetoJsonException
     */
    public Conversacion crearConversacion() throws ImposibleCrearConversacionException, ImposibleLeerObjetoJsonException {
        try {
            return this.crear();
        } catch (ImposibleLeerObjetoJsonException e) {
            return this.crearUltimosMensajes();
            //throw  new ImposibleCrearConversacionException();
        }
    }

    private Conversacion crear() throws ImposibleLeerObjetoJsonException {
        JSONObject objeto = null;
        JSONArray mensaje = null;
        JSONArray mensajes = null;


        try {
            objeto = new JSONObject(this.JSonDeConversacion);
            if(objeto.getString(APIConstantes.ESTADO).compareTo(APIConstantes.ESTADO_VALIDO)==0){
                conexion = objeto.getString(APIConstantes.CAMPO_CONEXION);
                mensajes = objeto.getJSONObject(APIConstantes.CONTENIDO).getJSONArray(CLAVE_CONVERSACION);
                for (int i = 0; i < mensajes.length(); i++) {
                    mensaje = mensajes.getJSONArray(i);
                    this.conversacion.agregarMensaje(this.convertirJSonAMensaje(mensaje));
                }
            }
        } catch (JSONException e) {
            throw new ImposibleLeerObjetoJsonException();
        }

        return this.conversacion;
    }


    /**
     * Convierte un Json de un mensaje a un objeto Mensaje
     * @param JSonMensaje Mensaje en formato Json
     * @return Mensaje
     * @throws ImposibleLeerObjetoJsonException
     */
    public Mensaje convertirJSonAMensaje(JSONArray JSonMensaje) throws ImposibleLeerObjetoJsonException {

        long fecha = 0;
        String mensaje = null;
        String usuario = null;

        try {
            fecha = JSonMensaje.getLong(this.POSICION_FECHA);
            usuario = JSonMensaje.getString(this.POSICION_USUARIO);
            mensaje = JSonMensaje.getString(this.POSICION_MENSAJE);
        } catch (JSONException e) {
            throw new ImposibleLeerObjetoJsonException();
        }

        return new Mensaje(usuario, mensaje, fecha);
    }

    /**
     * Devuelve el estado de conexion del usuario destino
     * @return Estado de conexion
     */
    public String getConexion() {
        return conexion;
    }
}
