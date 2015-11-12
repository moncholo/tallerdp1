package ar.uba.fi.taller2.mensajerocliente.manejadores;

import java.util.LinkedList;

/**
 * Modelo de una conversacion entre dos usuarios
 */
public class Conversacion {

    private LinkedList<Mensaje> mensajes;

    //en una conversacion hay dos usuarios
    private String unUsuario;
    private String otroUsuario;

    /**
     * Crea una conversacion vacia y sin usuarios
     */
    public Conversacion(){
        this.mensajes = new LinkedList<Mensaje>();
    }

    /**
     * Agrega nuevo mensaje a la conversacion
     * @param unMensaje Mensaje a agregar
     */
    public void agregarMensaje(Mensaje unMensaje){
        this.mensajes.addLast(unMensaje);
    }

    /**
     *
     * @return Lista de mensajes de la conversacion
     */
    public LinkedList<Mensaje> getMensajes() {
        return mensajes;
    }

    /**
     * Agrega un conjunto de mensajes a la conversacion
     * @param mensajes Mensajes a agregar
     */
    public void setMensajes(LinkedList<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    /**
     * Devuelve uno de los usuarios de la conversacion
     * @return Un usuario
     */
    public String getUnUsuario() {
        return unUsuario;
    }

    /**
     * Settea uno de los usuarios de la conversaciones
     * @param unUsuario Un usuario
     */
    public void setUnUsuario(String unUsuario) {
        this.unUsuario = unUsuario;
    }

    /**
     * Devuelve el otro usuario de la conversacion
     * @return Otro usuario
     */
    public String getOtroUsuario() {
        return otroUsuario;
    }


    /**
     * Settea el otro usuario de la conversaciones
     * @param otroUsuario Otro usuario
     */
    public void setOtroUsuario(String otroUsuario) {
        this.otroUsuario = otroUsuario;
    }
}
