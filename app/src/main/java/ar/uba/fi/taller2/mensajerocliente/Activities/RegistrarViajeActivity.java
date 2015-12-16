package ar.uba.fi.taller2.mensajerocliente.Activities;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.APIConstantes;

public class RegistrarViajeActivity extends ActionBarActivity {
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;
    private CalendarView calendarioIda, calendarioVuelta;
    public Button bLlegada, bSalida, bFinalizar;
    public final static String CITY = "Ciudad";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_viaje);
        String[] cities = getResources().getStringArray(R.array.countries);
        this.adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, cities);
        this.autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        autoCompleteTextView.setAdapter(this.adapter);
        autoCompleteTextView.setThreshold(1);
        this.bLlegada = (Button) findViewById(R.id.button_llegada);
        this.bSalida = (Button) findViewById(R.id.button_salida);
        this.bFinalizar = (Button) findViewById(R.id.finalizar);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void seleccionarFechaIda(View view){
        calendarioIda = (CalendarView) findViewById(R.id.calendario_ida);
        calendarioIda.setVisibility(View.VISIBLE);
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        final long date = calendarioIda.getDate();
        calendarioIda.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view,
                                            int year, int month, int dayOfMonth) {
                if (view.getDate() != date) {
                    int monthOk = month + 1;
                    bLlegada.setText("Ida: \n" + dayOfMonth + "/" + monthOk + "/" + year);
                    calendarioIda.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void seleccionarFechaVuelta(View view){
        calendarioVuelta = (CalendarView) findViewById(R.id.calendario_vuelta);
        calendarioVuelta.setVisibility(View.VISIBLE);
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        final long date = calendarioVuelta.getDate();
        calendarioVuelta.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view,
                                            int year, int month, int dayOfMonth) {
                if (view.getDate() != date) {
                    int monthOk = month + 1;
                    bSalida.setText("Vuelta: " + dayOfMonth + "/" + monthOk + "/" + year);
                    calendarioVuelta.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    public void finalizar (View view){

        String ciudad = autoCompleteTextView.getText().toString();
        Intent intent = new Intent(this, PreferenciasViajeActivity.class);
        switch (ciudad){
            case "Paris, Francia":
                intent.putExtra(CITY, ciudad);
                break;
            case "Nueva York, Estados Unidos":
                intent.putExtra(CITY, ciudad);
                break;
            case "Miami, Estados Unidos":
                intent.putExtra(CITY, ciudad);
                break;
            default:
                intent.putExtra(CITY, "Not Found");
                break;
        }



        intent.putExtra(APIConstantes.FECHA_IDA, Long.toString(calendarioIda.getDate()));
        intent.putExtra(APIConstantes.FECHA_VUELTA, Long.toString(calendarioVuelta.getDate()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
