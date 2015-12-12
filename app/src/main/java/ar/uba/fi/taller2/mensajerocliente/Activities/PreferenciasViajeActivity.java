package ar.uba.fi.taller2.mensajerocliente.Activities;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.taller2.mensajerocliente.InfoCiudades.MiamiInfo;
import ar.uba.fi.taller2.mensajerocliente.InfoCiudades.NuevaYorkInfo;
import ar.uba.fi.taller2.mensajerocliente.InfoCiudades.ParisInfo;
import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.APIConstantes;
import ar.uba.fi.taller2.mensajerocliente.manejadores.HTTPApi;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorActualizarUbicacion;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorVerPerfil;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ManejadorViajes;
import ar.uba.fi.taller2.mensajerocliente.manejadores.Perfil;
import ar.uba.fi.taller2.mensajerocliente.manejadores.ReceptorDeResultados;
import ar.uba.fi.taller2.mensajerocliente.manejadores.RespuestaHTTP;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ConstructorDeDialogo;
import ar.uba.fi.taller2.mensajerocliente.utilidades.ManejadorDeToken;
import ar.uba.fi.taller2.mensajerocliente.utilidades.MultiSelectionSpinner;
public class PreferenciasViajeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private MultiSelectionSpinner multiSelectionSpinnerMuseos, multiSelectionSpinnerRestaurantes, multiSelectionSpinnerBares, multiSelectionSpinnerMonumentos;
    private String desde, hasta, city, usuario;
    private int edadDesde, edadHasta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias_viaje);
        multiSelectionSpinnerMuseos = (MultiSelectionSpinner) findViewById(R.id.spinner_museos);
        multiSelectionSpinnerRestaurantes = (MultiSelectionSpinner) findViewById(R.id.spinner_restaurantes);
        multiSelectionSpinnerBares = (MultiSelectionSpinner) findViewById(R.id.spinner_bares);
        multiSelectionSpinnerMonumentos = (MultiSelectionSpinner) findViewById(R.id.spinner_monumetos);
        Intent intent = getIntent();
        this.city = intent.getStringExtra(RegistrarViajeActivity.CITY);
        this.desde = intent.getStringExtra(APIConstantes.FECHA_IDA);
        this.hasta = intent.getStringExtra(APIConstantes.FECHA_VUELTA);
        this.edadDesde = 18;
        this.edadHasta = 18;
        this.determinarCiudad(city);
        this.crearSpinner();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //getting age you selected
        int age = Integer.parseInt(parent.getItemAtPosition(position).toString());
        if (parent.getId() == R.id.spinnerHasta){
            this.edadHasta = age;
        }else if (parent.getId() == R.id.spinnerDesde){
            this.edadDesde = age;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void crearSpinner(){
        Spinner s = (Spinner) findViewById(R.id.spinnerDesde);
        Spinner hasta = (Spinner) findViewById(R.id.spinnerHasta);

        //creating a list of data to show
        List age = new ArrayList<>();
        for (int i=18; i < 100; i++){
            age.add(i);
        }
        //creating a adapter for spinner
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,age);

        //used to set layout style
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //connecting adapter to spinner object.
        s.setAdapter(adapter);
        hasta.setAdapter(adapter);

        //setting on item selected listener
        s.setOnItemSelectedListener(this);
        hasta.setOnItemSelectedListener(this);
    }
    public void guardarSeleccion(View v) {

        ManejadorViajes unManejador = new ManejadorViajes(this.city, this.desde, this.hasta, this.multiSelectionSpinnerMuseos.getSelectedStrings(),
                this.multiSelectionSpinnerBares.getSelectedStrings(), this.multiSelectionSpinnerRestaurantes.getSelectedStrings(),
                this.multiSelectionSpinnerMonumentos.getSelectedStrings(), this.edadDesde, this.edadHasta);
        String viaje = unManejador.crearJson();

        if (ManejadorViajes.obtenerViajes(this) == null) {
            ManejadorViajes.persistirViajes(this, viaje);
        } else {
            ManejadorViajes.agregarYPersistir(this, viaje);
        }

        actualizarFiltros(ManejadorViajes.obtenerViajes(this));

        Intent intent = new Intent(this, ListaDeConversacionesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);

        super.finish();
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



    public void determinarCiudad(String ciudad){
        String[] museosArray = null;
        String[] restArray = null;
        String[] baresArray = null;
        String[] monumetosArray = null;
        boolean notFound = false;
        switch (ciudad){
            case "Paris, Francia":
                museosArray = ParisInfo.getMuseos();
                restArray = ParisInfo.getRestaurantes();
                baresArray = ParisInfo.getBares();
                monumetosArray = ParisInfo.getMonumetos();
                break;
            case "Nueva York, Estados Unidos":
                museosArray = NuevaYorkInfo.getMuseos();
                restArray = NuevaYorkInfo.getRestaurantes();
                baresArray = NuevaYorkInfo.getBares();
                monumetosArray = NuevaYorkInfo.getMonumetos();
                break;
            case "Miami, Estados Unidos":
                museosArray = MiamiInfo.getMuseos();
                restArray = MiamiInfo.getRestaurantes();
                baresArray = MiamiInfo.getBares();
                monumetosArray = MiamiInfo.getMonumetos();
                break;
            case "Not Found":
                notFound = true;
                break;
            default:
                notFound = true;
                break;
        }
        if(!notFound) {
            multiSelectionSpinnerMuseos.setItems(museosArray);
multiSelectionSpinnerMuseos.setSelection(0);
            multiSelectionSpinnerRestaurantes.setItems(restArray);
multiSelectionSpinnerRestaurantes.setSelection(0);
            multiSelectionSpinnerBares.setItems(baresArray);
multiSelectionSpinnerBares.setSelection(0);
            multiSelectionSpinnerMonumentos.setItems(monumetosArray);
multiSelectionSpinnerMonumentos.setSelection(0);
        }

    }
}