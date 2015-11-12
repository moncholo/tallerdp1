package ar.uba.fi.taller2.mensajerocliente.manejadores;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import ar.uba.fi.taller2.mensajerocliente.Excepciones.ImposibleLeerObjetoJsonException;

/**
 * Maneja el servicio de Lista de Conversaciones
 */
public class ManejadorListaConversaciones {

    LinkedList<ResumenConversacion> listaUltimosMensajes;

    public ManejadorListaConversaciones (String jSonDeLaLista){
        JSONArray listaConversaciones;

        this.listaUltimosMensajes = new LinkedList<ResumenConversacion>();

        try {

            JSONObject objetoConversaciones = new JSONObject(jSonDeLaLista);

            listaConversaciones = objetoConversaciones.getJSONArray(APIConstantes.CONTENIDO);

            for (int i = 0; i < listaConversaciones.length(); i++){

                String usuarioDestino = listaConversaciones.getJSONObject(i).getString(APIConstantes.CAMPO_USUARIO_DESTINO);
                boolean conexion = listaConversaciones.getJSONObject(i).getString(APIConstantes.CAMPO_CONEXION).compareTo("Online") == 0;

                ManejadorConversacion manejadorConversacion = new ManejadorConversacion();
                JSONArray array = listaConversaciones.getJSONObject(i).getJSONArray(APIConstantes.CLAVE_CONVERSACION);
                Mensaje unMensaje = manejadorConversacion.convertirJSonAMensaje(array);

                this.listaUltimosMensajes.addLast(new ResumenConversacion(usuarioDestino, unMensaje, conexion));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ImposibleLeerObjetoJsonException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<ResumenConversacion> obtenerLista(){
        return this.listaUltimosMensajes;
    }

}
