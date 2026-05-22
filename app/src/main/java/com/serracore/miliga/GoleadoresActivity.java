package com.serracore.miliga;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

// Activity básica relacionada con la gestión de goleadores.
// Actualmente solo contiene un botón que muestra un mensaje.
// Puede servir como base para futuras mejoras.
public class GoleadoresActivity extends MenuActivity {

    private Button btnAnadirGoleador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goleadores);

        // Referencia al botón de la interfaz
        btnAnadirGoleador = findViewById(R.id.btnAnadirGoleador);

        // Evento al pulsar el botón
        btnAnadirGoleador.setOnClickListener(v ->
                // Muestra un mensaje en pantalla indicando la acción
                Toast.makeText(this, "Añadir goleador pulsado", Toast.LENGTH_SHORT).show());
    }
}