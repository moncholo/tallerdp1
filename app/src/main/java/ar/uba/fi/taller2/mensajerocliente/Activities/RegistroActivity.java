package ar.uba.fi.taller2.mensajerocliente.Activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.HTTPApi;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorRegistracion;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ReceptorDeResultados;
import ar.uba.fi.taller2.mensajerocliente.manejadores.RespuestaHTTP;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ConstructorDeDialogo;

/**
 * Activity de lista de conversaciones archivadas
 */
public class RegistroActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro, menu);
        return true;
    }

    public void enunciarEstado(String estado, int color){
        TextView error = (TextView) this.findViewById(R.id.textError);
        error.setText(estado);
        error.setVisibility(View.VISIBLE);
        error.setTextColor(color);
    }

    public void setActivados(boolean activados) {
        EditText usuario = (EditText) this.findViewById(R.id.editUsuario);
        EditText password = (EditText) this.findViewById(R.id.editPassword);
        EditText password2 = (EditText) this.findViewById(R.id.editPasswordNueva);
        Button buttonRegistrarse = (Button) this.findViewById(R.id.buttonBuscar);

        usuario.setEnabled(activados);
        password.setEnabled(activados);
        password2.setEnabled(activados);
        buttonRegistrarse.setEnabled(activados);
    }

    public void registracion(View v){

        final ProgressDialog dialogo = ConstructorDeDialogo.obtener(this);

        EditText usuario = (EditText) this.findViewById(R.id.editUsuario);
        EditText password = (EditText) this.findViewById(R.id.editPassword);
        EditText passwordConfirmacion = (EditText) this.findViewById(R.id.editPasswordNueva);

        String pass1 = password.getText().toString();
        String pass2 = passwordConfirmacion.getText().toString();

        if (password.getText().toString().equals(passwordConfirmacion.getText().toString())) {

            HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados(){

                @Override
                public void recibirResultado(RespuestaHTTP resultado) {

                    dialogo.dismiss();
                    ManejadorRegistracion manejador = new ManejadorRegistracion(resultado.getString());

                    if (manejador.estadoValido()){
                        setActivados(false);
                        enunciarEstado(getResources().getString(R.string.registracion_valida), Color.DKGRAY);
                    }else{
                        enunciarEstado(manejador.obtenerEstado(), Color.RED);
                    }

                }

                @Override
                public void errorResultado(String error) {
                    enunciarEstado(getResources().getString(R.string.error_conexion), Color.RED);
                    setActivados(true);
                }
            });

            dialogo.show();
            httpApi.registracion(usuario.getText().toString(), password.getText().toString());

        }else{
            enunciarEstado(getResources().getString(R.string.contrasenas_no_concuerdan), Color.RED);
        }

    }

}
