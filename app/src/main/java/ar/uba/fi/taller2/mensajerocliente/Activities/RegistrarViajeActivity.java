package ar.uba.fi.taller2.mensajerocliente.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import ar.uba.fi.taller2.mensajerocliente.R;
import ar.uba.fi.taller2.mensajerocliente.manejadores.PickerDialog;

public class RegistrarViajeActivity extends ActionBarActivity {


    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_viaje);

        String[] cities = getResources().getStringArray(R.array.countries);
        this.adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, cities);
        this.autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        autoCompleteTextView.setAdapter(this.adapter);

        autoCompleteTextView.setThreshold(1);

    }

    public void setDate(View view){

        PickerDialog pickerDialog = new PickerDialog();
        pickerDialog.show(getFragmentManager(), "date_picker");

    }
}
