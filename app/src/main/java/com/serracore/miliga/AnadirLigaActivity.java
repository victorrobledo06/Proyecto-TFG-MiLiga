package com.serracore.miliga;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AnadirLigaActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_liga);

        EditText etNombre = findViewById(R.id.etNombreLiga);
        EditText etDescripcion = findViewById(R.id.etDescripcionLiga);
        EditText etGoleadores = findViewById(R.id.etGoleadores);
        EditText etAsistentes = findViewById(R.id.etAsistentes);
        EditText etResultados = findViewById(R.id.etResultados);
        EditText etEquipos = findViewById(R.id.etEquipos);

        Button btnGuardar = findViewById(R.id.btnGuardarLiga);

        btnGuardar.setOnClickListener(v -> {

            String nombre = etNombre.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();
            String goleadores = etGoleadores.getText().toString().trim();
            String asistentes = etAsistentes.getText().toString().trim();
            String resultados = etResultados.getText().toString().trim();
            String equipos = etEquipos.getText().toString().trim();

            if (nombre.isEmpty()) {
                etNombre.setError("Introduce un nombre");
                return;
            }

            SharedPreferences prefs = getSharedPreferences("LigasDB", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            // Guardamos todos los campos de la liga
            editor.putString("liga_" + nombre + "_descripcion", descripcion);
            editor.putString("liga_" + nombre + "_goleadores", goleadores);
            editor.putString("liga_" + nombre + "_asistentes", asistentes);
            editor.putString("liga_" + nombre + "_resultados", resultados);
            editor.putString("liga_" + nombre + "_equipos", equipos);

            // Guardamos un flag para saber que esta liga existe
            editor.putBoolean("liga_" + nombre + "_existe", true);

            editor.apply();

            Toast.makeText(this, "Liga guardada correctamente", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
