package com.example.videoplay;

import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.videoplay.databinding.ActivityMainBinding;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.zlylib.fileselectorlib.FileSelector;
import com.zlylib.fileselectorlib.bean.EssFile;
import com.zlylib.fileselectorlib.utils.Const;

import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tcking.github.com.giraffeplayer2.BaseMediaController;
import tcking.github.com.giraffeplayer2.DefaultPlayerListener;
import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.Option;
import tcking.github.com.giraffeplayer2.PlayerListener;
import tcking.github.com.giraffeplayer2.PlayerManager;
import tcking.github.com.giraffeplayer2.VideoInfo;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    String defKey = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    //获取剪切板内容
    public String getClipContent() {
        ClipboardManager manager = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                String addedTextString = String.valueOf(addedText);
                if (!TextUtils.isEmpty(addedTextString)) {
                    return addedTextString;
                }
            }
        }
        return "";
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation != Configuration.ORIENTATION_LANDSCAPE) {

        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//隐藏软键盘 //
        imm.hideSoftInputFromWindow(binding.textInputLayout.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.textInputLayoutKey.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = binding.textInputLayout.getText().toString();
                if (!key.equals("")) {
                    VideoInfo videoInfo = new VideoInfo(key);
                    videoInfo.setTitle(key);
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

        binding.videoView.setPlayerListener(new PlayerListener() {
            @Override
            public void onPrepared(GiraffePlayer giraffePlayer) {

            }

            @Override
            public void onBufferingUpdate(GiraffePlayer giraffePlayer, int percent) {

            }

            @Override
            public boolean onInfo(GiraffePlayer giraffePlayer, int what, int extra) {
                return false;
            }

            @Override
            public void onCompletion(GiraffePlayer giraffePlayer) {

            }

            @Override
            public void onSeekComplete(GiraffePlayer giraffePlayer) {

            }

            @Override
            public boolean onError(GiraffePlayer giraffePlayer, int what, int extra) {
                Toast.makeText(MainActivity.this, "播放错误。。。", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onPause(GiraffePlayer giraffePlayer) {

            }

            @Override
            public void onRelease(GiraffePlayer giraffePlayer) {

            }

            @Override
            public void onStart(GiraffePlayer giraffePlayer) {

            }

            @Override
            public void onTargetStateChange(int oldState, int newState) {

            }

            @Override
            public void onCurrentStateChange(int oldState, int newState) {

            }

            @Override
            public void onDisplayModelChange(int oldModel, int newModel) {

            }

            @Override
            public void onPreparing(GiraffePlayer giraffePlayer) {

            }

            @Override
            public void onTimedText(GiraffePlayer giraffePlayer, IjkTimedText text) {

            }

            @Override
            public void onLazyLoadProgress(GiraffePlayer giraffePlayer, int progress) {

            }

            @Override
            public void onLazyLoadError(GiraffePlayer giraffePlayer, String message) {
                Toast.makeText(MainActivity.this, "加载错误。。。", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.textInputLayoutKey.setText(getKey());

        //在所有组件绘制完成后在获取  不然获取不到
        this.getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                //把获取剪切板
                String url = getClipContent();
                if (!TextUtils.isEmpty(url)) {
                    if (binding.videoView.getVideoInfo() == null
                            ||
                            TextUtils.isEmpty(binding.videoView.getVideoInfo().getTitle())
                            ||
                            !binding.videoView.getVideoInfo().getTitle().equals(url)) {
                        binding.textInputLayout.setText(url);
                        binding.btnPlay.callOnClick();
                    }
                }
            }
        });

    }

    void saveKey(String key) {
        //步骤2-1：创建一个SharedPreferences.Editor接口对象，lock表示要写入的XML文件名，MODE_WORLD_WRITEABLE写操作
        SharedPreferences.Editor editor = getSharedPreferences("key", MODE_PRIVATE).edit();
        //步骤2-2：将获取过来的值放入文件
        editor.putString("key", key);
        //步骤3：提交
        editor.commit();
    }

    String getKey() {
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("key", MODE_PRIVATE);
        //步骤2：获取文件中的值
        String value = read.getString("key", defKey);
        return value;
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
        JSONObject jsonObject = new JSONObject();
        String key = binding.textInputLayoutKey.getText().toString();
        if (!TextUtils.isEmpty(key)) {
            if (key.length() == 32) {
                videoInfo.addOption(Option.create(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "decryption_key", key));
                saveKey(key);
            } else {
                Toast.makeText(MainActivity.this, "密钥长度不对 未使用解密播放", Toast.LENGTH_SHORT).show();
            }
        }

//        GiraffePlayer.play(getBaseContext(), videoInfo);
        binding.videoView.videoInfo(videoInfo);
        binding.videoView.getPlayer().start();
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
                        videoInfo.setTitle(file.getUri().toString());
                    } else {
                        videoInfo = new VideoInfo(file.getAbsolutePath());
                        videoInfo.setTitle(file.getAbsolutePath());
                    }
                    play(videoInfo);
                    return;
                }
            }
        }
    }
}