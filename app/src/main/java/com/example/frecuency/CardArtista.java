package com.example.frecuency;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Global.Listas;

public class CardArtista extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtv_nombreArt, txtv_nombreReal, txtv_apep, txtv_apem, txtv_tel,
            txtv_fecnac, txtv_ciudadShow, txtv_horaIncio, txtv_horaFin;
    Button btn_llamar;
    SharedPreferences archivo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_card_artista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Asignar ID a cada componente
        txtv_nombreArt = findViewById(R.id.txtv_cnombreArt);
        txtv_nombreReal = findViewById(R.id.txtv_cnombreReal);
        txtv_apep = findViewById(R.id.txtv_cApep);
        txtv_apem = findViewById(R.id.txtv_cApem);
        txtv_tel = findViewById(R.id.txtv_cTel);
        txtv_fecnac = findViewById(R.id.txtv_cFechaNac);
        txtv_ciudadShow = findViewById(R.id.txtv_cCiudadS);
        txtv_horaIncio = findViewById(R.id.txtv_cHoraInicio);
        txtv_horaFin = findViewById(R.id.txtv_cHoraFinal);
        btn_llamar = findViewById(R.id.btn_callArtist);

        int pos = getIntent().getIntExtra("pos", -1);

        txtv_nombreArt.setText(Listas.listaArtistas.get(pos).getNombreArt());
        txtv_nombreReal.setText(Listas.listaArtistas.get(pos).getNombreReal());
        txtv_apep.setText(Listas.listaArtistas.get(pos).getApellidoP());
        txtv_apem.setText(Listas.listaArtistas.get(pos).getApellidoM());
        txtv_tel.setText(Listas.listaArtistas.get(pos).getTelefonoCont());
        txtv_fecnac.setText(Listas.listaArtistas.get(pos).getFechaNacimiento());
        txtv_ciudadShow.setText(Listas.listaArtistas.get(pos).getCiudadShow());
        txtv_horaIncio.setText(Listas.listaArtistas.get(pos).getHoraInicio());
        txtv_horaFin.setText(Listas.listaArtistas.get(pos).getHoraFinal());

        // Shared Preferences
        archivo = this.getSharedPreferences("sesion", Context.MODE_PRIVATE);

        // Para toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamar();
            }
        });
    }

    private void llamar() {
        Intent llamada = new Intent(Intent.ACTION_CALL);
        llamada.setData(Uri.parse("tel:"+txtv_tel.getText().toString()));
        //Verifica si tiene permisos de hacer llamadas
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 10);
            return;
        }
        startActivity(llamada);
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

            Intent aMod = new Intent(this, Modificar.class);
            startActivity(aMod);

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