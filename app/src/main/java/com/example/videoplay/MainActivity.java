package com.example.videoplay;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.videoplay.databinding.ActivityMainBinding;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.zlylib.fileselectorlib.FileSelector;
import com.zlylib.fileselectorlib.bean.EssFile;
import com.zlylib.fileselectorlib.utils.Const;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.Option;
import tcking.github.com.giraffeplayer2.VideoInfo;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.textInputLayout.getText().toString().equals("")) {
                    VideoInfo videoInfo = new VideoInfo(binding.textInputLayout.getText().toString());
                    play(videoInfo);
                } else {
                    Toast.makeText(MainActivity.this, "请输入播放地址在播放", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileSelector.from(MainActivity.this)
                        .setMaxCount(1) //设置最大选择数
                        .setFileTypes("mp4") //设置文件类型
                        .requestCode(1) //设置返回码
                        .start();
            }
        });
        getPermissions();
    }

    // 获取权限
    void getPermissions() {
        XXPermissions.with(this)
                // 申请单个权限
//                .permission(Permission.RECORD_AUDIO)
                // 申请多个权限
                .permission(Permission.Group.STORAGE)
                // 设置权限请求拦截器（局部设置）
                //.interceptor(new PermissionInterceptor())
                // 设置不触发错误检测机制（局部设置）
                //.unchecked()
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean all) {
//                        if (!all) {
//                            toast("获取部分权限成功，但部分权限未正常授予");
//                            return;
//                        }
//                        toast("获取录音和日历权限成功");
                    }

                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean never) {
//                        if (never) {
//                            toast("被永久拒绝授权，请手动授予录音和日历权限");
//                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
//                            XXPermissions.startPermissionActivity(context, permissions);
//                        } else {
//                            toast("获取录音和日历权限失败");
//                        }
                    }
                });
    }

    void play(VideoInfo videoInfo) {
        videoInfo.addOption(Option.create(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "decryption_key", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        GiraffePlayer.play(getBaseContext(), videoInfo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                ArrayList<EssFile> essFileList = data.getExtras().getParcelableArrayList(Const.EXTRA_RESULT_SELECTION);
                for (EssFile file : essFileList) {
                    VideoInfo videoInfo;// = new VideoInfo(file.getUri() != null?file.getUri():file.getAbsolutePath());
                    if (file.getUri() != null) {
                        videoInfo = new VideoInfo(file.getUri());
                    } else {
                        videoInfo = new VideoInfo(file.getAbsolutePath());
                    }
                    play(videoInfo);
                    return;
                }
            }
        }
    }
}