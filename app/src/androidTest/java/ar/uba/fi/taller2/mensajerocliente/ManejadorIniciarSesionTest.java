package ar.uba.fi.taller2.mensajerocliente;

import junit.framework.TestCase;

import ar.uba.fi.taller2.mensajerocliente.manejadores.APIConstantes;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorIniciarSesion;

/**
 * Created by Juan Andres Laura on 26/04/15.
 */
public class ManejadorIniciarSesionTest extends TestCase{

    public void testDevuelveBienEstado(){
        String jSon = "{\n" +
                "   \"Estado\" : \"Servicio Finalizado Correctamente\",\n" +
                "   \"token\" : \"bc20241fb743d5a68b9b97e229c2bf32\"\n" +
                "}";
        ManejadorIniciarSesion manejador = new ManejadorIniciarSesion(jSon);

        assertEquals(manejador.obtenerEstado(), APIConstantes.ESTADO_VALIDO);
    }

    public void testDevuelveBienToken(){
        String jSon = "{\n" +
                "   \"Estado\" : \"Servicio Finalizado Correctamente\",\n" +
                "   \"token\" : \"bc20241fb743d5a68b9b97e229c2bf32\"\n" +
                "}";

        ManejadorIniciarSesion manejador = new ManejadorIniciarSesion(jSon);

        assertEquals(manejador.obtenerToken(),"bc20241fb743d5a68b9b97e229c2bf32");
    }

    public void testValidaBienEstado(){
        String jSon = "{\n" +
                "   \"Estado\" : \"Servicio Finalizado Correctamente\",\n" +
                "   \"token\" : \"bc20241fb743d5a68b9b97e229c2bf32\"\n" +
                "}";

        ManejadorIniciarSesion manejador = new ManejadorIniciarSesion(jSon);

        assertEquals(manejador.estadoValido(),true);
    }

    public void testValidaBienEstadoInvalido(){
        String jSon = "{\n" +
                "   \"Estado\" : \"Cualquier estado\",\n" +
                "   \"token\" : \"bc20241fb743d5a68b9b97e229c2bf32\"\n" +
                "}";

        ManejadorIniciarSesion manejador = new ManejadorIniciarSesion(jSon);

        assertEquals(manejador.estadoValido(),false);
    }
}
