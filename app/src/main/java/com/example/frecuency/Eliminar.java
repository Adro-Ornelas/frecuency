package com.example.frecuency;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.security.Principal;

import Adaptadores.adaptadorEliminar;
import Global.Listas;
import POJO.Artista;

public class Eliminar extends AppCompatActivity {
    Toolbar toolbar;
    SharedPreferences archivo;
    RecyclerView recyclerView;
    Button btn_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eliminar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Asigna id a toolbar y activa
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Shared pref
        archivo = this.getSharedPreferences("sesion", Context.MODE_PRIVATE);

        // Para recyclerView
        Context context;
        recyclerView = findViewById(R.id.recicladorEliminar);
        adaptadorEliminar miAdp = new adaptadorEliminar();
        miAdp.context = this;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(miAdp);
        
        // Asigna id a button y su clickListener
        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evento_delete();
            }
        });
    }

    private void evento_delete() {

        for(int i = 0; i < Listas.listaBajasArtistas.size(); ++i){

            // Recupera todos los los artista de lista Bajas y elimina
            Artista artista = Listas.listaBajasArtistas.get(i);
            Listas.listaArtistas.remove(artista);

            // Elimina de la base de datos, sólo pasa su id

            String url = getResources().getString(R.string.base_url) +
                   "eliminar_artista.php?id_artista=" +
                    Listas.listaBajasArtistas.get(i).getId();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if(response.getInt("success") == 1)
                            Toast.makeText(Eliminar.this, "1", Toast.LENGTH_SHORT).show();;

                        Toast.makeText(Eliminar.this, response.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("yo", error.getMessage());
                    // edt_passwd.setText(error.getMessage());
                    Toast.makeText(Eliminar.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Pon petición en cola
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        }

        // Limpia lista bahas y notifica a adaptador para actualizar
        Listas.listaBajasArtistas.clear();
        recyclerView.getAdapter().notifyDataSetChanged();
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

            Intent aPrc = new Intent(this, MainActivity.class);
            startActivity(aPrc);

        } else if(item.getItemId() == R.id.opc_ver){

            Intent aVer = new Intent(this, Ver.class);
            startActivity(aVer);

        } else if(item.getItemId() == R.id.opc_modificar) {

            Intent aMod = new Intent(this, Modificar.class);
            startActivity(aMod);

        } else if(item.getItemId() == R.id.opc_eliminar) {

            Toast.makeText(this, "Ya se encuentra aquí.", Toast.LENGTH_SHORT).show();

        } else if(item.getItemId() == R.id.opc_logout) {
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