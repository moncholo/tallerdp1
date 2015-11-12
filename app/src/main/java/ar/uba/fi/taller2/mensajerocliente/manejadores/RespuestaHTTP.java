package ar.uba.fi.taller2.mensajerocliente.manejadores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Maneja la respuesta del servidor
 */
public class RespuestaHTTP {

    private InputStream in;

    private Bitmap bitmap;
    private String convertido;
    public enum tipoRespuesta {TEXTO, IMAGEN};

    /**
     * Crea una respuesta con el input y el tipo de respuesta
     * @param input Input
     * @param t Tipo de respuesta
     * @throws IOException
     */
    public RespuestaHTTP(InputStream input, tipoRespuesta t) throws IOException {
        this.in = input;

        if (t == tipoRespuesta.TEXTO)
            this.convertido = this.convertirAString(this.in);
        else if (t == tipoRespuesta.IMAGEN)
            this.bitmap = BitmapFactory.decodeStream(this.in);
    }

    /**
     *
     * @return El bitmap
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /*
    * Devuelve la respuesta
     */
    public String getString() {
        return convertido;
    }


    private String convertirAString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
