package ar.uba.fi.taller2.mensajerocliente.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.HTTPApi;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ReceptorDeResultados;
import ar.uba.fi.taller2.mensajerocliente.manejadores.RespuestaHTTP;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ManejadorDeToken;

/**
* Activity de Broadcast
 */
public class BroadcastActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_broadcast);
    }

    public void enviarMensajeBroadcast(View v){
        TextView mensaje = (TextView)findViewById(R.id.editMensaje);
        Button enviar = (Button) findViewById(R.id.buttonEnviar);
        mensaje.setEnabled(false);
        enviar.setEnabled(false);
        this.enviarMensaje(mensaje.getText().toString());
    }

    void enviarMensaje(String mensaje){

        final TextView textMensaje = (TextView)findViewById(R.id.editMensaje);
        final Button buttonEnviar = (Button) findViewById(R.id.buttonEnviar);
        buttonEnviar.setText(R.string.enviando);

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {

            @Override
            public void recibirResultado(RespuestaHTTP resultado) {
                TextView mensaje = (TextView)findViewById(R.id.editMensaje);
                mensaje.setText("");
                textMensaje.setEnabled(true);
                buttonEnviar.setEnabled(true);
                buttonEnviar.setText(R.string.boton_enviar);
                finish();
            }

            @Override
            public void errorResultado(String error) {
                Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
                textMensaje.setEnabled(true);
                buttonEnviar.setEnabled(true);
                buttonEnviar.setText(R.string.boton_enviar);
                finish();
            }
        });


        if (mensaje.trim().compareTo("")!=0) {
            httpApi.enviarBroadcast(ManejadorDeToken.obtenerToken(this), mensaje);
        }

    }


}
