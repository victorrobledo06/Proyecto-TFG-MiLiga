package com.serracore.miliga;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();
        if (id==R.id.action_home){
            startActivity(new Intent(this, StartActivity.class));
        }
        if (id==R.id.action_exit){
            mostrarDialogoSalida();
        }

        return super.onOptionsItemSelected(item);
    }


    private void mostrarDialogoSalida() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar aplicación")
                .setMessage("¿Seguro que quieres salir de MiLiga?")
                .setPositiveButton("Salir", (dialog, which) -> finishAffinity())
                .setNegativeButton("Volver", null)
                .show();
    }
}
