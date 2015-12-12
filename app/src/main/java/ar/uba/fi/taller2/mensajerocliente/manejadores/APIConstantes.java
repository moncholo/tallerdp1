package ar.uba.fi.taller2.mensajerocliente.manejadores;

/**
 * Define las constantes que manejan el sistema
 */
public class APIConstantes {

    // Parámetros del servidor
    static final String IP = "104.131.7.26";
    static final String PUERTO= "8080";

    //Persistencia de datos
    public static final String PERSISTENCIA_DATOS = "Datos";
    public static final String PERSISTENCIA_TOKEN = "token";
    public static final String PERSISTENCIA_USUARIO = "usuario";
    public static final String PERSISTENCIA_VIAJES = "viajes";
    public static final String PERSISTENCIA_CONVERSACIONES = "conversaciones";
    public static final String PERSISTENCIA_EDAD = "edad";
    public static final String PERSISTENCIA_GRATUITA="version" ;

    //Estado de servicios
    public static final String ESTADO_VALIDO = "Servicio Finalizado Correctamente";
    public static final String ESTADO = "Estado";
    public static final String CONTENIDO = "contenido";
    public static final String ESTADO_USUARIO_EXISTENTE = "Usuario existente";
    public static final String ESTADO_PASSWORD_INVALIDA = "Password invalida";
    public static final String ESTADO_PASSWORD_ACTUAL_INVALIDA = "Password actual invalida";
    public static final String ESTADO_PASSWORD_NUEVA_INVALIDA = "Password nueva invalida";
    public static final String ESTADO_USUARIO_INVALIDO = "Usuario invalido";
    public static final String ESTADO_TOKEN_INVALIDO = "Token invalido";
    public static final String ESTADO_USUARIO_INEXISTENTE = "Usuario inexistente";
    public static final String ESTADO_MENSAJE_VACIO = "Mensaje vacio";
    public static final String ESTADO_CONVERSACION_INEXISTENTE = "Conversacion Inexistente";
    public static final String CONEXION_INICIAL = "Offline";
    public static final String NICK_DEFAULT = "NewChatter";
    public static final String CLAVE_USUARIO_ORIGEN = "usuarioOrigen";
    public static final String CLAVE_USUARIO_DESTINO = "usuarioDestino";
    public static final String CLAVE_CONVERSACION = "conversacion";
    public static final String CONVERSACION_ACTIVA = "1";
    public static final String CONVERSACION_ARCHIVADA = "0";
    public static final String CLAVE_FECHA = "fecha";
    public static final String CLAVE_FILTROS = "filtros";
    public static final String CLAVE_MENSAJE = "mensaje";
    public static final String TOKEN_INVALIDO = "Token invalido";
    public static final String CLAVE_CONEXION = "conexion";
    public static final String CLAVE_NICK = "nick";
    public static final String CLAVE_ULTIMA_CONEXION = "ultimaConexion";
    public static final String CLAVE_LATITUD = "latitud";
    public static final String CLAVE_LONGITUD = "longitud";
    public static final String CLAVE_UBICACION_CERCANA = "ubicacionCercana";
    public static final String CLAVE_LISTA_USUARIOS = "listaUsuarios";
    // URL Servicios
    public static final String LISTA_USUARIOS = "/api/listaUsuarios";
    public static final String REGISTRACION = "/api/registracion";
    public static final String SESION_INICIAR = "/api/sesion/iniciar";
    public static final String SESION_CERRAR = "/api/sesion/cerrar";
    public static final String MENSAJE_ENVIAR = "/api/mensaje/enviar";
    public static final String PERFIL_VER = "/api/perfil/ver";
    public static final String PERFIL_EDITAR = "/api/perfil/editar";
    public static final String CONVERSACION_VER = "/api/conversacion/ver";
    public static final String CONVERSACIONES_VER = "/api/conversaciones/ver";
    public static final String CONVERSACIONES_VER_ARCHIVADAS = "/api/conversaciones/verArchivadas";
    public static final String CONVERSACION_ARCHIVAR = "/api/conversacion/archivar";
    public static final String CONVERSACION_DESARCHIVAR = "/api/conversacion/desarchivar";
    public static final String IMAGEN_VER = "/api/imagen/ver";
    public static final String IMAGEN_SUBIR = "/api/imagen/subir";
    public static final String CONVERSACION_OBTENER_NO_LEIDOS = "/api/conversacion/obtenerNoLeidos";
    public static final String UBICACION_ACTUALIZAR = "/api/ubicacion/actualizar";
    public static final String FILTROS_ACTUALIZAR= "/api/filtros/actualizar";
    public static final String NOTIFICACION_NO_LEIDOS = "/api/notificacion/obtenerNoLeidos";
    public static final String MENSAJE_BROADCAST = "/api/mensaje/broadcast";
    public static final String BUSCAR_COINCIDENCIAS = "/api/coincidencias/buscar";

    // Campos utilizados
    public static final String CAMPO_USUARIO = "usuario";
    public static final String CAMPO_EDAD = "edad";
    public static final String CAMPO_PASSWORD = "pass";
    public static final String FECHA_IDA = "fechaIda";
    public static final String FECHA_VUELTA = "fechaVuelta";
    public static final String CAMPO_TOKEN = "token";
    public static final String CAMPO_USUARIO_DESTINO = "usuarioDestino";
    public static final String CAMPO_MENSAJE = "mensaje";
    public static final String CAMPO_NICK = "nick";
    public static final String CAMPO_CONEXION = "conexion";
    public static final String CAMPO_PASSWORD_VIEJA = "pass";
    public static final String CAMPO_PASSWORD_NUEVA = "newPass";
    public static final String CAMPO_PASSWORD_ARCHIVO = "file";
    public static final String CAMPO_LATITUD = "latitud";
    public static final String CAMPO_LONGITUD = "longitud";
    public static final String CAMPO_FECHA = "fecha";
    public static final String CAMPO_FILTROS = "filtros";

    //Colores
    public static final int COLOR_NUEVO_MENSAJE = 0xFFA1A1A1;
    public static final int COLOR_MENSAJE_LEIDO = 0xFFFFFFFF;
}
