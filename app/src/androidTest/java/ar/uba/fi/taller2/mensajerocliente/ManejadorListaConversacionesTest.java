package ar.uba.fi.taller2.mensajerocliente;

import junit.framework.TestCase;

import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorListaConversaciones;

/**
 * Created by Juan Andres Laura on 27/04/15.
 */
public class ManejadorListaConversacionesTest extends TestCase{

    private String unaConversacion = "[\n" +
            "   {\n" +
            "      \"conversacion\" : [ \"1430078981\", \"lucas\", \"Como va\" ],\n" +
            "      \"usuarioDestino\" : \"juan\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"conversacion\" : [ \"1430106682\", \"mica\", \"Como va\" ],\n" +
            "      \"usuarioDestino\" : \"charly\"\n" +
            "   }\n" +
            "]\n";

    public void testGeneraBienLaListaDeConversaciones(){
        ManejadorListaConversaciones unManejador = new ManejadorListaConversaciones(unaConversacion);

        assertEquals(unManejador.obtenerLista().size(), 2);
    }

    public void testGeneraBienEmisoresMensajes(){
        ManejadorListaConversaciones unManejador = new ManejadorListaConversaciones(unaConversacion);

        assertEquals(unManejador.obtenerLista().get(0).getMensajeDelEmisor().getUsuario(),"lucas");
        assertEquals(unManejador.obtenerLista().get(1).getMensajeDelEmisor().getUsuario(),"mica");

    }

    public void testGeneraBienUsuarioDestino(){
        ManejadorListaConversaciones unManejador = new ManejadorListaConversaciones(unaConversacion);

        assertEquals(unManejador.obtenerLista().get(0).getUsuarioDestino(),"juan");
        assertEquals(unManejador.obtenerLista().get(1).getUsuarioDestino(),"charly");

    }



}
