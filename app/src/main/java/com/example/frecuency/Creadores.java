package com.example.frecuency;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Creadores extends AppCompatActivity {
    Toolbar toolbar;
    SharedPreferences archivo;
    int id_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_creadores);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Shared Preferences
        archivo = this.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        // Recupera el ID del usuario (default user normal)
        id_user = archivo.getInt("id_usuario", 6);

        // Asigna id a toolbar y activa
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        archivo = this.getSharedPreferences("sesion", Context.MODE_PRIVATE);
    }

    // Inflar options menuAdd commentMore actions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // SI es user admin carga el menu completo
        if (id_user < 6) {
            getMenuInflater().inflate(R.menu.menu, menu);
        } else {
            // SI no carga el menu limitado
            getMenuInflater().inflate(R.menu.menu_limited, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    // Opciones del menu (navega entre activities o logout)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {        // Si es usuario normal (ID > 5) no muestra principal, modificar ni eliminar

        if(id_user < 6) {
            if (item.getItemId() == R.id.opc_principal) {
                Intent aMain = new Intent(this, MainActivity.class);
                startActivity(aMain);

            } else if (item.getItemId() == R.id.opc_modificar) {

                Intent aMod = new Intent(this, Modificar.class);
                startActivity(aMod);

            } else if (item.getItemId() == R.id.opc_eliminar) {
                Intent aElim = new Intent(this, Eliminar.class);
                startActivity(aElim);

            }
        }
        if(item.getItemId() == R.id.opc_ver){

            Intent aVer = new Intent(this, Ver.class);
            startActivity(aVer);

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

            Toast.makeText(this, "Ya se encuentra aquí.", Toast.LENGTH_SHORT).show();

        } else if(item.getItemId() == R.id.opc_contactos) {

            Intent aCont = new Intent(this, Contactos.class);
            startActivity(aCont);

        }
        finish();
        return super.onOptionsItemSelected(item);
    }
}