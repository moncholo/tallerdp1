package ar.uba.fi.taller2.mensajerocliente.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.APIConstantes;
import ar.uba.fi.taller2.mensajerocliente.manejadores.HTTPApi;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorVerPerfil;
import ar.uba.fi.taller2.mensajerocliente.manejadores.Perfil;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ReceptorDeResultados;
import ar.uba.fi.taller2.mensajerocliente.manejadores.RespuestaHTTP;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ActualizadorDeImagen;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ManejadorDeToken;

/**
 * Activity para ver Perfil
 */
public class VerPerfilActivity extends ActionBarActivity implements OnMapReadyCallback {

    private String usuario;
    private GoogleMap mMap;
    private boolean mapaListo;
    private GoogleMap mapaGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentMapa);
        SupportMapFragment mapFragment = (SupportMapFragment) fragment;
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart(){
        super.onStart();

        Intent intent = getIntent();
        this.usuario = intent.getStringExtra(APIConstantes.CAMPO_USUARIO);

        obtenerPerfil();
    }

    private void obtenerPerfil() {

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {

                ManejadorVerPerfil manejador = new ManejadorVerPerfil(resultado.getString());

                if (manejador.estadoValido()) {
                    Perfil perfil = manejador.getPerfil();

                    TextView nickUsuario = (TextView) findViewById(R.id.textNickUsuario);
                    TextView conexion = (TextView) findViewById(R.id.textConexion);
                    TextView ultimaConexion = (TextView) findViewById(R.id.textUltimaConexion);
                    TextView ubicacion = (TextView) findViewById(R.id.textUbicacion);

                    nickUsuario.setText(perfil.getNick() + " (" + usuario + ")");
                    conexion.setText(perfil.getConexion());
                    ubicacion.setText(perfil.getUbicacionCercana());
                    Date fecha = new Date(perfil.getUltimaConexion() * 1000);

                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                    formatter.setTimeZone(TimeZone.getDefault());

                    ultimaConexion.setText(formatter.format(fecha));

                    moverMapa(perfil.getLatitud(), perfil.getLongitud());
                }

            }

            @Override
            public void errorResultado(String error) {
                Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
            }
        });

        httpApi.verPerfil(ManejadorDeToken.obtenerToken(this), this.usuario);
        ActualizadorDeImagen.actualizar(getApplicationContext(), usuario, (ImageView) findViewById(R.id.imagePerfil));

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //this.mostrarMapa();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ver_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapaListo = true;
        mapaGoogle = googleMap;


        googleMap.setMyLocationEnabled(true);

    }

    public void moverMapa(double latitud, double longitud) {

        if (!mapaListo)
            return;

        LatLng location = new LatLng(latitud, longitud);
        mapaGoogle.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

        mapaGoogle.addMarker(new MarkerOptions()
                .title("Ubicacion")
                .snippet("Desc.")
                .position(location));
    }
}
