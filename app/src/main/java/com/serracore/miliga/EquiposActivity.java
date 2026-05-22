package com.serracore.miliga;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

// Activity sencilla que muestra una lista de equipos
// y permite realizar una acción al pulsar un botón
public class EquiposActivity extends MenuActivity {

    private ListView listEquipos;
    private Button btnAnadirLiga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipos);

        // Referencias a los elementos de la interfaz
        listEquipos = findViewById(R.id.listEquipos);
        btnAnadirLiga = findViewById(R.id.btnAnadirLiga);

        // Array de equipos de ejemplo (datos estáticos)
        String[] equipos = {"Atléticos FC", "Universidad CF", "Tigers FC"};

        // Adaptador que conecta el array de datos con el ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                equipos
        );

        // Asignar el adaptador al ListView para mostrar los equipos
        listEquipos.setAdapter(adapter);

        // Evento al pulsar el botón
        btnAnadirLiga.setOnClickListener(v ->
                Toast.makeText(this, "Añadir Liga pulsado", Toast.LENGTH_SHORT).show());
    }
}