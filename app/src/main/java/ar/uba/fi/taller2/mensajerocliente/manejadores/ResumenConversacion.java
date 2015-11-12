package ar.uba.fi.taller2.mensajerocliente.manejadores;

/**
 * Modelo del resumen de conversacion
 */
public class ResumenConversacion  {

    private String usuarioDestino;
    private Mensaje mensajeDelEmisor;
    private boolean conexion;

    /**
     *
     * @return Usuario con el que se tiene la conversacion
     */
    public String getUsuarioDestino() {
        return usuarioDestino;
    }

    /**
     * Settea el usuario destino
     * @param usuarioDestino Usuario destino
     */
    public void setUsuarioDestino(String usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }

    /**
     *
     * @return Devuelve la conexion
     */
    public boolean getConexion() {
        return conexion;
    }

    /**
     * Settea la conexion
     * @param conexion Conexion
     */
    public void setConexion(boolean conexion) {
        this.conexion = conexion;
    }

    /**
     *
     * @return Devuelve el mensaje enviado
     */
    public Mensaje getMensajeDelEmisor() {
        return mensajeDelEmisor;
    }

    /**
     * Settea el mensaje enviado
     * @param mensajeDelEmisor Mensaje enviado
     */
    public void setMensajeDelEmisor(Mensaje mensajeDelEmisor) {
        this.mensajeDelEmisor = mensajeDelEmisor;
    }

    /**
     * Crea un resumen de conversacion con un usuario destino, un mensaje y una conexion
     * @param usrDestino Usuario destino
     * @param unMensaje Mensaje
     * @param conexion Conexion
     */
    public ResumenConversacion(String usrDestino, Mensaje unMensaje, boolean conexion){
        this.usuarioDestino = usrDestino;
        this.mensajeDelEmisor = unMensaje;
        this.conexion = conexion;
    }
}
