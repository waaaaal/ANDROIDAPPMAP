package com.example.buenaporfin;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class formulario extends AppCompatActivity {
    private ListView listViewCities;
    private ArrayAdapter<String> adapter;
    private List<String> cityDataList;
    private List<Double> latitudeList;
    private List<Double> longitudeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_activity);

        listViewCities = findViewById(R.id.listViewCities);
        cityDataList = new ArrayList<>();
        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();

        // Realizar la solicitud HTTP para obtener los datos del servidor
        new FetchCitiesTask().execute();

        listViewCities.setOnItemClickListener((parent, view, position, id) -> {
            String cityInfo = cityDataList.get(position);
            String[] values = cityInfo.split("\n");
            String cityName = values[0];
            String description = values[1];

            AlertDialog.Builder builder = new AlertDialog.Builder(formulario.this);
            builder.setTitle("Mostrar mapa");
            builder.setMessage("¿Estás seguro de que quieres mostrar el mapa de " + cityName + "?");
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Obtener las coordenadas de la ciudad seleccionada
                    double latitude = latitudeList.get(position);
                    double longitude = longitudeList.get(position);

                    // Crear la URI con las coordenadas
                    String uri = "geo:" + latitude + "," + longitude;

                    // Crear un intent para abrir el mapa de Google
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");

                    // Verificar si hay una aplicación que pueda manejar el intent
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(formulario.this, "No se pudo abrir el mapa de Google", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        });
    }
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

    private class FetchCitiesTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> cityDataList = new ArrayList<>();

            try {
                // Establecer la URL del archivo PHP
                URL url = new URL("http://192.168.122.166/compartida/ciudades-junio.php?nombre=waldemar");

                // Abrir la conexión HTTP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Leer la respuesta del servidor
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Separar los valores de cada línea
                    String[] values = line.split("&");
                    if (values.length >= 4) {
                        // Obtener los valores necesarios para mostrar en la lista
                        String cityName = values[0];
                        double latitude = Double.parseDouble(values[1]);
                        double longitude = Double.parseDouble(values[2]);
                        String description = values[3];

                        // Crear una cadena con el nombre de la ciudad y su descripción en líneas separadas
                        String cityInfo = cityName + "\n" + description;

                        // Agregar la cadena a la lista
                        cityDataList.add(cityInfo);
                        latitudeList.add(latitude);
                        longitudeList.add(longitude);
                    }
                }

                // Cerrar los recursos
                reader.close();
                inputStream.close();
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return cityDataList;
        }

        @Override
        protected void onPostExecute(List<String> cityDataList) {
            if (cityDataList.isEmpty()) {
                Toast.makeText(formulario.this, "No se pudieron obtener los datos de la ciudad", Toast.LENGTH_SHORT).show();
            } else {
                formulario.this.cityDataList = cityDataList;
                // Configurar el adaptador de la lista
                adapter = new ArrayAdapter<>(formulario.this, android.R.layout.simple_list_item_1, cityDataList);
                listViewCities.setAdapter(adapter);
            }
        }
    }
}