package com.example.buenaporfin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    //private static final int REQUEST_IMAGE_CAPTURE = 1;
    private TextView anunciosTextView;
    private DownloadAnunciosThread downloadAnunciosThread;



    @SuppressLint("MissingInflatedId")
    /*
    Esta anotación indica que se debe suprimir una advertencia específica relacionada con un ID faltante
     en la inflación de un objeto de la interfaz de usuario. Es
     posible que haya un objeto inflado en el código que no tenga un ID asignado y esta anotación se
      utiliza para evitar la advertencia relacionada con eso.
     */



    /*
    @Override indica que el método onCreate() está anulando el método onCreate() de la clase bas
    e (AppCompatActivity en este caso). Esto es una convención en Java cuando se anulan métodos de una clase base.
     */
    // inicializa la acticvidad , establece diseño, busca vistas y crea un hilo separado para descargar textos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        es el método que se llama cuando se crea
        la actividad. Recibe un parámetro savedInstanceState que es un objeto Bundle que contiene el
        estado previamente guardado de la actividad.
         */
        super.onCreate(savedInstanceState);
        /*
        super.onCreate(savedInstanceState) llama al método onCreate() de la clase base (AppCompatActivity).
        Es importante llamar a este método para que se realicen las tareas de inicialización básicas de la actividad.
         */
        setContentView(R.layout.activity_main);
        /*
        setContentView(R.layout.activity_main) establece el diseño de la actividad, que se define en
         el archivo XML activity_main.xml. Esto infla el diseño y lo muestra en la pantalla.
         */



        anunciosTextView = findViewById(R.id.textView3);
        /*
        anunciosTextView = findViewById(R.id.textView3) busca la vista con el ID textView3 en el
        diseño inflado y la asigna a la variable anunciosTextView. Esta vista es un TextView utilizado
         para mostrar los anuncios descargados.
         */
        imageView = findViewById(R.id.imageView);
        /*
        imageView = findViewById(R.id.imageView) busca la vista con el ID imageView en el diseño inflado
         y la asigna a la variable imageView. Esta vista es un ImageView utilizado para mostrar una imagen en la actividad.
         */

        // Iniciar el hilo de descarga de anuncios
        downloadAnunciosThread = new DownloadAnunciosThread();
        /*
        downloadAnunciosThread = new DownloadAnunciosThread() crea una instancia del objeto DownloadAnunciosThread,
         que es una clase interna definida en la misma clase que contiene este método onCreate(). Este objeto
         representa un hilo separado que se utilizará para descargar anuncios.
         */
        downloadAnunciosThread.start();
        /*
        downloadAnunciosThread.start() inicia la ejecución del hilo de descarga de anuncios llamando
         al método start(). Esto ejecutará el método run() del hilo de descarga de anuncios en un hilo
          separado, lo que permite realizar la descarga en segundo plano sin bloquear el hilo principal de la interfaz de usuario.
         */





    }
    /*
    Este código corresponde a un método llamado openaboutActivity que se invoca cuando se selecciona
    un elemento del menú (representado por MenuItem) en la actividad actual.
     */
    public void openaboutActivity(MenuItem item) {
        Intent intent = new Intent(this, CiudadesActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        /*
        Se utiliza el flag FLAG_ACTIVITY_REORDER_TO_FRONT en el intent, lo cual indica que si la actividad
         CiudadesActivity ya está en la pila de actividades, se mueva al frente en lugar de crear una nueva instancia.
         */
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Detener el hilo de descarga de anuncios si la actividad se destruye
        if (downloadAnunciosThread != null) {
            downloadAnunciosThread.interrupt();
        }
    }

    //Clase que permite crear un hilo a parte para la descarga.
    private class DownloadAnunciosThread extends Thread {
        @Override
        public void run() {
            try {
                // Crear una URL para el archivo de anuncios
                URL url = new URL("http://192.168.122.166/compartida/anuncios.php");

                // Abrir una conexión HTTP con la URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Obtener el flujo de entrada de la conexión para leer los datos
                InputStream inputStream = connection.getInputStream();

                // Crear un lector de buffer para leer los datos del flujo de entrada
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                // Variables para almacenar cada línea de datos y finalLine
                String line;

                // Leer cada línea de datos del flujo de entrada
                while ((line = reader.readLine()) != null) {
                    // Almacenar la línea actual en finalLine (se utiliza final para ser accesible dentro de la clase interna anónima que hay a continuación)
                    final String finalLine = line;

                    // Actualizar el TextView en el hilo principal (UI thread) para mostrar la línea actual
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Agregar la línea actual al contenido existente del TextView, junto con un salto de línea
                            anunciosTextView.setText(anunciosTextView.getText() + finalLine + "\n");
                        }
                    });

                    // Pausar el hilo durante 2 segundos (2000 milisegundos)
                    Thread.sleep(2000);
                }

                // Cerrar el lector de buffer y desconectar la conexión
                reader.close();
                connection.disconnect();
            } catch (Exception e) {
                // Manejar cualquier excepción que ocurra durante la descarga de anuncios y registrarla en el registro de errores (Log)
                Log.e("MainActivity", "Error al descargar anuncios", e);
            }
        }
    }

}