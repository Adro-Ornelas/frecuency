package com.example.frecuency;

import static android.text.TextUtils.isEmpty;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Global.Listas;
import POJO.Artista;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    SharedPreferences archivo;
    EditText edt_nombreArt, edt_nombreReal, edt_apep, edt_apem, edt_numT, edt_borndate,
             edt_horaInicio, edt_horaFinal;
    Spinner spinner_city;
    Button btn_crearArt;

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
        btn_crearArt = findViewById(R.id.btn_crearArtista);

        // Shared Preferences
        archivo = this.getSharedPreferences("sesion", Context.MODE_PRIVATE);

        // Para Spinner
        String ciudades[] = {"Selecciona una ciudad",
                "Moscú", "Berlín", "Guanajato", "GDL"};
        ArrayAdapter<String> adaptador1 = new ArrayAdapter<String>(this,
                R.layout.spinner1_cities,
                ciudades);
        spinner_city.setAdapter(adaptador1);

        // Para toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Llena ArrayList de artistas
        recuperarArtistas();

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
        // ClickListener para evento crear (botón)
        btn_crearArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evento_crear();
            }
        });
    }

    private void evento_crear() {

        // Primero recupera todos los campos
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

        // Si alguno de los campos está vacío informa al usuario
        if (isEmpty(nomArt) || isEmpty(nomReal) || isEmpty(tel) || isEmpty(nacim) ||
                ciudad.equals("Selecciona una ciudad") || isEmpty(hInicio) ||
                isEmpty(hFinal) || isEmpty(apep) || isEmpty(apem)) {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();

            // Si el número de teléfono no es de diez dígitos, no continúa
        } else if (tel.length() != 10) {
            Toast.makeText(this, "Número telefónico de 10 dígitos", Toast.LENGTH_SHORT).show();

        } else{
            // Si todo campo estaba lleno, procede a crear
            // Llena de información nuevo objeto
            Artista newArtist = new Artista();
            newArtist.setNombreArt(nomArt);
            newArtist.setNombreReal(nomReal);
            newArtist.setApellidoP(apep);
            newArtist.setApellidoM(apem);
            newArtist.setTelefonoCont(tel);
            newArtist.setFechaNacimiento(nacim);
            newArtist.setCiudadShow(ciudad);
            newArtist.setHoraInicio(hInicio);
            newArtist.setHoraFinal(hFinal);

            // Añadir a la lista de artistas
            Listas.listaArtistas.add(newArtist);

            // Guarda en base de datos
            String url = getResources().getString(R.string.base_url) + "insertar_artista.php";
            StringRequest insertarArt = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("NO paso", response);
                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.d("NO paso", Objects.requireNonNull(error.getMessage()));
                    Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            })
            {
                @NonNull
                @Override
                protected Map<String, String> getParams() throws AuthFailureError{
                    Map<String, String> x = new HashMap<>();
                    x.put("nombre_art",  newArtist.getNombreArt());
                    x.put("nombre_real", newArtist.getNombreReal());
                    x.put("apep", newArtist.getApellidoP());
                    x.put("apem",  newArtist.getApellidoM());
                    x.put("tel", newArtist.getTelefonoCont());
                    x.put("fecha_nac", newArtist.getFechaNacimiento());
                    x.put("ciudad_show", newArtist.getCiudadShow());
                    x.put("hora_inicio", newArtist.getHoraInicio());
                    x.put("hora_final",  newArtist.getHoraFinal());

                    return x;

                }
            };
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(insertarArt);
            Toast.makeText(MainActivity.this, "Registro ingresado", Toast.LENGTH_SHORT).show();
        }

    }

    private void picker_horaInicio() {

        int h, m;   // hour, minute
        Calendar calendar = Calendar.getInstance();
        h = calendar.get(Calendar.HOUR_OF_DAY);
        m = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String cadena = "" + hourOfDay + ":" +
                        minute;
                edt_horaInicio.setText(cadena);
            }
        }, h, m, true); // Establece parámetros y formato 24 horas true
        timePickerDialog.show();    // Mostrar TimePickerD
    }
    private void picker_horaFinal() {

        int h, m;   // hour, minute
        Calendar calendar = Calendar.getInstance();
        h = calendar.get(Calendar.HOUR_OF_DAY);
        m = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String cadena = "" + hourOfDay + ":" +
                        minute;
                edt_horaFinal.setText(cadena);
            }
        }, h, m, true); // Establece parámetros y formato 24 horas true
        timePickerDialog.show();    // Mostrar TimePickerD
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
                // Suma uno a mes porque inicia desde cero
                String cadena = year +  "-" + (month + 1) + "-"  + dayOfMonth;
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


            Intent aVer = new Intent(this, Ver.class);
            startActivity(aVer);

        } else if(item.getItemId() == R.id.opc_modificar) {

            //Intent aMod = new Intent(this, Modificar.class);
           // startActivity(aMod);

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

        } else if (item.getItemId() == R.id.perfil){
           /* Intent perfil = new Intent(this, Perfil.class);
            startActivity(perfil);*/
        }
        return super.onOptionsItemSelected(item);
    }

    private void recuperarArtistas() {
        // Si el arrayList está vacío, lo llena de lo recuperado de la base de datos
        if(Listas.listaArtistas.isEmpty()){

            String url = getResources().getString(R.string.base_url)
                        + "recuperar_artistas.php";

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {

                                Listas.listaArtistas.clear();

                                // Itera por cada JSON Object, hace objeto nuevo y guarda en lista
                                for (int i = 0; i < response.length(); ++i) {

                                    JSONObject artista = response.getJSONObject(i);

                                    // Llena objeto
                                    Artista newArtist = new Artista();
                                    newArtist.setNombreArt(artista.getString("nombre_art"));
                                    newArtist.setNombreReal(artista.getString("nombre_real"));
                                    newArtist.setApellidoP(artista.getString("apep"));
                                    newArtist.setApellidoM(artista.getString("apem"));
                                    newArtist.setTelefonoCont(artista.getString("tel"));
                                    newArtist.setFechaNacimiento(artista.getString("fecha_nac"));
                                    newArtist.setCiudadShow(artista.getString("ciudad_show"));
                                    newArtist.setHoraInicio(artista.getString("hora_inicio"));
                                    newArtist.setHoraFinal(artista.getString("hora_final"));

                                    // Agrega objeto a lista
                                    Listas.listaArtistas.add(newArtist);
                                }

                            } catch (JSONException e) {
                                Log.d("E: ", e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "E:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }

    }
}