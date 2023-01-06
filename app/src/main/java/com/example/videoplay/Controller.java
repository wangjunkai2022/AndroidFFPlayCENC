package com.example.videoplay;

import static tv.danmaku.ijk.media.player.IjkMediaPlayer.FFP_PROP_FLOAT_PLAYBACK_RATE;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;

import tcking.github.com.giraffeplayer2.DefaultMediaController;
import tcking.github.com.giraffeplayer2.Option;
import tcking.github.com.giraffeplayer2.VideoInfo;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class Controller extends DefaultMediaController {
    Context context;

    private native void _setPropertyFloat(int property, float value);


    public Controller(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
//        com.github.tcking.giraffeplayer2.R.id.app_video_clarity

        $.id(com.github.tcking.giraffeplayer2.R.id.app_video_clarity).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Controller", "点击了设置按钮");

                //屏幕展示设置
                BasePopupView aspectRatioPopup = new XPopup.Builder(Controller.this.context).asCenterList("选择屏幕展示模式",
                        new String[]{
                                "比例缩小",
                                "比例放大",
                                "视频大小显示",
                                "拉伸视频",
                                "16:9",
                                "4:3",
                        },
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                switch (position) {
                                    case 0:
                                        Controller.this.videoView.getPlayer().aspectRatio(VideoInfo.AR_ASPECT_FIT_PARENT);
                                        break;
                                    case 1:
                                        Controller.this.videoView.getPlayer().aspectRatio(VideoInfo.AR_ASPECT_FILL_PARENT);
                                        break;
                                    case 2:
                                        Controller.this.videoView.getPlayer().aspectRatio(VideoInfo.AR_ASPECT_WRAP_CONTENT);
                                        break;
                                    case 3:
                                        Controller.this.videoView.getPlayer().aspectRatio(VideoInfo.AR_MATCH_PARENT);
                                        break;
                                    case 4:
                                        Controller.this.videoView.getPlayer().aspectRatio(VideoInfo.AR_16_9_FIT_PARENT);
                                        break;
                                    case 5:
                                        Controller.this.videoView.getPlayer().aspectRatio(VideoInfo.AR_4_3_FIT_PARENT);
                                        break;
                                }
                            }
                        });

                BasePopupView playSpeed = new XPopup.Builder(context).asCenterList("选择倍数",
                        new String[]{
                                "0.5",
                                "1",
                                "1.25",
                                "1.5",
                                "1.75",
                                "2",
                                "2.5",
                        },
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                Controller.this.videoView.getPlayer().setSpeed(Float.parseFloat(text));
                            }
                        });

                BasePopupView setting = new XPopup.Builder(context).asCenterList("设置",
                        new String[]{
                                "显示设置",
                                "播放速度设置",
                        }, new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                switch (position) {
                                    case 0:
                                        aspectRatioPopup.show();
                                        break;
                                    case 1:
                                        playSpeed.show();
                                        break;
                                }
                            }
                        });

                setting.show();
            }
        });
    }
}