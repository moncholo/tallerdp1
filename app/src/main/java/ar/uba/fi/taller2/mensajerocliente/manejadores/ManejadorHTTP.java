package ar.uba.fi.taller2.mensajerocliente.manejadores;

import android.util.Log;

import java.io.BufferedInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Maneja forma de enviar solicitudes HTTP y de obtener los resultados
 */


public class ManejadorHTTP {

    protected String api;

    protected RespuestaHTTP.tipoRespuesta tipo;

    protected ArrayList<ClaveValor> campos;

    /**
     *  Crea un manejador sin respuesta
     */
    public ManejadorHTTP(){
        this.campos = new ArrayList<ClaveValor>();
        this.tipo = RespuestaHTTP.tipoRespuesta.TEXTO;
    }

    /**
     * Crea la URI para ejecutar el request
     * @return Devuelve la URI armada
     * @throws UnsupportedEncodingException
     */
    public String encodeUri() throws UnsupportedEncodingException {
        String paramsEncoded = "?";

        for (int i = 0; i < this.campos.size(); i++)
            paramsEncoded += "&" + this.campos.get(i).getClave() + "=" + URLEncoder.encode(this.campos.get(i).getValor(), "utf-8");


        return "http://" + APIConstantes.IP + ":" + APIConstantes.PUERTO + this.api + paramsEncoded;
    }

    /**
     * Ejecuta el request a partir de la URI creada
     * @return Respuesta del servidor
     * @throws IOException
     */
    public RespuestaHTTP enviarRequest() throws IOException {

        URI uri = null;
        try {
            uri = new URI(this.encodeUri());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URL url = uri.toURL();
        Log.i("URI: ", uri.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return new RespuestaHTTP(in, tipo);
        } finally {
            urlConnection.disconnect();
        }

    }

    /**
     * Settea una API a solicitar
     * @param api API a solicitar
     */
    public void setAPI(String api) {
        this.api = api;
    }

    /**
     * Agrega los parametros para el request
     * @param claveValor Parametro Clave Valor
     */
    public void agregarCampo(ClaveValor claveValor) {
        this.campos.add(claveValor);
    }

    /**
     * Settea el tipo de solicitud al servidor
     * @param tipo Tipo de solicitud
     */
    public void setTipo(RespuestaHTTP.tipoRespuesta tipo) {
        this.tipo = tipo;
    }
}
