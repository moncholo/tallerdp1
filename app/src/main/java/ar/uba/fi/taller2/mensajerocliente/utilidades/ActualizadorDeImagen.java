package ar.uba.fi.taller2.mensajerocliente.utilidades;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.HTTPApi;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ReceptorDeResultados;
import ar.uba.fi.taller2.mensajerocliente.manejadores.RespuestaHTTP;

/**
 * Maneja la actualizacion de imagenes
 */
public class ActualizadorDeImagen {

    private static HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();

    /**
     * Actualiza la imagen de un usuario en un Activity dado
     * @param context Activity en que se solicita actualizar la iamgen
     * @param usuario Usuario del que se actualiza la imagen
     * @param imagen Imagen a actualizar
     */
    public static void actualizar(final Context context, final String usuario, final ImageView imagen) {
        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados(){
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {
                cache.put(usuario, resultado.getBitmap());
                actualizarImagen(context, cache.get(usuario), imagen);
            }
            @Override
            public void errorResultado(String error) {

            }
        });

        if (cache.get(usuario) == null) {
            httpApi.verImagen(usuario, ManejadorDeToken.obtenerToken(context));
        } else {
            actualizarImagen(context, cache.get(usuario), imagen);
        }
    }

    /**
     * Elimina la imagen del cache
     * @param usuario Usuario que tiene dicha imagen
     */
    public static void borrarCache(String usuario) {
        cache.remove(usuario);
    }

    /**
     * Elimina todas las imagenes del cache
     */
    public static void borrarCache() {
        cache.clear();
    }

    public static void actualizarImagen(Context context, Bitmap bitmap, ImageView image) {

        Bitmap original = bitmap;
        Bitmap mask = BitmapFactory.decodeResource(context.getResources(), R.drawable.mascara);
        original = Bitmap.createScaledBitmap(original, mask.getWidth(), mask.getHeight(), true);

        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        image.setImageBitmap(result);

        //mImageView.setScaleType(ScaleType.FIT_XY);
    }

}
