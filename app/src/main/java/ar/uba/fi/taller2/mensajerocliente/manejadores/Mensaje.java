package ar.uba.fi.taller2.mensajerocliente.manejadores;

import android.util.Log;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Encargada de contener informacion acerca de mensajes recibidos/enviados
 */

public class Mensaje {

    private String usuario;
    private String mensaje;
    private String fecha;
    private long fechaFormatoEntero;
    public Mensaje(){
        this.usuario = "";
        this.mensaje = "";
        this.fecha = "";
        this.fechaFormatoEntero = 0;
    }

    /**
     *Crea un mensaje con usuario y fecha
     *
     * @param unUsuario Usuario que redacta el mensaje
     * @param unMensaje El mensaje en si
     * @param unaFecha Fecha de redaccion de mensajed
     */
    public Mensaje(String unUsuario, String unMensaje, long unaFecha){
        this.usuario = unUsuario;
        this.mensaje = unMensaje;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getDefault());
        Date fecha = new Date(unaFecha * 1000);
        this.fecha = formatter.format(fecha);
        this.fechaFormatoEntero = unaFecha;
    }

    /**
     *
     * @return Usuario que escribio el mensaje
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Settea usuario que redacta el mensaje
     *
     * @param usuario Nombre del usuario
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     *
     * @return Mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /***
     * Settea el mensaje
     *
     * @param mensaje Contenido del mensaje
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     *
     * @return Fecha de redaccion del mensaje en formato String
     */
    public String getFecha() {
        return fecha;
    }

    /**
     *
     * @return Fecha en formato entero
     */
    public long getFechaFormatoEntero(){ return this.fechaFormatoEntero; };
}
