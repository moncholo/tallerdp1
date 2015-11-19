package ar.uba.fi.taller2.mensajerocliente.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ar.uba.fi.taller2.mensajerocliente.InfoCiudades.MiamiInfo;
import ar.uba.fi.taller2.mensajerocliente.InfoCiudades.NuevaYorkInfo;
import ar.uba.fi.taller2.mensajerocliente.InfoCiudades.ParisInfo;
import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.utilidades.MultiSelectionSpinner;

public class PreferenciasViajeActivity extends AppCompatActivity {
    private MultiSelectionSpinner multiSelectionSpinnerMuseos, multiSelectionSpinnerRestaurantes, multiSelectionSpinnerBares
            ,multiSelectionSpinnerMonumentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias_viaje);

        multiSelectionSpinnerMuseos = (MultiSelectionSpinner) findViewById(R.id.spinner_museos);
        multiSelectionSpinnerRestaurantes = (MultiSelectionSpinner) findViewById(R.id.spinner_restaurantes);
        multiSelectionSpinnerBares = (MultiSelectionSpinner) findViewById(R.id.spinner_bares);
        multiSelectionSpinnerMonumentos = (MultiSelectionSpinner) findViewById(R.id.spinner_monumetos);

        Intent intent = getIntent();
        String city = intent.getStringExtra(RegistrarViajeActivity.CITY);


        this.determinarCiudad(city);


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
            //multiSelectionSpinnerMuseos.setSelection(0);

            multiSelectionSpinnerRestaurantes.setItems(restArray);
            //multiSelectionSpinnerRestaurantes.setSelection(0);

            multiSelectionSpinnerBares.setItems(baresArray);
            //multiSelectionSpinnerBares.setSelection(0);

            multiSelectionSpinnerMonumentos.setItems(monumetosArray);
            //multiSelectionSpinnerMonumentos.setSelection(0);
        }

    }
}
