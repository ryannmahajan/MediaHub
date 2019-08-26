package com.ryannm.android.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class ShowRecordingsFragment extends Fragment {
    AppCompatImageButton playButton;
    ImageView imageView;
    VideoView videoView;
    Boolean playing = false;
    MediaPlayer audioPlayer = new MediaPlayer();

    public static final String TAG = "ShowRecordingsFragment";

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, ContainingActivity.class);
        i.putExtra(ContainingActivity.CONTAIN_FRAGMENT, ShowRecordingsFragment.TAG);
        return i;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.show_recordings_fragment, container, false);

        videoView = v.findViewById(R.id.videoView);
        //Setting MediaController and URI, then starting the videoView
        videoView.setVideoURI(Uri.fromFile(new File(Helper.getVideoPath(getActivity()))));
        videoView.requestFocus();
        videoView.seekTo(1);

        try {
            audioPlayer.setDataSource(getActivity(), Uri.fromFile(new File(Helper.getAudioPath(getActivity()))));
            audioPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playButton = v.findViewById(R.id.play_button);
        playButton.setOnClickListener( x -> playPause());

        imageView = v.findViewById(R.id.image_display);
        imageView.setImageURI(Uri.fromFile(new File(Helper.getImagePath(getActivity()))));

        return v;
    }

    private void playPause() {

        if (playing) {
            videoView.pause();
            audioPlayer.pause();
            playButton.setImageResource(android.R.drawable.ic_media_play);
        } else {
            videoView.start();

            audioPlayer.start();

            playButton.setImageResource(android.R.drawable.ic_media_pause);
        }
        playing = !playing;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (audioPlayer != null) {
            audioPlayer.release();
            audioPlayer = null;
        }

    }
}
