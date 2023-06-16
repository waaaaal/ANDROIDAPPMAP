package com.example.buenaporfin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MapActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int UPDATE_REGISTER = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView imageView;
    private EditText editTextText3;
    private EditText editTextText4;
    private EditText editTextText2;
    private EditText editTextTextcurso;
    private RadioGroup radioGroup;

    private Handler handler;
    private Bitmap imageBitmap;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //comprobación de permido de la camara
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
        /*
        Esta sección verifica si la aplicación tiene el permiso de la cámara. Si el permiso no está
         concedido, se solicita al usuario que lo conceda. ContextCompat.checkSelfPermission() se
         utiliza para comprobar el estado del permiso, y ActivityCompat.requestPermissions() se
         utiliza para solicitar el permiso en caso de que no esté concedido.
         */

        //Configura la vista de la actividad
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        //Configuración de la barra de herramientas:
       // MaterialToolbar toolbar = findViewById(R.id.materialToolbar);
       // setSupportActionBar(toolbar);
        /*
        Se encuentra la barra de herramientas definida en el archivo de diseño y se configura como
        la barra de herramientas de la actividad utilizando setSupportActionBar().
         */
        imageView = findViewById(R.id.imageView);
        editTextText3 = findViewById(R.id.editTextTextfecha);
        editTextText4 = findViewById(R.id.editTexthora);
        editTextText2 = findViewById(R.id.editTextText2);
        editTextTextcurso = findViewById(R.id.editTextTextcurso);
        radioGroup = findViewById(R.id.RadioGroup);

        //Configuración del clic en la imagen:
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
/*
});
Se configura un escuchador de clics en la imagen. Cuando se hace clic en la imagen,
se llama al método dispatchTakePictureIntent().
 */

        //Configuración del clic en editTextText3 (fecha):
        editTextText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        /*
        e configura un escuchador de clics en editTextText3 (un campo de texto para la fecha).
        Cuando se hace clic en este campo de texto, se llama al método showDatePickerDialog().
         */

        //Configuración del clic en editTextText4 (hora):
        editTextText4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        /*
        Se configura un escuchador de clics en editTextText4 (un campo de texto para la hora).
        Cuando se hace clic en este campo de texto, se llama al método showTimePickerDialog().
         */

        //configuracion del handler
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == UPDATE_REGISTER) {
                    // Obtener los datos enviados desde la pantalla de MapActivity
                    // Código para manejar los datos recibidos y realizar acciones en función de ellos
                    Bundle data = msg.getData();
                    String imageFilePath = data.getString("imageFilePath");
                    String dateTime = data.getString("dateTime");
                    String name = data.getString("name");
                    String curso = data.getString(("curso"));
                    String radioOption = data.getString("radioOption");
                    // Imprimir los datos en el registro de log
                    Log.d("MapActivity", "Image File Path: " + imageFilePath);
                    Log.d("MapActivity", "Date and Time: " + dateTime);
                    Log.d("MapActivity", "Name: " + name);
                    Log.d("MapActivity", "Curso: " + curso);
                    Log.d("MapActivity", "Radio Option: " + radioOption);

                    // Validar si hay campos vacíos
                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(dateTime) || TextUtils.isEmpty(curso) || TextUtils.isEmpty(radioOption)) {
                        String emptyFields = "";
                        if (TextUtils.isEmpty(name)) {
                            emptyFields += "Nombre ";
                        }
                        if (TextUtils.isEmpty(dateTime)) {
                            emptyFields += "Fecha y Hora ";
                        }
                        if (TextUtils.isEmpty(curso)) {
                            emptyFields += "Curso ";
                        }
                        if (TextUtils.isEmpty(radioOption)) {
                            emptyFields += "Opción del Radio Button ";
                        }
                        Toast.makeText(MapActivity.this, "Los siguientes campos están vacíos: " + emptyFields, Toast.LENGTH_SHORT).show();
                    } else {
                        // Separar la fecha y la hora
                        String[] dateTimeParts = dateTime.split(" ");
                        String fecha = dateTimeParts[0];
                        String hora = dateTimeParts[1];

                        // Abrir la pantalla de RegisterActivity y pasar los datos separados
                        Intent intent = new Intent(MapActivity.this, RegisterActivity.class);
                        intent.putExtra("imageFilePath", imageFilePath);
                        intent.putExtra("fecha", fecha);
                        intent.putExtra("hora", hora);
                        intent.putExtra("name", name);
                        intent.putExtra("curso", curso);
                        intent.putExtra("radioOption", radioOption);
                        startActivity(intent);
                    }
                }
            }
        };
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

    private void dispatchTakePictureIntent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Estás seguro de cambiar la foto?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    /*
                    Si hay una aplicación de cámara disponible, se crea un objeto Intent llamado
                    takePictureIntent con la acción MediaStore.ACTION_IMAGE_CAPTURE. Este intent se
                    utilizará para iniciar la actividad de la cámara y capturar la foto.
                     */
                }
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            /*
            Se crea un objeto DatePickerDialog.OnDateSetListener anónimo. Este objeto se utiliza para
             manejar el evento de selección de fecha cuando el usuario elige una fecha en el diálogo.
             */
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = formatDate(year, month, dayOfMonth);
                editTextText3.setText(selectedDate);
                /*
                Dentro del método onDateSet(), se obtiene la fecha seleccionada a partir de los parámetros
                proporcionados (year, month, dayOfMonth). Luego, se formatea la fecha utilizando el método
                formatDate() (que no se muestra en el código) y se guarda en la variable selectedDate.
                 */
            }
        };

        Calendar calendar = Calendar.getInstance();
        /*
        Se crea un objeto Calendar y se obtiene la instancia actual utilizando Calendar.getInstance().
         Esto se hace para obtener la fecha actual y utilizarla como fecha inicial en el diálogo de selección de fecha
         */
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        /*
        Se crea un objeto DatePickerDialog pasando el contexto (this en este caso), el dateSetListener
        definido anteriormente y las fechas inicialmente seleccionadas (obtenidas del objeto Calendar).
         Esto crea el diálogo de selección de fecha con la configuración proporcionada.
         */
        datePickerDialog.show();
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    public void showTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String selectedTime = formatTime(hourOfDay, minute);
                editTextText4.setText(selectedTime);
            }
        };

        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    private String formatTime(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

/*
maneja la respuesta de la actividad de captura de imágenes. Si la captura de imágenes fue exitosa,
se recupera la imagen capturada del resultado y se muestra en un ImageView.
 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            /*
             Esto se utiliza para determinar si la actividad que se completó con éxito es la de
              de imágenes que se inició previamente.
             */
            Bundle extras = data.getExtras();
            if (extras != null) {
                imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            }
        }
    }

    public void onSaveButtonClick(View view) {
        sendMessageAndUpdateRegister();
    }

    private void sendMessageAndUpdateRegister() {
        if (imageBitmap != null) {
            String imageFileName = "my_image.jpg";

            // Insertar la imagen capturada en la galería usando MediaStore
            MediaStore.Images.Media.insertImage(
                    getContentResolver(),
                    imageBitmap,
                    imageFileName,
                    "Captured Image"
            );

            // Obtener los datos del formulario
            String name = editTextText2.getText().toString();
            String dateTime = editTextText3.getText().toString() + " " + editTextText4.getText().toString();
            String curso = editTextTextcurso.getText().toString();

            RadioGroup radioGroup = findViewById(R.id.RadioGroup);
            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String radioOption = selectedRadioButton.getText().toString();
//waldemr stegier
            // Crear un mensaje para enviar los datos a RegisterActivity
            Message message = handler.obtainMessage(UPDATE_REGISTER);
            //update_register es una constante para identificar el tipo de mensaje
            Bundle data = new Bundle();
            data.putString("name", name);
            data.putString("imageFilePath", imageFileName);
            data.putString("dateTime", dateTime);
            data.putString("curso", curso);
            data.putString("radioOption", radioOption);
            message.setData(data);

            // Registrar los datos en los registros
            handler.sendMessage(message);

            // Mostrar los datos en los registros
            Log.d("MapActivity", "Name: " + name);
            Log.d("MapActivity", "Image File Path: " + imageFileName);
            Log.d("MapActivity", "Date and Time: " + dateTime);
            Log.d("MapActivity", "Curso: " + curso);
            Log.d("MapActivity", "Radio Option: " + radioOption);

        }
    }
}