package com.example.frecuency;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import Global.Listas;
import POJO.Artista;

public class Modificar extends AppCompatActivity {
    Toolbar toolbar;
    EditText edt_nombreArt, edt_nombreReal, edt_apep, edt_apem, edt_numT, edt_borndate,
            edt_horaInicio, edt_horaFinal;
    Spinner spinner_city;
    Button btn_actualizar;
    ImageButton btn_anterior, btn_siguiente;
    SharedPreferences archivo;
    int pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar);
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
        btn_actualizar = findViewById(R.id.actualizar);
        btn_anterior = findViewById(R.id.anterior);
        btn_siguiente = findViewById(R.id.siguiente);

        // Para Spinner
        ArrayAdapter<CharSequence> adaptador1 = ArrayAdapter.createFromResource(
                this,
                R.array.ciudades_array,
                R.layout.spinner1_cities
        );
        adaptador1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(adaptador1);

        // Shared Preferences
        archivo = this.getSharedPreferences("sesion", Context.MODE_PRIVATE);

        // Para toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        primerDato();

        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });
        
        btn_anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anterior();
            }
        });
        
        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siguiente();
            }
        });

    }

    private void primerDato() {
        pos = 0;
        String ciudad = Listas.listaArtistas.get(pos).getCiudadShow();
        edt_nombreArt.setText(Listas.listaArtistas.get(pos).getNombreArt());
        edt_nombreReal.setText(Listas.listaArtistas.get(pos).getNombreReal());
        edt_apep.setText(Listas.listaArtistas.get(pos).getApellidoP());
        edt_apem.setText(Listas.listaArtistas.get(pos).getApellidoM());
        edt_numT.setText(Listas.listaArtistas.get(pos).getTelefonoCont());
        edt_borndate.setText(Listas.listaArtistas.get(pos).getFechaNacimiento());
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner_city.getAdapter();
        int index = adapter.getPosition(ciudad);
        // Selecciona "Selecciona una ciudad" si no la encuentra
        spinner_city.setSelection(Math.max(index, 0));
        edt_horaInicio.setText(Listas.listaArtistas.get(pos).getHoraInicio());
        edt_horaFinal.setText(Listas.listaArtistas.get(pos).getHoraFinal());
    }

    private void siguiente() {
        //Llego al fina
        if(pos == Listas.listaArtistas.size()-1){
            pos = 0;
        }else {
            pos++;
        }
        String ciudad = Listas.listaArtistas.get(pos).getCiudadShow();
        edt_nombreArt.setText(Listas.listaArtistas.get(pos).getNombreArt());
        edt_nombreReal.setText(Listas.listaArtistas.get(pos).getNombreReal());
        edt_apep.setText(Listas.listaArtistas.get(pos).getApellidoP());
        edt_apem.setText(Listas.listaArtistas.get(pos).getApellidoM());
        edt_numT.setText(Listas.listaArtistas.get(pos).getTelefonoCont());
        edt_borndate.setText(Listas.listaArtistas.get(pos).getFechaNacimiento());
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner_city.getAdapter();
        int index = adapter.getPosition(ciudad);
        // Selecciona "Selecciona una ciudad" si no la encuentra
        spinner_city.setSelection(Math.max(index, 0));
        edt_horaInicio.setText(Listas.listaArtistas.get(pos).getHoraInicio());
        edt_horaFinal.setText(Listas.listaArtistas.get(pos).getHoraFinal());
    }

    private void anterior() {
        //Llego al inicio
        if(pos == 0){
            pos = Listas.listaArtistas.size()-1;
        }else {
            pos--;
        }
        String ciudad = Listas.listaArtistas.get(pos).getCiudadShow();
        edt_nombreArt.setText(Listas.listaArtistas.get(pos).getNombreArt());
        edt_nombreReal.setText(Listas.listaArtistas.get(pos).getNombreReal());
        edt_apep.setText(Listas.listaArtistas.get(pos).getApellidoP());
        edt_apem.setText(Listas.listaArtistas.get(pos).getApellidoM());
        edt_numT.setText(Listas.listaArtistas.get(pos).getTelefonoCont());
        edt_borndate.setText(Listas.listaArtistas.get(pos).getFechaNacimiento());
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner_city.getAdapter();
        int index = adapter.getPosition(ciudad);
        // Selecciona "Selecciona una ciudad" si no la encuentra
        spinner_city.setSelection(Math.max(index, 0));
        edt_horaInicio.setText(Listas.listaArtistas.get(pos).getHoraInicio());
        edt_horaFinal.setText(Listas.listaArtistas.get(pos).getHoraFinal());
    }

    private void guardar() {
        String nomArt, nomReal, apep, apem, tel, nacim, ciudad, hInicio, hFinal;

        nomArt = edt_nombreArt.getText().toString();
        nomReal = edt_nombreReal.getText().toString();
        tel = edt_numT.getText().toString();
        nacim = edt_borndate.getText().toString();
        apep = edt_apep.getText().toString();
        apem = edt_apem.getText().toString();
        ciudad = spinner_city.getSelectedItem().toString();
        hInicio = edt_horaInicio.getText().toString();
        hFinal = edt_horaFinal.getText().toString();

        // Llena de información nuevo objeto
        Artista update = new Artista();
        update.setId(Listas.listaArtistas.get(pos).getId());
        update.setNombreArt(nomArt);
        update.setNombreReal(nomReal);
        update.setApellidoP(apep);
        update.setApellidoM(apem);
        update.setTelefonoCont(tel);
        update.setFechaNacimiento(nacim);
        update.setCiudadShow(ciudad);
        update.setHoraInicio(hInicio);
        update.setHoraFinal(hFinal);

        Listas.listaArtistas.set(pos, update);
        //Guarda en base de datos
        String url = getResources().getString(R.string.base_url) + "actualizar_artista.php";
        StringRequest insertarArt = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("NO paso", response);
                        Toast.makeText(Modificar.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("NO paso", Objects.requireNonNull(error.getMessage()));
                Toast.makeText(Modificar.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        })
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> x = new HashMap<>();
                x.put("id", String.valueOf(update.getId()));
                x.put("nombre_art",  update.getNombreArt());
                x.put("nombre_real", update.getNombreReal());
                x.put("apep", update.getApellidoP());
                x.put("apem",  update.getApellidoM());
                x.put("tel", update.getTelefonoCont());
                x.put("fecha_nac", update.getFechaNacimiento());
                x.put("ciudad_show", update.getCiudadShow());
                x.put("hora_inicio", update.getHoraInicio());
                x.put("hora_final",  update.getHoraFinal());
                return x;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(insertarArt);
        Toast toast = Toast.makeText(this /* MyActivity */, "Datos modificados exitosamente.", Toast.LENGTH_SHORT);
        toast.show();
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

            Intent aMain = new Intent(this, MainActivity.class);
            startActivity(aMain);

        } else if(item.getItemId() == R.id.opc_ver){


            Intent aVer = new Intent(this, Ver.class);
            startActivity(aVer);

        } else if(item.getItemId() == R.id.opc_modificar) {

            Toast.makeText(this, "Ya se encuentra aquí.", Toast.LENGTH_SHORT).show();

        } else if(item.getItemId() == R.id.opc_eliminar) {

            Intent aElim = new Intent(this, Eliminar.class);
            startActivity(aElim);

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

        }
        return super.onOptionsItemSelected(item);
    }
}