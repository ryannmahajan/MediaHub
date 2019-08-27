package com.ryannm.android.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 4545;
    MediaPlayer audioPlayer;
    private boolean audioPrepared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        audioPlayer = new MediaPlayer();

        ConstraintLayout videoLayout = findViewById(R.id.videoLayout);
        videoLayout.setOnClickListener(v -> recordVideo());

        ConstraintLayout audioLayout = findViewById(R.id.audioLayout);
        audioLayout.setOnClickListener(v -> recordAudio());

        ConstraintLayout pictureLayout = findViewById(R.id.picLayout);
        pictureLayout.setOnClickListener(v -> clickPicture());

        Button showRecordings = findViewById(R.id.show_recordings);
        showRecordings.setOnClickListener(v -> playAudioOnly());//startActivity(ShowRecordingsFragment.newIntent(this)));
    }

    private void playAudioOnly() {
        if (!audioPrepared) {
            try {
                audioPlayer.setDataSource(this, Uri.fromFile(new File(Helper.getAudioPath(this))));
                audioPlayer.prepare();
                audioPlayer.start();
                audioPrepared = true;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Create a recording first", Toast.LENGTH_SHORT).show();
            }
        } else {
            audioPlayer.start();
        }
    }

    private void recordVideo() {
        if (Helper.hasCamera2API(this)) {
            startActivity(VideoFragment.newIntent(this));
        }
        else if (Helper.phoneHasCamera(this)) {
            // TODO: Display using camera 1.0
        } else {
            Toast.makeText(this, R.string.no_camera_found, Toast.LENGTH_SHORT).show();
        }
    }

    private void recordAudio() {
        String audioRecordPermission = Manifest.permission.RECORD_AUDIO;
        String filePath = Helper.getAudioPath(this);
        if (ActivityCompat.checkSelfPermission(this, audioRecordPermission)==PackageManager.PERMISSION_GRANTED) {
            MediaRecorder recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setOutputFile(filePath);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                recorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            recorder.start();

            new AlertDialog.Builder(this)
                    .setMessage(R.string.listening_now)
                    .setPositiveButton(R.string.save, new AudioDialogButtonListener(recorder, filePath))
                    .setCancelable(false)
                    .setNegativeButton(android.R.string.cancel, new AudioDialogButtonListener(recorder, filePath))
                    .show();
        } else {
            ActivityCompat.requestPermissions(this,new String[] {audioRecordPermission}, REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED) recordAudio();
        }
    }

    private void clickPicture() {
        if (Helper.hasCamera2API(this)) {
            startActivity(PictureFragment.newIntent(this));
        }
        else if (Helper.phoneHasCamera(this)) {
            // TODO: Display using camera 1.0
        } else {
            Toast.makeText(this, R.string.no_camera_found, Toast.LENGTH_SHORT).show();
        }
    }

    private class AudioDialogButtonListener implements DialogInterface.OnClickListener {
        MediaRecorder mediaRecorder;
        String filePath;

        AudioDialogButtonListener(MediaRecorder recorder, String filePath) {
            mediaRecorder = recorder;
            this.filePath = filePath;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            mediaRecorder.stop();
            mediaRecorder.release();
            if (which==AlertDialog.BUTTON_NEGATIVE) {
                File audio = new File(filePath);
                audio.delete();
            } else {
                Toast.makeText(MainActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
