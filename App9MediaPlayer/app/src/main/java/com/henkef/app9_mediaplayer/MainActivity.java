package com.henkef.app9_mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBarVolume;
    private AudioManager audioManager;  //Essa indicação AudioManager gerencia o audio do aparelho

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.teste );
        inicializarSeekBar();

    }

    private void inicializarSeekBar(){

        seekBarVolume = findViewById(R.id.seekBarVolume);

        //configura o audio manager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);  //getSystemService() consegue recuperar serviços do sistema,o comando Context.AUDIO_SERVICE recupera o servido de audio

        //recupera os valores de volume máximo e o volume atual
        int volumeMaximo = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //esse comando recupera o volume máximo da musica que está tocando
        int volumeAtual = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  //esse comando recupera o volume atual da musica que está tocando

        //configurar os valores máximos para o SeekBar
        seekBarVolume.setMax( volumeMaximo );

        //configura o progresso atual do seekBar
        seekBarVolume.setProgress( volumeAtual );

        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i,
                        0);  //irá pegar o (AudioManager.STREAM_MUSIC) volume da musica, transformar no inteiro(i) para poder manipular, e não fará nada (flag=0) ou mostra-rá a alteração diretamente no celular(AudioManager.FLAG_SHOW_UI), mas se quiser pode ser configurada para fazer alguma açõa, aqui tem um site que informa algumas ações (https://developer.android.com/reference/android/media/AudioManager). Porem quando o seekBar for mechido dentro do app o valor inteiro irá mecher e alterar no aparelho também
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void executarSom(View view){
        if (mediaPlayer != null ){
            mediaPlayer.start();
        }
    }

    public void pausarMusica(View view){
        if ( mediaPlayer.isPlaying() ){
            mediaPlayer.pause();
        }
    }

    public void pararMusica(View view){
        if ( mediaPlayer.isPlaying() ){
            mediaPlayer.stop();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.teste);
        }
    }

    //quando a activity for destruida
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( mediaPlayer != null && mediaPlayer.isPlaying() ){  //quando o app for destruido se o media player não estiver nulo e estiver tocando
            mediaPlayer.stop();  //pare a musica
            mediaPlayer.release();  //libere a memoria do celular para não gastar recursos, libera recursos de memoria
            mediaPlayer = null;  //faz com que o mediaPlayer se torne nulo, e não gaste memoria nem recursos do celular
        }
    }

    //comando para quando o usuario sair do app a musica ser pausada e quando retornar ao app poder dar play e continuar normalmente de onde parou, caso vc queira que ao sair do app a musica continue tocando é só remover esse comando
    @Override
    protected void onStop() {
        super.onStop();
        if ( mediaPlayer.isPlaying() ){
            mediaPlayer.pause();
        }
    }


}