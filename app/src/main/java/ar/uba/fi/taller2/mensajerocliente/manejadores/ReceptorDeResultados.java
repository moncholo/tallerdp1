package ar.uba.fi.taller2.mensajerocliente.manejadores;

/**
 * Interfaz para recibir resultados del servidor
 */
public interface ReceptorDeResultados {
    public void recibirResultado(RespuestaHTTP resultado);
    public void errorResultado(String error);


}
