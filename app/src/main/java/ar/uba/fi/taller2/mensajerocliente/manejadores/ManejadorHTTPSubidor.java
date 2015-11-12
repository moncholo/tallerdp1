package ar.uba.fi.taller2.mensajerocliente.manejadores;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Manejador HTTP Exclusivo para subir archivos al servidor.
 */


public class ManejadorHTTPSubidor extends ManejadorHTTP {

    /**
     * Request para subir un archivo
     * @return Respuesta del servidor
     * @throws IOException
     */
    public RespuestaHTTP enviarRequest() throws IOException {

        String archivo = this.campos.get(1).getValor();

        FileInputStream entrada = new FileInputStream(new File(archivo));

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
            int bytesLeidos, bytesDisponibles, tamanoBuffer;
            byte[] buffer;
            int maxTamBuffer = 1*1024*1024;
            String finalLinea = "\r\n";
            String dobleGuion = "--";
            String limite =  "*****";

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + limite);

            DataOutputStream salida = new DataOutputStream(urlConnection.getOutputStream());
            salida.writeBytes(dobleGuion + limite + finalLinea);
            salida.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + archivo + "\"" + finalLinea);
            salida.writeBytes(finalLinea);

            bytesDisponibles = entrada.available();
            tamanoBuffer = Math.min(bytesDisponibles, maxTamBuffer);
            buffer = new byte[tamanoBuffer];

            // Read file
            bytesLeidos = entrada.read(buffer, 0, tamanoBuffer);

            while (bytesLeidos > 0)
            {
                salida.write(buffer, 0, tamanoBuffer);
                bytesDisponibles = entrada.available();
                tamanoBuffer = Math.min(bytesDisponibles, maxTamBuffer);
                bytesLeidos = entrada.read(buffer, 0, tamanoBuffer);
            }

            salida.writeBytes(finalLinea);
            salida.writeBytes(dobleGuion + limite + dobleGuion + finalLinea);

            //urlConnection.getResponseMessage();

            entrada.close();
            salida.flush();
            salida.close();


            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return new RespuestaHTTP(in, tipo);
        } finally {
            urlConnection.disconnect();
        }

    }
}
