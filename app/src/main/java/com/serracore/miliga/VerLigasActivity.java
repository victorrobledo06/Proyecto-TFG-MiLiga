package com.serracore.miliga;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

// Activity que muestra ligas almacenadas localmente mediante SharedPreferences.
// Esta implementación es independiente de Firebase y se basa en almacenamiento local.
public class VerLigasActivity extends MenuActivity {

    //"Se han utilizado dos formas de almacenamiento, una local mediante
    // SharedPreferences para pruebas iniciales y otra remota con Firebase
    // para la implementación final del sistema."

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ligas);

        // Referencia al ListView
        ListView listView = findViewById(R.id.listLigas);

        // Obtener acceso a SharedPreferences con el nombre "LigasDB"
        SharedPreferences prefs = getSharedPreferences("LigasDB", MODE_PRIVATE);

        // Obtener todos los datos almacenados
        Map<String, ?> todas = prefs.getAll();

        // Lista para almacenar solo los nombres de las ligas
        ArrayList<String> nombres = new ArrayList<>();

        // Recorrer todos los pares clave-valor
        for (String key : todas.keySet()) {

            // Filtrar solo las claves que empiezan por "liga_"
            if (key.startsWith("liga_")) {

                // Extraer el nombre eliminando el prefijo "liga_"
                nombres.add(key.replace("liga_", ""));
            }
        }

        // Crear adaptador para mostrar los nombres en el ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                nombres
        );

        // Asignar el adaptador al ListView
        listView.setAdapter(adapter);
    }
}