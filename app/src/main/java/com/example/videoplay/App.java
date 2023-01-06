package com.example.videoplay;

import android.app.Application;
import android.content.Context;

import tcking.github.com.giraffeplayer2.MediaController;
import tcking.github.com.giraffeplayer2.PlayerManager;
import tcking.github.com.giraffeplayer2.VideoInfo;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PlayerManager.getInstance().setMediaControllerGenerator(new PlayerManager.MediaControllerGenerator() {
            @Override
            public MediaController create(Context context, VideoInfo videoInfo) {
                return new Controller(context) ;
            }
        });
    }
}
