package com.example.eag.grabadora_audio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageButton btn_recorder;
    private MediaRecorder grabacion;
    private String archivoSalida = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_recorder = (ImageButton) findViewById(R.id.grabar);

        //Pedimos permisos para escribir en el dispositivo y grabar con el micrófono

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
            },1000);
        }
    }

    public void recorder(View view){
        if(grabacion == null){ //Si está nulo no se está grabando nada

            //Obtenemos la ruta donde grabar el audio

            archivoSalida = Environment.getExternalStorageDirectory().getAbsolutePath() + "/grabacion.mp3";

            //Creamos un objeto de grabación

            grabacion = new MediaRecorder();

            //Indicamos la fuente de audio
            grabacion.setAudioSource(MediaRecorder.AudioSource.MIC);

            //Especificamos el formato de salida del audio

            grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            //Codificamos el audio

            grabacion.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

            //Indicamos el archivo de salida

            grabacion.setOutputFile(archivoSalida);

            try {
                grabacion.prepare();
                grabacion.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Cambiamos el botón
            btn_recorder.setBackgroundResource(R.drawable.rec);

            Toast.makeText(this, "Grabando...", Toast.LENGTH_SHORT).show();


        }else if(grabacion != null){ //Está grabando
            //Detenemos la grabación
            grabacion.stop();
            //Ponemos la grabación en estado de finalizado
            grabacion.release();
            //Ponemos la grabación a null
            grabacion = null;
            //Cambiamos el botón de grabación
            btn_recorder.setBackgroundResource(R.drawable.stop_rec);
        }

    }

    public void play(View view){
        //Objeto MediaPlayer
        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            //Especificamos donde está nuestro audio guardado
            mediaPlayer.setDataSource(archivoSalida);
            //Preparamos la reprodución del audio
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Comenzamos a reproducir
        mediaPlayer.start();
        Toast.makeText(this, "Reproduciendo...", Toast.LENGTH_SHORT).show();
    }
}
