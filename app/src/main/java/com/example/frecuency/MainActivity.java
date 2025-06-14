package com.example.frecuency;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    SharedPreferences archivo;
    EditText edt_nombreArt, edt_nombreReal, edt_apep, edt_apem, edt_numT, edt_borndate,
             edt_ciudad, edt_horaInicio, edt_horaFinal;
    Spinner spinner_city;

    Button playlist, albums, artistas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Asignar ID a cada componente
        edt_nombreArt = findViewById(R.id.edt_nombre_art);
        edt_nombreReal = findViewById(R.id.edt_nombre_real);
        edt_apep = findViewById(R.id.edt_apep);
        edt_apem = findViewById(R.id.edt_apem);
        edt_numT = findViewById(R.id.edt_tel);
        edt_borndate = findViewById(R.id.edt_borndate);
        spinner_city = findViewById(R.id.spinner_city);
        edt_horaInicio = findViewById(R.id.edt_horaInicio);
        edt_horaFinal = findViewById(R.id.edt_horaFinal);

        // Para Spinner
        String ciudades[] = {"Moscú", "Berlín", "Guanajato", "GDL"};
        ArrayAdapter<String> adaptador1 = new ArrayAdapter<String>(this,
                R.layout.spinner1_cities,
                ciudades);
        spinner_city.setAdapter(adaptador1);

        // Para toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ClickListener para fecha de nacimiento
        edt_borndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker_borndate();
            }
        });
        // ClickListener para hora inicio
        edt_horaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker_horaInicio();
            }
        });
        // ClickListener para hora final
        edt_horaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker_horaFinal();
            }
        });
    }

    private void picker_horaFinal() {
    }

    private void picker_horaInicio() {
        
    }

    private void picker_borndate() {

        int d, m, y;    // day, month, year
        Calendar calendar = Calendar.getInstance();
        d = calendar.get(Calendar.DAY_OF_MONTH);
        m = calendar.get(Calendar.MONTH);
        y = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Suma uno a día porque inicia desde cero
                String cadena = "" + (dayOfMonth + 1) + "/" +
                        month + "/" + year;
                edt_borndate.setText(cadena);
            }
        }, y, m, d);
        datePickerDialog.show();    // Mostrar datePickerD
    }

    // Inflar options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Opciones del menu (navega entre activities o logout)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.opc_principal){

            Toast.makeText(this, "Ya se encuentra aquí.", Toast.LENGTH_SHORT).show();
        } else if(item.getItemId() == R.id.opc_ver){

            /*
            Intent aVer = new Intent(this, Ver.class);
            startActivity(aVer);*/
/*
            Intent aVer = new Intent(this, VerCancion.class);
            startActivity(aVer);
        } else if(item.getItemId() == R.id.opc_modificar) {

            Intent aMod = new Intent(this, Modificar.class);
            startActivity(aMod);

        } else if(item.getItemId() == R.id.opc_eliminar) {

            Intent aElim = new Intent(this, Eliminar.class);
            startActivity(aElim);
*/
        } else if(item.getItemId() == R.id.opc_logout) {

            // Si el usuario existe, lo borra de shared preferences y regres a a inicio
            if(archivo.contains("id_usuario")){
                Intent cerrar = new Intent(this, Inicio.class);
                SharedPreferences.Editor editor = archivo.edit();
                editor.remove("id_usuario");
                editor.apply();
                startActivity(cerrar);
                finish();
            }

        } else if(item.getItemId() == R.id.opc_creadores) {

            Intent aCrea = new Intent(this, Creadores.class);
            startActivity(aCrea);

        } else if(item.getItemId() == R.id.opc_contactos) {

            Intent aCont = new Intent(this, Contactos.class);
            startActivity(aCont);

        } else if (item.getItemId() == R.id.perfil){
           /* Intent perfil = new Intent(this, Perfil.class);
            startActivity(perfil);*/
        }
        return super.onOptionsItemSelected(item);
    }
}