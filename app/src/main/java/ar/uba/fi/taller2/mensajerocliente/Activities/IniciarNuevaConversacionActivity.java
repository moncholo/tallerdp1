package ar.uba.fi.taller2.mensajerocliente.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.APIConstantes;
import ar.uba.fi.taller2.mensajerocliente.manejadores.HTTPApi;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorVerPerfil;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ReceptorDeResultados;
import ar.uba.fi.taller2.mensajerocliente.manejadores.RespuestaHTTP;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ConstructorDeDialogo;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ManejadorDeToken;

/**
 * Activity de iniciar nueva conversacion
 */
public class IniciarNuevaConversacionActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_nueva_conversacion);
    }

    public void buscar(View v){

        final ProgressDialog dialogo = ConstructorDeDialogo.obtener(this);

        final EditText usuario = (EditText) this.findViewById(R.id.editUsuario);

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados(){

                @Override
                public void recibirResultado(RespuestaHTTP resultado) {

                    dialogo.dismiss();
                    ManejadorVerPerfil manejador = new ManejadorVerPerfil(resultado.getString());

                    if (manejador.estadoValido()){
                        Intent intent = new Intent(getApplicationContext(), ConversacionActivity.class);
                        intent.putExtra(APIConstantes.CAMPO_USUARIO_DESTINO, usuario.getText().toString());
                        startActivity(intent);
                    }else{
                        // Modificar la clase perfil adecuadamente
                        Toast.makeText(getApplicationContext(), "Usuario inexistente", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void errorResultado(String error) {
                    Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
                }
        });

        dialogo.show();
        httpApi.verPerfil(ManejadorDeToken.obtenerToken(getApplicationContext()), usuario.getText().toString());

    }

}
