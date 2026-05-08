package com.serracore.miliga;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MultimediaActivity extends BaseActivity {

    private Button btnPlayAudio, btnPauseAudio, btnStopAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);

        btnPlayAudio = findViewById(R.id.btnPlayAudio);
        btnPauseAudio = findViewById(R.id.btnPauseAudio);
        btnStopAudio = findViewById(R.id.btnStopAudio);

        btnPlayAudio.setOnClickListener(v ->
                Toast.makeText(this, "Play audio", Toast.LENGTH_SHORT).show());

        btnPauseAudio.setOnClickListener(v ->
                Toast.makeText(this, "Pause audio", Toast.LENGTH_SHORT).show());

        btnStopAudio.setOnClickListener(v ->
                Toast.makeText(this, "Stop audio", Toast.LENGTH_SHORT).show());
    }
}
