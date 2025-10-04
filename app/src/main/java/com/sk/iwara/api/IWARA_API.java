package com.sk.iwara.api;

public class IWARA_API {
    public static final String VIDEO="https://api.iwara.tv";
    public static final String IMAGE="https://i.iwara.tv/image/";

    public static final String VIDEO_DETAIL="https://lumi.iwara.tv/view";
    public static String getHomeVideos(String type,int limit,int page){
        return VIDEO+ "/videos?rating=echo&sort="+type+"&limit="+limit+"&page="+page;
    }
    public static String getHomeVideoImage(String type,int limit){
        return IMAGE+ "/videos?rating=echo&sort="+type+"&limit="+limit;
    }
}
