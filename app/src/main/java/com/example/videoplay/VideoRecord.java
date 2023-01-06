package com.example.videoplay;

import java.util.Objects;

public class VideoRecord {
    //播放地址
    public String uri = "";
    //解密key
    public String key = "";
    //当前播放进度
    public int duration = 0;

    @Override
    public String toString() {
        return "VideoRecord{" +
                "uri='" + uri + '\'' +
                ", key='" + key + '\'' +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoRecord that = (VideoRecord) o;
        return uri.equals(that.uri) && key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, key);
    }
}
