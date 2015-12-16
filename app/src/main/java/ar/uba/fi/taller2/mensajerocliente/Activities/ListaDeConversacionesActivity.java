package ar.uba.fi.taller2.mensajerocliente.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import ar.uba.fi.taller2.mensajerocliente.Excepciones.ImposibleLeerObjetoJsonException;
import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.APIConstantes;
import ar.uba.fi.taller2.mensajerocliente.manejadores.HTTPApi;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorArchivarConversacion;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorCerrarSesion;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorListaConversaciones;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorNoLeidos;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorViajes;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ReceptorDeResultados;
import ar.uba.fi.taller2.mensajerocliente.manejadores.RespuestaHTTP;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ResumenConversacion;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ActualizadorDeImagen;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ConstructorDeDialogo;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ManejadorDeToken;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ServicioDeNotificaciones;

/**
 * Activity de lista de conversaciones
 */

public class ListaDeConversacionesActivity extends ActionBarActivity {
    private static final String INICIAL = "inicial";
    private static final String ACTUALIZADA = "actualizada";
    LinearLayout l;
    String usuario;
    String conversacionParaArchivar;
    ProgressBar progressBar;
    Timer timer;
    Timer timerCoincidencias;
    EditText textBuscador;
    LinkedHashMap<String, Boolean> estadoLecturaConversaciones;
    boolean eventoRecibido, eventoRecibidoCoincidencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.estadoLecturaConversaciones = new LinkedHashMap<String, Boolean>();

        setContentView(R.layout.activity_lista_de_conversaciones);

        generarListaConversaciones(INICIAL);

        this.l = (LinearLayout) findViewById(R.id.linear);

        this.textBuscador = (EditText) findViewById(R.id.textBuscador);

