package ar.uba.fi.taller2.mensajerocliente.utilidades;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.LinkedList;

import ar.uba.fi.taller2.mensajerocliente.Excepciones.ImposibleLeerObjetoJsonException;
import ar.uba.fi.taller2.mensajerocliente.Activities.ListaDeConversacionesActivity;
import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.HTTPApi;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorNoLeidos;
import ar.uba.fi.taller2.mensajerocliente.manejadores.Mensaje;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ReceptorDeResultados;
import ar.uba.fi.taller2.mensajerocliente.manejadores.RespuestaHTTP;

/**
 * Maneja el servicio de notificaciones
 */

public class ServicioDeNotificaciones extends Service {

    private boolean corriendo;
    private int mId = 1;
    private String textoNotificacion = "";
    @Override
    public void onCreate() {
        corriendo = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("service", "Service onStartCommand");

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(corriendo) {
                    try {
                        Thread.sleep(20000);
                    } catch (Exception e) {
                    }

                    HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados(){

                        @Override
                        public void recibirResultado(RespuestaHTTP resultado) {

                            ManejadorNoLeidos manejador = new ManejadorNoLeidos(resultado.getString());
                            try {
                                LinkedList<Mensaje> mensajes = manejador.obtener().getMensajes();
                                mostrarNotificacion(mensajes);
                            }catch (ImposibleLeerObjetoJsonException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void errorResultado(String error) {

                        }
                    });

                    String token = ManejadorDeToken.obtenerToken(getApplicationContext());

                    if (token != null)
                        httpApi.notificacionNoLeidos(ManejadorDeToken.obtenerToken(getApplicationContext()));

                }

                //stopSelf();
            }
        }).start();

        return Service.START_STICKY;
    }


    /**
     * Hace visibles las notificaciones
     * @param mensajes Mensajes a notificar
     */
    public void mostrarNotificacion(LinkedList<Mensaje> mensajes) {

        String textoNotificacionAuxiliar = "";
        for (int i = 0; i < mensajes.size(); i++) {
           textoNotificacionAuxiliar += mensajes.get(i).getUsuario() + ": " + mensajes.get(i).getMensaje() + "\n";
        }

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (mensajes.size() == 0) {
            mNotificationManager.cancel(mId);
            return;
        }

        if ( (textoNotificacion.compareTo(textoNotificacionAuxiliar)==0)){
            return;
        }



        textoNotificacion = textoNotificacionAuxiliar;

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.not)
                        .setContentTitle(getString(R.string.nuevos_mensajes))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(textoNotificacion));
                        //.setSound(alarmSound);
         mBuilder.setSound(alarmSound);

        Intent resultIntent = new Intent(this, ListaDeConversacionesActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ListaDeConversacionesActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        corriendo = false;
        super.onDestroy();
    }
}
