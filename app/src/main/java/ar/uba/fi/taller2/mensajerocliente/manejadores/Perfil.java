package ar.uba.fi.taller2.mensajerocliente.manejadores;

/**
 * Perfil de usuario
 */

public class Perfil {

    private String filtros;

    /**
     *
     * @return Nick del usuario
     */
    public String getNick() {
        return nick;
    }

    /**
     * Settea el nick del usuario
     * @param nick Nick del usuario
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     *
     * @return Conexion del usuario
     */
    public String getConexion() {
        return conexion;
    }

    /**
     * Settea la conexion del usuario
     * @param conexion Conexion del usuario
     */
    public void setConexion(String conexion) {
        this.conexion = conexion;
    }

    /**
     *
     * @return Ultima conexion del usuario
     */
    public long getUltimaConexion() {
        return ultimaConexion;
    }

    /**
     * Settea la ultima conexion del usuario
     * @param ultimaConexion Ultima conexion del usuario
     */
    public void setUltimaConexion(long ultimaConexion) {
        this.ultimaConexion = ultimaConexion;
    }

    /**
     *
     * @return Latitud en la que se encuentra
     */
    public double getLatitud() {
        return latitud;
    }

    /**
     * Settea latitud
     * @param latitud Latitud en la que se encuentra
     */
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    /**
     *
     * @return Longitud en la que se encuentra
     */
    public double getLongitud() {
        return longitud;
    }

    /**
     * Settea longitud
     * @param longitud Longitud en la que se encuentra
     */
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    /**
     *
     * @return Ubicacion cercana a la posicion del usuario
     */
    public String getUbicacionCercana() {
        return ubicacionCercana;
    }

    /**
     * Settea la ubicacion cercana a la posicion del usuario
     * @param ubicacionCercana Ubicacion cercana
     */
    public void setUbicacionCercana(String ubicacionCercana) {
        this.ubicacionCercana = ubicacionCercana;
    }

    private String nick;
    private String conexion;
    private long ultimaConexion;
    private double latitud;
    private double longitud;
    private String ubicacionCercana;

    public void setFiltros(String filtros) {
        this.filtros = filtros;
    }
}
