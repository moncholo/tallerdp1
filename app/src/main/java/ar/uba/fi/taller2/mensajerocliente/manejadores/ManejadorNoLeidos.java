package ar.uba.fi.taller2.mensajerocliente.manejadores;

import com.google.android.gms.maps.model.internal.IPolylineDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

import ar.uba.fi.taller2.mensajerocliente.Excepciones.ImposibleCrearConversacionException;
import ar.uba.fi.taller2.mensajerocliente.Excepciones.ImposibleLeerObjetoJsonException;

/**
 * Maneja el servicio de mensajes no leidos
 */
public class ManejadorNoLeidos {

    private final String CLAVE_CONVERSACION = "conversacion";
    private final int POSICION_FECHA = 0;
    private final int POSICION_USUARIO = 1;
    private final int POSICION_MENSAJE = 2;
    private Conversacion conversacion;
    private String JSonDeConversacion;

    /**
     * Crea una conversacion vacia
     */
    public ManejadorNoLeidos(){
        this.conversacion = null;
        this.JSonDeConversacion = "";
    }

    /**
     * Crea una conversacion con mensajes no leidos a partir de un JSon
     * @param JSonDeConversacion Json que contiene los mensajes no leidos
     */
    public ManejadorNoLeidos(String JSonDeConversacion){
        this.JSonDeConversacion = JSonDeConversacion;
        this.conversacion = new Conversacion();
    }

    /**
     *
     * @return Devuelve si hay algun nuevo mensaje
     * @throws ImposibleLeerObjetoJsonException
     */
    public boolean hayNotificaciones() throws ImposibleLeerObjetoJsonException {
        JSONObject objeto = null;
        JSONArray mensaje = null;

        try {
            objeto = new JSONObject(this.JSonDeConversacion);
            if(objeto.getString(APIConstantes.ESTADO).compareTo(APIConstantes.ESTADO_VALIDO)==0){
                JSONArray mensajesDeAlgunUsuario = objeto.getJSONArray(APIConstantes.CONTENIDO);
                if (mensajesDeAlgunUsuario.length()>0){
                    return true;
                }
            }
        } catch (JSONException e) {
            throw new ImposibleLeerObjetoJsonException();
        }

        return false;
    }

    /**
     * @return Conversacion creada y editada
     * @throws ImposibleLeerObjetoJsonException
     */
    public Conversacion obtener() throws ImposibleLeerObjetoJsonException {
        JSONObject objeto = null;
        JSONArray mensaje = null;

        try {
            objeto = new JSONObject(this.JSonDeConversacion);
            if(objeto.getString(APIConstantes.ESTADO).compareTo(APIConstantes.ESTADO_VALIDO)==0){

                JSONArray mensajesDeAlgunUsuario = objeto.getJSONArray(APIConstantes.CONTENIDO);

                for (int i = 0; i < mensajesDeAlgunUsuario.length(); i++) {

                    JSONArray mensajes = mensajesDeAlgunUsuario.getJSONArray(i);

                    for (int j = 0; j < mensajes.length(); j++) {
                        mensaje = mensajes.getJSONArray(j);
                        this.conversacion.agregarMensaje(this.convertirJSonAMensaje(mensaje));
                    }
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
     *Usado para encontrar aquellos usuarios a los que hay que notificar de nuevos mensajes
     * @return Lista de los usuarios a notificar
     * @throws ImposibleLeerObjetoJsonException
     */
    public ArrayList<String> usuariosANotificar() throws ImposibleLeerObjetoJsonException {
            ArrayList<String> usuariosANotificar = new ArrayList<String>();
            Conversacion conversacion = this.obtener();
            for (int i = 0; i < conversacion.getMensajes().size(); i++){
                if (!usuariosANotificar.contains(conversacion.getMensajes().get(i).getUsuario())){
                    usuariosANotificar.add(conversacion.getMensajes().get(i).getUsuario());
                }
            }
        return usuariosANotificar;
    }
}
