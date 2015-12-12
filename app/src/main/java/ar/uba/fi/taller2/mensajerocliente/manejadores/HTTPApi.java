package ar.uba.fi.taller2.mensajerocliente.manejadores;

import android.os.AsyncTask;

import java.io.IOException;

/**
 * Maneja los request HTTP que ejecuta la aplicacion
 */
public class HTTPApi extends AsyncTask<String, Void, String> {

    private ReceptorDeResultados receptor;
    private ManejadorHTTP manejadorHTTP;
    private boolean error = false;
    private RespuestaHTTP res;

    /**
     * Crea el manejador de request con un receptor
     * @param receptor Receptor de resultados
     */
    public HTTPApi(ReceptorDeResultados receptor) {
        this.receptor = receptor;
    }

    /**
     * Ejecuta el servicio HTTP de inicio de sesion
     * @param usuario Usuario que quiere iniciar la sesion
     * @param password Su contraseña
     */
    public void iniciarSesion(String usuario, String password) {
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.SESION_INICIAR);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_USUARIO, usuario));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_PASSWORD, password));

        this.execute();
    }

    /**
     * Ejecuta el servicio de cierre de sesion
     * @param token Token del usuario que quiere cerrar la sesion
     */
    public void cerrarSesion(String token) {
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.SESION_CERRAR);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, token));

        this.execute();
    }

    /**
     * Ejecuta el servicio de envio de mensajes
     * @param token Token del usuario que quiere enviar el mensaje
     * @param mensaje Mensaje a enviar
     * @param usuarioDestino Usuario destinatario
     */
    public void enviarMensaje(String token, String mensaje, String usuarioDestino){
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.MENSAJE_ENVIAR);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN,token));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_USUARIO_DESTINO, usuarioDestino));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CLAVE_MENSAJE,mensaje));

        this.execute();
    }

    /**
     * Ejecuta el servicio de resumenes de conversacion
     * @param token Token del usuario que quiere el resumen
     */
    public void resumenesConversacion(String token){
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.CONVERSACIONES_VER);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, token));

        this.execute();
    }

    public void listaUsuarios(){
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.LISTA_USUARIOS);

        this.execute();
    }

    /**
     * Ejecuta el servicio de resumenes de las conversaciones archivadas
     * @param token Token del usuario que quiere el resumen
     */
    public void resumenesConversacionArchivadas(String token){
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.CONVERSACIONES_VER_ARCHIVADAS);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, token));

        this.execute();
    }

    /**
     * Ejecuta el servicio de obtener mensajes desde una fecha especifica
     * @param token Token del usuario que quiere los mensajes
     * @param usuarioDestino Usuario destinatario con el cual tiene la conversacion
     * @param fecha Fecha desde la cual quiere los mensajes
     */
    public void obtenerUltimosMensajes(String token, String usuarioDestino, long fecha){
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.CONVERSACION_OBTENER_NO_LEIDOS);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, token));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_USUARIO_DESTINO, usuarioDestino));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_FECHA, String.valueOf(fecha)));

        this.execute();
    }

    /**
     * Ejecuta el servicio de Broadcast
     * @param token Token del usuario que quiere realizar el Broadcast
     * @param mensaje Mensaje a enviar
     */
    public void enviarBroadcast(String token, String mensaje){
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.MENSAJE_BROADCAST);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN,token));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_MENSAJE, mensaje));

        this.execute();
    }

    /**
     * Ejecuta el servicio de notificacion
     * @param token Token del usuario que quiere las notificaciones
     */
    public void notificacionNoLeidos(String token){
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.NOTIFICACION_NO_LEIDOS);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, token));

        this.execute();
    }

    /**
     *Ejecuta el servicio de actualizar ubicacion
     * @param token Token del usuario que actualiza la ubicacion
     * @param latitud Latitud
     * @param longitud Longitud
     */
    public void actualizarUbicacion(String token, String latitud, String longitud){
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.UBICACION_ACTUALIZAR);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, token));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_LATITUD, latitud));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_LONGITUD, longitud));

        this.execute();
    }

    /**
     *Ejecuta el servicio de actualizar filtros
     * @param token Token del usuario que actualiza la ubicacion
     * @param filtros Filtros a actualizar
     */
    public void actualizarFiltros(String token, String filtros){
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.FILTROS_ACTUALIZAR);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, token));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_FILTROS, filtros));

        this.execute();
    }

    public void buscarCoincidencias(String token){
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.BUSCAR_COINCIDENCIAS);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, token));

        this.execute();
    }
    /**
     * Ejecuta el servicio de registracion
     * @param usuario Usuario que quiere registrarse
     * @param password Su contraseña
     */
    public void registracion(String usuario, String password, int edad) {
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.REGISTRACION);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_EDAD, Integer.toString(edad)));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_USUARIO, usuario));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_PASSWORD, password));

        this.execute();
    }

    /**
     * Ejecuta el servicio de Ver Perfil
     * @param token Token del usuario que quiere ver el perfil
     * @param usuario Usuario del que se quiere ver el perfil
     */
    public void verPerfil(String token, String usuario) {
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.PERFIL_VER);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_USUARIO, usuario));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, token));

        this.execute();
    }

    /**
     * Ejecuta el servicio de Archivar Conversacion
     * @param token Token del usuario que quiere archivar la conversacion
     * @param usuario Usuario del cual se quiere archivar la conversacion
     */
    public void archivarConversacion(String token, String usuario) {
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.CONVERSACION_ARCHIVAR);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_USUARIO_DESTINO, usuario));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, token));

        this.execute();
    }

    /**
     * Ejecuta el servicio de Desarchivar Conversacion
     * @param token Token del usuario que quiere desarchivar la conversacion
     * @param usuario Usuario del cual se quiere desarchivar la conversacion
     */
    public void desarchivarConversacion(String token, String usuario) {
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.CONVERSACION_DESARCHIVAR);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_USUARIO_DESTINO, usuario));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, token));

        this.execute();
    }

    /**
     * Ejecuta el servicio de editar perfil
     * @param token Token del usuario que quiere editar el perfil
     * @param nick Nick que quiere actualizar
     * @param conexion Conexion que quiere actualizar
     * @param passVieja Contraseña vieja que quiere actualizar
     * @param passNueva Contraseña nueva que quiere actualizar
     */
    public void editarPerfil(String token, String nick, String conexion, String passVieja, String passNueva) {
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.PERFIL_EDITAR);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, token));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_NICK, nick));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_CONEXION, conexion));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_PASSWORD_VIEJA, passVieja));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_PASSWORD_NUEVA, passNueva));


        this.execute();
    }



    /**
     * Ejecuta el servicio de Ver Imagen
     * @param usuario Usuario del cual se quiere ver la imagen
     * @param token Token del usuario que quiere ver la imagen
     */
    public void verImagen(String usuario, String token) {
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.IMAGEN_VER);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_USUARIO, usuario));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, token));
        this.manejadorHTTP.setTipo(RespuestaHTTP.tipoRespuesta.IMAGEN);

        this.execute();
    }

    /**
     * Ejecuta el servicio de ver conversacion
     * @param token Token del usuario que quiere ver una conversacion
     * @param usuarioDestino Usuario destinatario con el cual tiene la conversacion
     */
    public void conversacion(String token, String usuarioDestino){
        this.manejadorHTTP = new ManejadorHTTP();
        this.manejadorHTTP.setAPI(APIConstantes.CONVERSACION_VER);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_USUARIO_DESTINO, usuarioDestino));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, token));

        this.execute();
    }

    /**
     *Ejecuta el servicio de subir imagen
     * @param usuario Usuario que quiere subir la imagen
     * @param archivo Archivo conteniendo la imagen
     */
    public void subirImagen(String usuario, String archivo) {
        this.manejadorHTTP = new ManejadorHTTPSubidor();
        this.manejadorHTTP.setAPI(APIConstantes.IMAGEN_SUBIR);
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_TOKEN, usuario));
        this.manejadorHTTP.agregarCampo(new ClaveValor(APIConstantes.CAMPO_PASSWORD_ARCHIVO, archivo));

        this.execute();
    }

    /**
     * Ejecuta el servicio solicitado en Background
     * @param APIs Api's a ejecutar
     * @return Resultado de la operacion
     */
    @Override
    protected String doInBackground(String... APIs){
        try {
            this.res = this.manejadorHTTP.enviarRequest();
            return "";
        } catch (IOException e) {
            this.error = true;
            return e.getMessage();
        }
    }

    /**
     * Guarda el resultado de la ejecucion anterior en el receptor de resultados
     * @param result Resultado de la ejecucion anterior
     */
    @Override
    protected void onPostExecute(String result){
        if (this.error)
            this.receptor.errorResultado(result);
        else
            this.receptor.recibirResultado(this.res);
    }


}
