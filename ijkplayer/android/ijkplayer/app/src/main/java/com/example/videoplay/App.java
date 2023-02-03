package com.example.videoplay;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.danikula.videocache.HttpProxyCacheServer;

import java.security.MessageDigest;

import tcking.github.com.giraffeplayer2.MediaController;
import tcking.github.com.giraffeplayer2.PlayerManager;
import tcking.github.com.giraffeplayer2.VideoInfo;

public class App extends Application {
    private String packageName;
    private int flags;

    @Override
    public void onCreate() {
        super.onCreate();
        PlayerManager.getInstance().setMediaControllerGenerator(new PlayerManager.MediaControllerGenerator() {
            @Override
            public MediaController create(Context context, VideoInfo videoInfo) {
                return new Controller(context);
            }
        });
    }

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}
