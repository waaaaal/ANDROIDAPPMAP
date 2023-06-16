package com.example.buenaporfin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CiudadesActivity extends AppCompatActivity {

    private TextView textViewAbout;
    private String aboutText;
    private int currentIndex = 0;
    private Handler handler = new Handler();
    private Button buttonGitHub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciudades);

        textViewAbout = findViewById(R.id.textViewAbout);
        buttonGitHub = findViewById(R.id.buttonGitHub);
        buttonGitHub.setVisibility(View.INVISIBLE);

        aboutText = getString(R.string.about_text);

        buttonGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGitHubPage();
            }
        });

        animateTypingEffect();
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

    private void animateTypingEffect() {
        if (currentIndex < aboutText.length()) {
            /*
            Primero, se verifica si el índice actual (currentIndex) es menor que la longitud del texto
            completo (aboutText). Esto asegura que aún hay caracteres por mostrar en el efecto de escritura.
             */
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textViewAbout.getText());
            /*
            Dentro del bloque if, se crea una instancia de SpannableStringBuilder, que es una clase que permite construir
             y manipular texto con atributos especiales, como el color del texto. Se inicializa con el texto actual del textViewAbout.
             */
            spannableStringBuilder.append(aboutText.charAt(currentIndex));
            /*
            Se agrega un nuevo carácter al SpannableStringBuilder utilizando el método append(),
            tomando el carácter correspondiente al índice actual (aboutText.charAt(currentIndex)).
             */
            SpannableString spannableString = new SpannableString(spannableStringBuilder);
            /*
            A continuación, se crea una instancia de SpannableString a partir del SpannableStringBuilder.
            Esto se hace para aplicar atributos al texto agregado recientemente.
             */
            spannableString.setSpan(new ForegroundColorSpan(textViewAbout.getCurrentTextColor()), 0, spannableString.length(), 0);
            /*
            se establece un atributo de color al SpannableString utilizando setSpan(), que especifica que el color del texto agregado debe ser
             el mismo color que el texto existente en textViewAbout. Esto ayuda a mantener la coherencia visual del texto
             */
            textViewAbout.setText(spannableString);
            /*
            Luego, se establece el SpannableString actualizado como el texto del textViewAbout
            utilizando textViewAbout.setText(spannableString).
             */
            currentIndex++;
            /*
            Incrementa el currentIndex en uno para que el siguiente carácter sea tomado en el próximo ciclo.
             */
            handler.postDelayed(new Runnable() {
                /*
                Finalmente, se utiliza handler.postDelayed() para programar la ejecución del método
                 animateTypingEffect() nuevamente después de un breve retraso de 50 milisegundos.
                 Esto crea un bucle recursivo que continúa mostrando los caracteres uno a uno hasta que se haya mostrado todo el texto.

              Cuando el bucle termina, es decir, cuando currentIndex es igual o mayor que la longitud del texto completo,
              se hace visible un botón llamado buttonGitHub que estaba oculto previamente.
                 */
                @Override
                public void run() {
                    animateTypingEffect();
                }
            }, 50);
        } else {
            buttonGitHub.setVisibility(View.VISIBLE);
        }
    }

    private void openGitHubPage() {
        String gitHubUrl = "https://github.com/TuUsuarioDeGitHub";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(gitHubUrl));
        startActivity(intent);
    }
}