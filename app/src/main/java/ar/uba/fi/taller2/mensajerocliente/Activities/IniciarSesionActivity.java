package ar.uba.fi.taller2.mensajerocliente.Activities;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.HTTPApi;
import ar.uba.fi.taller2.mensajerocliente.manejadores.APIConstantes;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorIniciarSesion;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ReceptorDeResultados;
import ar.uba.fi.taller2.mensajerocliente.manejadores.RespuestaHTTP;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ConstructorDeDialogo;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ManejadorDeToken;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ServicioDeNotificaciones;
/**
 * Activity de iniciar sesion
 */
public class IniciarSesionActivity extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iniciar_sesion, menu);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        EditText password = (EditText) this.findViewById(R.id.editPassword);
        password.setText("");
        EditText usuario = (EditText) this.findViewById(R.id.editUsuario);
        usuario.setText(ManejadorDeToken.obtenerUsuario(this));
        usuario.setSelection(usuario.getText().length());
        autoIniciarSesion();
    }
    public void cambiarActivity(String usuario){
        Intent serv = new Intent(this, ServicioDeNotificaciones.class);
        startService(serv);
        Intent intent = new Intent(this, ListaDeConversacionesActivity.class);
        intent.putExtra(APIConstantes.CAMPO_USUARIO, usuario);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
    public void enunciarEstado(String estado){
        TextView error = (TextView) this.findViewById(R.id.textError);
        error.setText(estado);
        error.setVisibility(View.VISIBLE);
    }
    public void abrirConversaciones(View v) {
        final ProgressDialog dialogo = ConstructorDeDialogo.obtener(this);
        final EditText usuario = (EditText) this.findViewById(R.id.editUsuario);
        EditText password = (EditText) this.findViewById(R.id.editPassword);
        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados(){
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {
                dialogo.dismiss();
                ManejadorIniciarSesion manejador = new ManejadorIniciarSesion(resultado.getString());
                if (manejador.estadoValido()){
                    ManejadorDeToken.persistirToken(getApplicationContext(), manejador.obtenerToken());
                    ManejadorDeToken.persistirUsuario(getApplicationContext(), usuario.getText().toString());
                    cambiarActivity(usuario.getText().toString());
                }else{
                    enunciarEstado(manejador.obtenerEstado());
                }
            }
            @Override
            public void errorResultado(String error) {
                dialogo.dismiss();
                enunciarEstado(getResources().getString(R.string.error_conexion));
            }
        });
        dialogo.show();
        httpApi.iniciarSesion(usuario.getText().toString(), password.getText().toString());
    }
    public void autoIniciarSesion() {
        String token = ManejadorDeToken.obtenerToken(this);
        if ( token == null)
            return;
        cambiarActivity(ManejadorDeToken.obtenerUsuario(getApplicationContext()));
    }
    public void abrirRegistro(View v) {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }
}