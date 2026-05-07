package com.serracore.miliga;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class GoleadoresActivity extends MenuActivity {

    private Button btnAnadirGoleador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goleadores);

        btnAnadirGoleador = findViewById(R.id.btnAnadirGoleador);

        btnAnadirGoleador.setOnClickListener(v ->
                Toast.makeText(this, "Añadir goleador pulsado", Toast.LENGTH_SHORT).show());
    }
}
