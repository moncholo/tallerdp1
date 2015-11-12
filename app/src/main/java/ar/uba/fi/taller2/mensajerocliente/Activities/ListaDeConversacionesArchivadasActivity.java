package ar.uba.fi.taller2.mensajerocliente.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.APIConstantes;
import ar.uba.fi.taller2.mensajerocliente.manejadores.HTTPApi;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorArchivarConversacion;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorListaConversaciones;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ReceptorDeResultados;
import ar.uba.fi.taller2.mensajerocliente.manejadores.RespuestaHTTP;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ResumenConversacion;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ActualizadorDeImagen;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ManejadorDeToken;

/**
 * Activity de registracion
 */

public class ListaDeConversacionesArchivadasActivity extends ActionBarActivity {

    LinearLayout l;
    String usuario;
    String conversacionParaDesarchivar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_de_conversaciones_archivadas);
    }

    @Override
    protected void onStart(){
        super.onStart();

        Intent intent = getIntent();
        this.usuario = intent.getStringExtra(APIConstantes.CAMPO_USUARIO);

        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        generarListaConversaciones();
    }

    private void generarListaConversaciones(){

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {

            @Override
            public void recibirResultado(RespuestaHTTP resultado) {

                ManejadorListaConversaciones manejador = new ManejadorListaConversaciones(resultado.getString());

                progressBar.setVisibility(View.GONE);
                actualizarActivity(manejador.obtenerLista());

            }

            @Override
            public void errorResultado(String error) {
                progressBar.setVisibility(View.GONE);
            }
         });

        progressBar.setVisibility(View.VISIBLE);
        httpApi.resumenesConversacionArchivadas(ManejadorDeToken.obtenerToken(this));
    }

    public void actualizarActivity(LinkedList<ResumenConversacion> resumenes)  {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        l = (LinearLayout) findViewById(R.id.linear);
        l.removeAllViews();

        for (int i = 0; i < resumenes.size(); i++){
            View v = inflater.inflate(R.layout.resumen_conversacion, l,false);
            TextView usuarioDestino = (TextView) v.findViewById(R.id.textNickUsuario);
            usuarioDestino.setText(resumenes.get(i).getUsuarioDestino());
            TextView ultimoMensaje = (TextView) v.findViewById(R.id.textMensaje);
            ImageView imagen = (ImageView) v.findViewById(R.id.imagen);
            TextView fecha = (TextView) v.findViewById(R.id.textFechaResumen);
            ImageView imagenEstado = (ImageView) v.findViewById(R.id.imageEstado);
            ActualizadorDeImagen.actualizar(this, resumenes.get(i).getUsuarioDestino(), imagen);
            ultimoMensaje.setText(resumenes.get(i).getMensajeDelEmisor().getMensaje());
            fecha.setText(resumenes.get(i).getMensajeDelEmisor().getFecha());
            if (resumenes.get(i).getConexion()) {
                imagenEstado.setVisibility(View.VISIBLE);
            } else {
                imagenEstado.setVisibility(View.INVISIBLE);
            }

            RelativeLayout relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeResumen);

            registerForContextMenu(relativeLayout);

            l.addView(v);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_desarchivar, menu);

        TextView usuarioDestino = (TextView) v.findViewById(R.id.textNickUsuario);

        conversacionParaDesarchivar = usuarioDestino.getText().toString();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_desarchivar:
                desarchivarConversacion(conversacionParaDesarchivar);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void abrirConversacion(View v) {
        TextView usuarioDestino = (TextView) v.findViewById(R.id.textNickUsuario);

        Intent intent = new Intent(this, ConversacionActivity.class);
        intent.putExtra(APIConstantes.CAMPO_USUARIO_DESTINO, usuarioDestino.getText());
        startActivity(intent);
    }

    public void desarchivarConversacion(String usuario){

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados(){
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {
                ManejadorArchivarConversacion manejador = new ManejadorArchivarConversacion(resultado.getString());

                if (manejador.estadoValido()) {
                    Toast.makeText(getApplicationContext(), R.string.conversacion_desarchivada, Toast.LENGTH_SHORT).show();
                    generarListaConversaciones();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void errorResultado(String error) {
                Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
            }
        });

        httpApi.desarchivarConversacion(ManejadorDeToken.obtenerToken(this), usuario);
    }

}
