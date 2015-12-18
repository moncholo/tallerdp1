package ar.uba.fi.taller2.mensajerocliente.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.APIConstantes;
import ar.uba.fi.taller2.mensajerocliente.manejadores.HTTPApi;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorActualizarUbicacion;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorViajes;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ReceptorDeResultados;
import ar.uba.fi.taller2.mensajerocliente.manejadores.RespuestaHTTP;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ConstructorDeDialogo;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ManejadorDeToken;

/**
 * Created by juan on 15/12/15.
 */
public class ViajesRegistradosActivity extends ActionBarActivity{

    LinearLayout l;
    private View viewAEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viajes_registrados);
        this.l = (LinearLayout) findViewById(R.id.linearViajes);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_eliminar_viaje, menu);

        this.viewAEliminar = v;

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_eliminar_viaje :
                eliminarViaje();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void eliminarViaje(){
        TextView destino = (TextView) this.viewAEliminar.findViewById(R.id.textDestinoViaje);
        TextView fechasViaje = (TextView) this.viewAEliminar.findViewById(R.id.textFechasViaje);

        String fechasDelViaje = fechasViaje.getText().toString();
        String[] parts = fechasDelViaje.split("a");
        String desde = parts[0].trim();
        String hasta = parts[1].trim();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = (Date)formatter.parse(desde);
            desde = Long.toString(date.getTime());

            date = (Date)formatter.parse(hasta);
            hasta = Long.toString(date.getTime());
            ManejadorViajes.eliminarViaje(this, destino.getText().toString(), desde, hasta);
            this.l.removeView(this.viewAEliminar);
            actualizarFiltros(ManejadorViajes.obtenerViajes(this));


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void actualizarFiltros(String filtros) {

        final ProgressDialog dialogo = ConstructorDeDialogo.obtener(this);

        HTTPApi httpApi = new HTTPApi(new ReceptorDeResultados() {
            @Override
            public void recibirResultado(RespuestaHTTP resultado) {
                dialogo.dismiss();
                ManejadorActualizarUbicacion manejador = new ManejadorActualizarUbicacion(resultado.getString());

            }

            @Override
            public void errorResultado(String error) {
                Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_SHORT).show();
            }
        });

        dialogo.show();
        httpApi.actualizarFiltros(ManejadorDeToken.obtenerToken(this), filtros);


    }


    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("Ok, entra aqui al resume");

        JSONArray viajes = null;
        JSONObject viaje = null;
        try {
            viajes = new JSONArray(ManejadorViajes.obtenerViajes(this));
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i=0; i < viajes.length(); i++){
                View v = inflater.inflate(R.layout.resumen_viaje, l, false);
                v.setBackgroundColor(APIConstantes.COLOR_VIAJE_REGISTRADO);

                TextView destino = (TextView) v.findViewById(R.id.textDestinoViaje);
                TextView fechaViaje = (TextView) v.findViewById(R.id.textFechasViaje);
                viaje = viajes.getJSONObject(i);
                destino.setText(viaje.getString("Destino"));
                String desde = viaje.getString("Desde");
                String hasta = viaje.getString("Hasta");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                formatter.setTimeZone(TimeZone.getDefault());
                Date fechaDesde = new Date(Long.parseLong(desde));
                Date fechaHasta = new Date(Long.parseLong(hasta));

                desde= formatter.format(fechaDesde);
                fechaViaje.setText(formatter.format(fechaDesde)+" a "+ formatter.format(fechaHasta));
                RelativeLayout relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeResumenViaje);
                registerForContextMenu(relativeLayout);
                l.addView(v, 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
