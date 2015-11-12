package ar.uba.fi.taller2.mensajerocliente.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.APIConstantes;
import ar.uba.fi.taller2.mensajerocliente.manejadores.HTTPApi;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorActualizarUbicacion;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorEditarPerfil;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorSubirImagen;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorVerPerfil;
import ar.uba.fi.taller2.mensajerocliente.manejadores.Perfil;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ReceptorDeResultados;
import ar.uba.fi.taller2.mensajerocliente.manejadores.RespuestaHTTP;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ActualizadorDeImagen;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ConstructorDeDialogo;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ManejadorDeToken;

/**
 * Activity de editar perfil
 */
public class EditarPerfilActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String usuario;
    private static final int ELEGIR_IMAGEN = 1;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        this.buildGoogleApiClient();
    }

    @Override
    protected void onStart(){
        super.onStart();

        Intent intent = getIntent();
        this.usuario = intent.getStringExtra(APIConstantes.CAMPO_USUARIO);

        obtenerMiPerfil();
        ActualizadorDeImagen.actualizar(getApplicationContext(), usuario, (ImageButton) findViewById(R.id.imagePerfil));

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void obtenerMiPerfil() {
        final ProgressDialog dialogo = ConstructorDeDialogo.obtener(this);

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {
                dialogo.dismiss();
                ManejadorVerPerfil manejador = new ManejadorVerPerfil(resultado.getString());

                if (manejador.estadoValido()) {
                    Perfil perfil = manejador.getPerfil();

                    EditText nick = (EditText) findViewById(R.id.editNick);
                    EditText conexion = (EditText) findViewById(R.id.editConexion);

                    nick.setText(perfil.getNick());
                    nick.setSelection(nick.getText().length());
                    conexion.setText(perfil.getConexion());
                }

            }

            @Override
            public void errorResultado(String error) {
                dialogo.dismiss();
                Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
            }
        });

        dialogo.show();
        httpApi.verPerfil(ManejadorDeToken.obtenerToken(this), this.usuario);
    }

    private void guardarMiPerfil() {

        final ProgressDialog dialogo = ConstructorDeDialogo.obtener(this);

        final EditText nick = (EditText) findViewById(R.id.editNick);
        final EditText conexion = (EditText) findViewById(R.id.editConexion);
        final EditText passVieja = (EditText) findViewById(R.id.editPasswordVieja);
        final EditText passNueva = (EditText) findViewById(R.id.editPasswordNueva);

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {
                dialogo.dismiss();
                ManejadorEditarPerfil manejador = new ManejadorEditarPerfil(resultado.getString());

                obtenerMiPerfil();

                if (!manejador.estadoValido()) {
                    Toast.makeText(getApplicationContext(), manejador.obtenerEstado(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.perfil_actualizado, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void errorResultado(String error) {
                dialogo.dismiss();
                Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
            }
        });

        dialogo.show();
        httpApi.editarPerfil(ManejadorDeToken.obtenerToken(this), nick.getText().toString(), conexion.getText().toString(), passVieja.getText().toString(), passNueva.getText().toString());

        passVieja.setText("");
        passNueva.setText("");
    }

    private void actualizarUbicacion() {

        final ProgressDialog dialogo = ConstructorDeDialogo.obtener(this);

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {
                dialogo.dismiss();
                ManejadorActualizarUbicacion manejador = new ManejadorActualizarUbicacion(resultado.getString());

                obtenerMiPerfil();

                if (!manejador.estadoValido()) {
                    Toast.makeText(getApplicationContext(), manejador.obtenerEstado(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.perfil_actualizado, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void errorResultado(String error) {
                dialogo.dismiss();
                Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
            }
        });

        if (mLastLocation != null) {
            String latitud = String.valueOf(mLastLocation.getLatitude());
            String longitud = String.valueOf(mLastLocation.getLongitude());

            httpApi.actualizarUbicacion(ManejadorDeToken.obtenerToken(this), latitud, longitud);
        } else {
            buildGoogleApiClient();
            Toast.makeText(getApplicationContext(), R.string.activar_ubicacion, Toast.LENGTH_SHORT).show();
        }

    }

    public void guardarPerfil(View v) {
        guardarMiPerfil();
    }

    public void cambiarImagen(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ELEGIR_IMAGEN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ELEGIR_IMAGEN) {
                Uri imagen = data.getData();

                Log.i("DATA IM", getPath(imagen));
                subirImagen(getPath(imagen));
            }
        }
    }

    public String getPath(Uri uri) {
        String result = "";
        boolean isok = false;

        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getContentResolver().query(uri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            isok = true;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return isok ? result : "";
    }

    public void subirImagen(String archivo) {
        final ProgressDialog dialogo = ConstructorDeDialogo.obtener(this);

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {
                dialogo.dismiss();

                ManejadorSubirImagen manejador = new ManejadorSubirImagen(resultado.getString());

                obtenerMiPerfil();

                if (!manejador.estadoValido()) {
                    Toast.makeText(getApplicationContext(), manejador.obtenerEstado(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.perfil_actualizado, Toast.LENGTH_SHORT).show();
                    ActualizadorDeImagen.borrarCache(usuario);
                    ActualizadorDeImagen.actualizar(getApplicationContext(), usuario, (ImageButton) findViewById(R.id.imagePerfil));
                }
            }

            @Override
            public void errorResultado(String error) {
                dialogo.dismiss();
                Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
            }
        });

        dialogo.show();
        httpApi.subirImagen(ManejadorDeToken.obtenerToken(this), archivo);

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void actualizarUbicacion(View v) {
        actualizarUbicacion();
    }
}
