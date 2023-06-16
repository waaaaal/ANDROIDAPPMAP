package com.example.buenaporfin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private TextView textViewName;
    private TextView textViewCurso;
    private TextView textViewFecha;
    private TextView textViewHora;
    private TextView textViewRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        // Obtener los datos enviados desde MapActivity
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String curso = intent.getStringExtra("curso");
        String fecha = intent.getStringExtra("fecha");
        String hora = intent.getStringExtra("hora");
        String radioOption = intent.getStringExtra("radioOption");

        // Vincular vistas
        textViewName = findViewById(R.id.textViewName);
        textViewCurso = findViewById(R.id.textViewCurso);
        textViewFecha = findViewById(R.id.textViewFecha);
        textViewHora = findViewById(R.id.textViewHora);
        textViewRadio = findViewById(R.id.textViewRadio);

        // Mostrar los datos en las vistas correspondientes
        textViewName.setText("Nombre: " + name );
        textViewCurso.setText("Hace el examen del Curso: " + curso);
        textViewFecha.setText("El día: " + fecha);
        textViewHora.setText("A las: " + hora);
        textViewRadio.setText("Considera que se merece " + radioOption);
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

         if (id == R.id.action_search) {
            // Acción para el ítem "Search"
            return true;
        } else if (id == R.id.action_settings) {
            // Acción para el ítem "Settings"
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
    public void openaboutActivity(MenuItem item) {
        Intent intent = new Intent(this, CiudadesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void openSettingsActivity(MenuItem item) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void openInicioActivity(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void openRegisterActivity(MenuItem item) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void formularioitem(MenuItem item) {
        Intent intent2 = new Intent(this, formulario.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent2);
    }
}