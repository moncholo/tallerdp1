package ar.uba.fi.taller2.mensajerocliente;

import junit.framework.TestCase;

import ar.uba.fi.taller2.mensajerocliente.Excepciones.ImposibleCrearConversacionException;
import ar.uba.fi.taller2.mensajerocliente.Excepciones.ImposibleLeerObjetoJsonException;
import ar.uba.fi.taller2.mensajerocliente.manejadores.Conversacion;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorConversacion;
import ar.uba.fi.taller2.mensajerocliente.manejadores.Mensaje;

/**
 * Created by juan on 26/04/15.
 */
public class JsonTest extends TestCase {

    public void test1(){

        assertEquals(1,1);
    }


    public void testNombreUsuario(){

        String conversacion = "{\n" +
                "   \"conversacion\" : [\n" +
                "      [ \"1430078981\", \"juan\", \"Como va\" ],\n" +
                "      [ \"1430079528\", \"pepe\", \"Como va\" ],\n" +
                "      [ \"1430079532\", \"juan\", \"Como va\" ]\n" +
                "   ]\n" +
                "}";

        ManejadorConversacion manejador = new ManejadorConversacion(conversacion);

        Conversacion unaConversacion = null;

        try {
            unaConversacion = manejador.crearConversacion();
        } catch (ImposibleCrearConversacionException e) {
            assertEquals(0,1);
        } catch (ImposibleLeerObjetoJsonException e) {
            e.printStackTrace();
        }

        Mensaje unMensaje = unaConversacion.getMensajes().get(0);

        assertEquals(unaConversacion.getMensajes().get(0).getUsuario(), "juan");
        assertEquals(unaConversacion.getMensajes().get(1).getUsuario(), "pepe");
        assertEquals(unaConversacion.getMensajes().get(2).getUsuario(), "juan");
    }
    public void testFecha(){

        String conversacion = "{\n" +
                "   \"conversacion\" : [\n" +
                "      [ \"1430078981\", \"juan\", \"Como va\" ],\n" +
                "      [ \"1430079528\", \"juan\", \"Como va\" ],\n" +
                "      [ \"1430079532\", \"juan\", \"Como va\" ]\n" +
                "   ]\n" +
                "}";

        ManejadorConversacion manejador = new ManejadorConversacion(conversacion);
        Conversacion unaConversacion = null;
        try {
            unaConversacion = manejador.crearConversacion();
        } catch (ImposibleCrearConversacionException e) {
            assertEquals(0,1);
        } catch (ImposibleLeerObjetoJsonException e) {
            e.printStackTrace();
        }


        assertEquals(unaConversacion.getMensajes().get(0).getFecha(), 1430078981);
        assertEquals(unaConversacion.getMensajes().get(1).getFecha(), 1430079528);
        assertEquals(unaConversacion.getMensajes().get(2).getFecha(), 1430079532);
    }

    public void testMensajeCorrecto(){
        String conversacion = "{\n" +
                "   \"conversacion\" : [\n" +
                "      [ \"1430078981\", \"juan\", \"Como va\" ],\n" +
                "      [ \"1430079528\", \"juan\", \"Bien vos?\" ],\n" +
                "      [ \"1430079532\", \"juan\", \"Todo Ok\" ]\n" +
                "   ]\n" +
                "}";

        ManejadorConversacion manejador = new ManejadorConversacion(conversacion);
        Conversacion unaConversacion = null;
        try {
            unaConversacion = manejador.crearConversacion();
        } catch (ImposibleCrearConversacionException e) {
            assertEquals(0,1);
        } catch (ImposibleLeerObjetoJsonException e) {
            e.printStackTrace();
        }


        assertEquals(unaConversacion.getMensajes().get(0).getMensaje(), "Como va");
        assertEquals(unaConversacion.getMensajes().get(1).getMensaje(), "Bien vos?");
        assertEquals(unaConversacion.getMensajes().get(2).getMensaje(), "Todo Ok");
    }
}
