package com.sk.iwara.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 25140 on 2025/10/13 .
 */
public class VideoTask {
    public static List<Video> video;
    public static class Video{
        private String videoId;
        private long pos;

        public Video(String videoId, long pos) {
            this.videoId = videoId;
            this.pos = pos;
        }

        public long getPos() {
            return pos;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setPos(long pos) {
            this.pos = pos;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }
    }

}
