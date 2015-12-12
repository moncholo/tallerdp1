package ar.uba.fi.taller2.mensajerocliente.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import ar.uba.fi.taller2.mensajerocliente.Excepciones.ImposibleCrearConversacionException;
import ar.uba.fi.taller2.mensajerocliente.Excepciones.ImposibleLeerObjetoJsonException;
import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.APIConstantes;
import ar.uba.fi.taller2.mensajerocliente.manejadores.Conversacion;
import ar.uba.fi.taller2.mensajerocliente.manejadores.HTTPApi;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorConversacion;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorVerPerfil;
import ar.uba.fi.taller2.mensajerocliente.manejadores.Perfil;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ReceptorDeResultados;
import ar.uba.fi.taller2.mensajerocliente.manejadores.RespuestaHTTP;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ActualizadorDeImagen;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ManejadorDeToken;

/**
 * Activity de una conversacion
 */

public class ConversacionActivity extends ActionBarActivity {
    String usuarioDestino;
    LinearLayout l;
    private long ultimaFecha;
    private Timer timer;
    boolean eventoRecibido = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversacion);
        this.l = (LinearLayout) findViewById(R.id.linearConversacion);

    }

    private void iniciarTimerBackground(int tiempoEnMS) {

        if (this.timer != null){
            this.timer.cancel();
        }

        this.timer = new Timer();

        this.timer.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        if (eventoRecibido) {
                            eventoRecibido = false;
                            obtenerUltimosMensajes();
                        }

                    }
                },
                0,      // run first occurrence immediately
                tiempoEnMS);  // run every three seconds
    }



    protected void onResume(){
        super.onResume();
        this.eventoRecibido = false;
        this.iniciarTimerBackground(1500);
    }
    protected void onPause(){
        super.onPause();
        this.eventoRecibido = false;
        this.timer.cancel();
    }
    protected void onStart() {

        super.onStart();
        Intent intent = getIntent();
        this.usuarioDestino = intent.getStringExtra(APIConstantes.CAMPO_USUARIO_DESTINO);
        obtenerMiniPerfil();
        obtenerMensajes();
    }

    private void obtenerUltimosMensajes(){

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {
                ManejadorConversacion manejadorConversacion = new ManejadorConversacion(resultado.getString());
                try {
                    Log.i("El resultado es: ", resultado.getString());
                    crearConversacion2(manejadorConversacion.crearUltimosMensajes());
                    TextView textUltimaConexion = (TextView) findViewById(R.id.textUltimaConexion);
                    textUltimaConexion.setText(manejadorConversacion.getConexion());
                }catch (ImposibleLeerObjetoJsonException e) {
                    e.printStackTrace();
                }

                eventoRecibido = true;
            }
            @Override
            public void errorResultado(String error) {

            }
        });

        httpApi.obtenerUltimosMensajes(ManejadorDeToken.obtenerToken(this), this.usuarioDestino, this.ultimaFecha);

    }
    private void obtenerMensajes(){

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {
                ManejadorConversacion manejadorConversacion = new ManejadorConversacion(resultado.getString());
                try {
                    try {
                        crearConversacion(manejadorConversacion.crearConversacion());
                        TextView textUltimaConexion = (TextView) findViewById(R.id.textUltimaConexion);
                        textUltimaConexion.setText(manejadorConversacion.getConexion());
                    } catch (ImposibleLeerObjetoJsonException e) {
                        e.printStackTrace();
                    }
                } catch (ImposibleCrearConversacionException e) {
                    e.printStackTrace();
                }

                eventoRecibido = true;
            }

            @Override
            public void errorResultado(String error) {

            }
        });

        httpApi.conversacion(ManejadorDeToken.obtenerToken(this), this.usuarioDestino);

    }

    private void crearConversacion2(Conversacion conversacion){


        boolean verificarPopUps = false;

        if (ultimaFecha>0){
            verificarPopUps = true;
        }
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (conversacion.getMensajes().size()>0) {
            for (int i = conversacion.getMensajes().size() - 1; i >= 0; i--) {

                if (conversacion.getMensajes().get(i).getMensaje().compareTo("")!=0) {
                    View v;

                    Log.i("Mensaje ok", String.valueOf(conversacion.getMensajes().size()));
                    if (conversacion.getMensajes().get(i).getUsuario().compareTo(this.usuarioDestino) == 0) {
                        v = inflater.inflate(R.layout.mensaje_conversacion_destino, l, false);
                    } else {
                        v = inflater.inflate(R.layout.mensaje_conversacion, l, false);
                    }

                    TextView mensaje = (TextView) v.findViewById(R.id.textMensaje);
                    TextView fecha = (TextView) v.findViewById(R.id.textFecha);
                    TextView textUsuario = (TextView) v.findViewById(R.id.textNombre);

                    textUsuario.setText(conversacion.getMensajes().get(i).getUsuario());
                    fecha.setText(conversacion.getMensajes().get(i).getFecha());
                    mensaje.setText(conversacion.getMensajes().get(i).getMensaje().trim());
                    this.ultimaFecha = conversacion.getMensajes().get(i).getFechaFormatoEntero();
                    if (verificarPopUps){
                        verificarPopUpMensaje(conversacion.getMensajes().get(i).getMensaje().trim());
                    }
                    l.addView(v);
                }
            }

            final ScrollView scroll = (ScrollView) findViewById(R.id.scrollMensajes);
            scroll.post(new Runnable() {
                @Override
                public void run() {
                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    private void verificarPopUpMensaje(String mensaje){

        String stringLink = "";
        if (mensaje.contains("telo") || mensaje.contains("hacer el amor")){
            stringLink = getString(R.string.linkTelo);
        }else if (mensaje.contains("cine")){
            stringLink = getString(R.string.linkCine);
        }

        if (stringLink != ""){
            LayoutInflater layoutInflater
                    = (LayoutInflater)getBaseContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.popup, null);
            final PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
            btnDismiss.setOnClickListener(new Button.OnClickListener(){

                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }});
            popupWindow.showAsDropDown(findViewById(R.id.headerConversacion));

            TextView link = (TextView) popupView.findViewById(R.id.textLink);
            link.setText(stringLink);
        }


    }

    private void crearConversacion(Conversacion conversacion){


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        l.removeAllViews();
        for (int i = 0; i < conversacion.getMensajes().size(); i++) {
            View v;
            Log.i("Mensaje ok", conversacion.getMensajes().get(i).getMensaje());
            if (conversacion.getMensajes().get(i).getUsuario().compareTo(this.usuarioDestino)==0) {
                v = inflater.inflate(R.layout.mensaje_conversacion_destino, l, false);
            }else{
                v = inflater.inflate(R.layout.mensaje_conversacion, l, false);
            }

            TextView mensaje = (TextView)v.findViewById(R.id.textMensaje);
            TextView fecha = (TextView)v.findViewById(R.id.textFecha);

            fecha.setText(conversacion.getMensajes().get(i).getFecha());
            mensaje.setText(conversacion.getMensajes().get(i).getMensaje().trim());
            this.ultimaFecha = conversacion.getMensajes().get(i).getFechaFormatoEntero();
            l.addView(v);
        }
        if (conversacion.getMensajes().size()!=0) {
            final ScrollView scroll = (ScrollView) findViewById(R.id.scrollMensajes);
            scroll.post(new Runnable() {
                @Override
                public void run() {
                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    private void obtenerMiniPerfil() {

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {

            @Override
            public void recibirResultado(RespuestaHTTP resultado) {

                ManejadorVerPerfil manejador = new ManejadorVerPerfil(resultado.getString());

                if (manejador.estadoValido()) {
                    Perfil perfil = manejador.getPerfil();
                    TextView nick = (TextView) findViewById(R.id.textNick);
                    TextView ultimaConexion = (TextView) findViewById(R.id.textUltimaConexion);
                    nick.setText(perfil.getNick());
                    Date fecha = new Date(perfil.getUltimaConexion() * 1000);

                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                    formatter.setTimeZone(TimeZone.getDefault());

                    //ultimaConexion.setText(formatter.format(fecha));
                }

            }

            @Override
            public void errorResultado(String error) {
                Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
            }
        });

        httpApi.verPerfil(ManejadorDeToken.obtenerToken(this), this.usuarioDestino);
        ActualizadorDeImagen.actualizar(getApplicationContext(), this.usuarioDestino, (ImageView) findViewById(R.id.imagePerfil));
    }

    private void enviarMensaje(String mensaje){

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
            }

            @Override
            public void errorResultado(String error) {
                Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
                textMensaje.setEnabled(true);
                buttonEnviar.setEnabled(true);
                buttonEnviar.setText(R.string.boton_enviar);
            }
        });

        if (mensaje.trim().compareTo("")!=0) {
            httpApi.enviarMensaje(ManejadorDeToken.obtenerToken(this), mensaje, this.usuarioDestino);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conversacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void enviarMensaje(View v) {
        TextView mensaje = (TextView)findViewById(R.id.editMensaje);
        Button enviar = (Button) findViewById(R.id.buttonEnviar);
        mensaje.setEnabled(false);
        enviar.setEnabled(false);
        this.enviarMensaje(mensaje.getText().toString());
    }

    public void abrirPerfil(View v) {
        Intent intent = new Intent(this, VerPerfilActivity.class);
        intent.putExtra(APIConstantes.CAMPO_USUARIO, this.usuarioDestino);
        startActivity(intent);
    }
}