package com.serracore.miliga;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

// Activity base de la aplicación.
// Todas las demás activities heredan de esta,
// lo que permite tener un menú común en toda la app.
public class MenuActivity extends AppCompatActivity {

    // Método que se ejecuta al crear el menú superior (barra de opciones)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflar el menú desde el archivo XML (menu_main)
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    // Método que gestiona qué ocurre cuando el usuario pulsa una opción del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Si se pulsa la opción "Home"
        if (id == R.id.action_home) {

            // Volver al menú principal
            startActivity(new Intent(this, MainMenuActivity.class));
        }

        // Si se pulsa la opción "Salir"
        if (id == R.id.action_exit) {

            // Mostrar diálogo de confirmación
            mostrarDialogoSalida();
        }

        return super.onOptionsItemSelected(item);
    }

    // Método que muestra un diálogo para confirmar salida de la app
    private void mostrarDialogoSalida() {

        new AlertDialog.Builder(this)
                .setTitle("Cerrar aplicación")
                .setMessage("¿Seguro que quieres salir de MiLiga?")
                .setPositiveButton("Salir", (dialog, which) ->
                        // Cierra todas las activities y la aplicación
                        finishAffinity())
                .setNegativeButton("Volver", null)
                .show();
    }
}