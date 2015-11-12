package ar.uba.fi.taller2.mensajerocliente.manejadores;

/**
 * Modelo de duplas clave-valor
 */
public class ClaveValor {

    private String clave;
    private String valor;

    /**Crea la dupla con clave y valor
     *
     * @param clave Clave de la dupla
     * @param valor Valor de la dupla
     */
    public ClaveValor(String clave,  String valor) {
        this.clave = clave;
        this.valor = valor;
    }

    /**
     *
     * @return La clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * Settea la clave de la dupla
     *
     * @param clave La clave
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     *
     * @return El valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * Settea el valor de la dupla
     *
     * @param valor El valor
     */
    public void setValor(String valor) {
        this.valor = valor;
    }
}
