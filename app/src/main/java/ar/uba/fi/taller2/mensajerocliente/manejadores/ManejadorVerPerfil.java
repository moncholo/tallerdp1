package ar.uba.fi.taller2.mensajerocliente.manejadores;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Maneja el servicio de ver perfil
 */
public class ManejadorVerPerfil {

    boolean estado;
    Perfil perfil;

    /**
     * Construye un manejador a partir de un JSon
     * @param json Contenido
     */
    public ManejadorVerPerfil(String json){
        JSONObject objeto = null;

        this.estado = true;
        try {
            objeto = new JSONObject(json);

            String estado = objeto.getString(APIConstantes.ESTADO);

            if (estado.compareTo(APIConstantes.ESTADO_VALIDO) == 0) {

                JSONObject contenido = objeto.getJSONObject(APIConstantes.CONTENIDO);
                this.perfil = new Perfil();
                this.perfil.setConexion(contenido.getString(APIConstantes.CLAVE_CONEXION));
                this.perfil.setFiltros(contenido.getString(APIConstantes.CLAVE_FILTROS));
                this.perfil.setNick(contenido.getString(APIConstantes.CLAVE_NICK));
                this.perfil.setUltimaConexion(contenido.getLong(APIConstantes.CLAVE_ULTIMA_CONEXION));
                this.perfil.setLatitud(contenido.getDouble(APIConstantes.CLAVE_LATITUD));
                this.perfil.setLongitud(contenido.getDouble(APIConstantes.CLAVE_LONGITUD));
                this.perfil.setUbicacionCercana(contenido.getString(APIConstantes.CLAVE_UBICACION_CERCANA));
            } else {
                this.estado = false;
            }

        } catch (JSONException e) {
            this.estado = false;
        }
    }

    /**
     *
     * @return Devuelve si el estado es valido
     */
    public boolean estadoValido(){
        return this.estado;
    }

    /**
     * @return Perfil del usuario
     */
    public Perfil getPerfil() {
        return perfil;
    }
}