        this.textBuscador.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2o, int i3) {
                mostrarConversaciones(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (!ManejadorDeToken.esVersionGratuita(this)){
            mAdView.setVisibility(View.GONE);
        }


    }


    private void mostrarConversaciones(String nombre) {
        for (int i = 0; i < this.l.getChildCount(); i++) {
            View v = this.l.getChildAt(i);
            String nombreConversacion = v.getTag().toString();
            if (!nombreConversacion.toLowerCase().contains(nombre.toLowerCase())){
                this.l.findViewWithTag(nombreConversacion).setVisibility(View.GONE);
            }else{
                this.l.findViewWithTag(nombreConversacion).setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        this.eventoRecibido = true;
        this.eventoRecibidoCoincidencias = true;
        this.iniciarTimerBackground(4000);
        this.buscarCoincidenciasEnBackground(10000);
        this.textBuscador.setText("");
        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {
                try {
                    JSONObject object = new JSONObject(resultado.getString());
                    String usuarios = object.getString(APIConstantes.CLAVE_LISTA_USUARIOS);
                    JSONArray listaUsuarios = new JSONArray(usuarios);
                    for (int i = 0; i < listaUsuarios.length(); i++){
                        System.out.println(listaUsuarios.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorResultado(String error) {
                System.out.println(error);
            }
        });

        httpApi.listaUsuarios();
    }

    private void buscarCoincidencias(){

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {

                eventoRecibidoCoincidencias = true;
                System.out.println(resultado.getString());
            }

            @Override
            public void errorResultado(String error) {
                System.out.println(error);

            }
        });

        httpApi.buscarCoincidencias(ManejadorDeToken.obtenerToken(this));

    }

    private void buscarCoincidenciasEnBackground(int tiempoEnMS) {
        if (this.timerCoincidencias != null){
            this.timerCoincidencias.cancel();
        }

        this.timerCoincidencias = new Timer();

        this.timerCoincidencias.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        if (eventoRecibidoCoincidencias) {
                            eventoRecibidoCoincidencias = false;
                            buscarCoincidencias();
                        }

                    }
                },
                0,      // run first occurrence immediately
                tiempoEnMS);  // run every three seconds
    }

    @Override
    protected void onPause(){
        super.onPause();
        this.timerCoincidencias.cancel();
        this.timer.cancel();
    }

    @Override
    protected void onStart(){
        super.onStart();
        this.eventoRecibido = true;
        this.eventoRecibidoCoincidencias = true;
        Intent intent = getIntent();
        this.usuario = intent.getStringExtra(APIConstantes.CAMPO_USUARIO);
        if (ManejadorViajes.obtenerViajes(this)==null){
            intent = new Intent(this, RegistrarViajeActivity.class);
            intent.putExtra(APIConstantes.CAMPO_USUARIO, usuario);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);

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
                            obtenerNotificaciones();
                        }

                    }
                },
                0,      // run first occurrence immediately
                tiempoEnMS);  // run every three seconds
    }

    private void obtenerNotificaciones() {
        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {

                ManejadorNoLeidos manejador = new ManejadorNoLeidos(resultado.getString());
                try {
                    if (manejador.hayNotificaciones()) {
                        notificarAUsuarios(manejador.usuariosANotificar());
                    }else{
                        generarListaConversaciones(ACTUALIZADA);
                    }
                }catch (ImposibleLeerObjetoJsonException e){
                    e.printStackTrace();
                }

            }
            @Override
            public void errorResultado(String error) {

            }
        });

        httpApi.notificacionNoLeidos(ManejadorDeToken.obtenerToken(this));
    }

    private void notificarAUsuarios(ArrayList<String> usuarios) {
        generarListaConversaciones(ACTUALIZADA);
        for (int i = 0; i < usuarios.size(); i++){
            if (this.l.findViewWithTag(usuarios.get(i))!=null){
                this.l.findViewWithTag(usuarios.get(i)).setBackgroundColor(APIConstantes.COLOR_NUEVO_MENSAJE);

            }
        }
    }

    private void generarListaConversaciones(final String estado){

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {

            @Override
            public void recibirResultado(RespuestaHTTP resultado) {

                ManejadorListaConversaciones manejador = new ManejadorListaConversaciones(resultado.getString());
                if (estado.compareTo(INICIAL) == 0) {
                    cargarListaInicialConversaciones(manejador.obtenerLista());
                    iniciarTimerBackground(4000);

                } else if (estado.compareTo(ACTUALIZADA) == 0) {
                    actualizarActivity(manejador.obtenerLista());
                }
                eventoRecibido = true;

            }

            @Override
            public void errorResultado(String error) {
                progressBar.setVisibility(View.GONE);
            }
        });

        httpApi.resumenesConversacion(ManejadorDeToken.obtenerToken(this));
    }

    private void cargarListaInicialConversaciones(LinkedList<ResumenConversacion> resumenes) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < resumenes.size(); i++){
            View v = inflater.inflate(R.layout.resumen_conversacion, l,false);
            v.setTag(resumenes.get(i).getUsuarioDestino());
            TextView usuarioDestino = (TextView) v.findViewById(R.id.textNickUsuario);
            TextView ultimoMensaje = (TextView) v.findViewById(R.id.textMensaje);
            TextView fecha = (TextView) v.findViewById(R.id.textFechaResumen);
            ImageView imagen = (ImageView) v.findViewById(R.id.imagen);
            ImageView imagenEstado = (ImageView) v.findViewById(R.id.imageEstado);
            ActualizadorDeImagen.actualizar(this, resumenes.get(i).getUsuarioDestino(), imagen);

            ultimoMensaje.setText(resumenes.get(i).getMensajeDelEmisor().getMensaje());
            usuarioDestino.setText(resumenes.get(i).getUsuarioDestino());
            fecha.setText(resumenes.get(i).getMensajeDelEmisor().getFecha());
            if (resumenes.get(i).getConexion()) {
                imagenEstado.setVisibility(View.VISIBLE);
            } else {
                imagenEstado.setVisibility(View.INVISIBLE);
            }
            RelativeLayout relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeResumen);
            registerForContextMenu(relativeLayout);
            l.addView(v,0);
        }

        ordenarLayout();
    }

    public void actualizarActivity(LinkedList<ResumenConversacion> resumenes)  {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < resumenes.size(); i++){
            if (this.l.findViewWithTag(resumenes.get(i).getUsuarioDestino())==null){
                crearView(resumenes.get(i));
            }else{
                actualizarView(this.l.findViewWithTag(resumenes.get(i).getUsuarioDestino()),resumenes.get(i));
            }

            ImageView imagenEstado = (ImageView) this.l.findViewWithTag(resumenes.get(i).getUsuarioDestino()).findViewById(R.id.imageEstado);

            if (resumenes.get(i).getConexion()) {
                imagenEstado.setVisibility(View.VISIBLE);
            } else {
                imagenEstado.setVisibility(View.INVISIBLE);
            }

        }

        ordenarLayout();
    }

    private void actualizarView(View vista, ResumenConversacion resumenConversacion) {
        TextView ultimoMensaje = (TextView) vista.findViewById(R.id.textMensaje);
        TextView fecha = (TextView) vista.findViewById(R.id.textFechaResumen);
        fecha.setText(resumenConversacion.getMensajeDelEmisor().getFecha());
        ultimoMensaje.setText(resumenConversacion.getMensajeDelEmisor().getMensaje());
        l.removeView(vista);
        l.addView(vista, 0);

    }

    private void crearView(ResumenConversacion resumenConversacion) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vista = inflater.inflate(R.layout.resumen_conversacion, l,false);
        vista.setTag(resumenConversacion.getUsuarioDestino());
        TextView usuarioDestino = (TextView) vista.findViewById(R.id.textNickUsuario);
        TextView ultimoMensaje = (TextView) vista.findViewById(R.id.textMensaje);
        TextView fecha = (TextView) vista.findViewById(R.id.textFechaResumen);
        ImageView imagen = (ImageView) vista.findViewById(R.id.imagen);
        ImageView imagenEstado = (ImageView) vista.findViewById(R.id.imageEstado);
        ActualizadorDeImagen.actualizar(this, resumenConversacion.getUsuarioDestino(), imagen);

        ultimoMensaje.setText(resumenConversacion.getMensajeDelEmisor().getMensaje());
        usuarioDestino.setText(resumenConversacion.getUsuarioDestino());
        fecha.setText(resumenConversacion.getMensajeDelEmisor().getFecha());

        RelativeLayout relativeLayout = (RelativeLayout) vista.findViewById(R.id.relativeResumen);

        registerForContextMenu(relativeLayout);

        l.addView(vista,0);

    }

    private void ordenarLayout() {
        //Bubble Sort
        int j;
        boolean flag = true;
        while (flag)
        {
            flag= false;
            for( j=0;  j < l.getChildCount() -1;  j++ )
            {
                String fecha1 = ((TextView)this.l.getChildAt(j).findViewById(R.id.textFechaResumen)).getText().toString();
                String fecha2 = ((TextView)this.l.getChildAt(j+1).findViewById(R.id.textFechaResumen)).getText().toString();
                if ( fecha1.compareTo(fecha2)<0)   // change to > for ascending sort
                {

                    View aux = this.l.getChildAt(j+1);
                    this.l.removeViewAt(j+1);
                    this.l.addView(aux,j);
                    flag = true;              //shows a swap occurred
                }
            }
        }
    }

    public void cerrarSesion(){


        final ProgressDialog dialogo = ConstructorDeDialogo.obtener(this);
        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados(){
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {
                dialogo.dismiss();
                ManejadorCerrarSesion manejador = new ManejadorCerrarSesion(resultado.getString());
                if (manejador.estadoValido()) {
                    ManejadorDeToken.borrarToken(getApplicationContext());
                    Intent serv = new Intent(getApplicationContext(), ServicioDeNotificaciones.class);
                    stopService(serv);
                    Intent intent = new Intent(getApplicationContext(), IniciarSesionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void errorResultado(String error) {
                dialogo.dismiss();
                Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
            }
        });
        dialogo.show();
        httpApi.cerrarSesion(ManejadorDeToken.obtenerToken(this));
    }


    @Override
    public void finish(){
        this.timer.cancel();
        this.timerCoincidencias.cancel();
        Log.i("Se cierra sesion", "ok");
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_de_conversaciones, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_archivar, menu);

        TextView usuarioDestino = (TextView) v.findViewById(R.id.textNickUsuario);

        conversacionParaArchivar = usuarioDestino.getText().toString();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_archivar:
                archivarConversacion(conversacionParaArchivar);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_editar_perfil) {
            Intent intent = new Intent(this, EditarPerfilActivity.class);
            intent.putExtra(APIConstantes.CAMPO_USUARIO, this.usuario);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_cerrar_sesion) {
            cerrarSesion();
            return true;

        } else if (id == R.id.action_ver_archivadas) {
            abrirConversacionesArchivadas();
            return true;
        } else if (id == R.id.action_mensaje_broadcast){
            enviarMensajeBroadcast();
        } else if (id == R.id.action_registrar_viaje){
            Intent intent = new Intent(this, RegistrarViajeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } else if (id == R.id.action_ver_viajes){
            abrirMisViajes();
        }

        return super.onOptionsItemSelected(item);
    }

    public void abrirConversacion(View v) {

        TextView usuarioDestino = (TextView) v.findViewById(R.id.textNickUsuario);
        v.setBackgroundColor(APIConstantes.COLOR_MENSAJE_LEIDO);
        Intent intent = new Intent(this, ConversacionActivity.class);
        intent.putExtra(APIConstantes.CAMPO_USUARIO_DESTINO, usuarioDestino.getText());
        startActivity(intent);

    }

    public void archivarConversacion(final String usuario){

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados(){
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {
                ManejadorArchivarConversacion manejador = new ManejadorArchivarConversacion(resultado.getString());

                if (manejador.estadoValido()) {
                    Toast.makeText(getApplicationContext(), R.string.conversacion_archivada, Toast.LENGTH_SHORT).show();
                    l.removeView(l.findViewWithTag(usuario));
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void errorResultado(String error) {
                Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
            }
        });

        httpApi.archivarConversacion(ManejadorDeToken.obtenerToken(this), usuario);
    }

    public void abrirConversacionesArchivadas() {
        Intent intent = new Intent(this, ListaDeConversacionesArchivadasActivity.class);
        intent.putExtra(APIConstantes.CAMPO_USUARIO, this.usuario);
        startActivity(intent);
    }


    public void abrirMisViajes(){
        Intent intent = new Intent(this, ViajesRegistradosActivity.class);
        startActivity(intent);
    }
    public void enviarMensajeBroadcast(){
        Intent intent = new Intent(this, BroadcastActivity.class);
        startActivity(intent);
    }

}