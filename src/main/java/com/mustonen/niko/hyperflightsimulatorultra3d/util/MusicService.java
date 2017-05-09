package com.mustonen.niko.hyperflightsimulatorultra3d.util;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mustonen.niko.hyperflightsimulatorultra3d.R;

import java.io.Serializable;

public class MusicService extends Service implements Serializable {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    MediaPlayer mediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.main);
            mediaPlayer.start();
        } else if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
    }
}
