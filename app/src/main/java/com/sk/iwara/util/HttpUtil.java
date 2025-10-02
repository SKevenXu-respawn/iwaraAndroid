package com.sk.iwara.util;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

public final class HttpUtil {

    /* ========== 单例 ========== */
    private static final class Holder {
        private static final HttpUtil INSTANCE = new HttpUtil();
    }
    public static HttpUtil get() { return Holder.INSTANCE; }

    private final OkHttpClient client;

    private HttpUtil() {


        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HeaderInterceptor())   // 统一头
                .build();
    }

    /* ========== 同步 GET ========== */
    public String getSync(String url, Map<String, String> params) throws IOException {
        Request request = new Request.Builder()
                .url(buildUrl(url, params))
                .build();
        try (Response resp = client.newCall(request).execute()) {
            assert resp.body() != null;
            return resp.body().string();
        }
    }

    /* ========== 异步 GET ========== */
    public void getAsync(String url, Map<String, String> params, NetCallback callback) {
        Request request = new Request.Builder()
                .url(buildUrl(url, params))
                .build();
        client.newCall(request).enqueue(new CallbackAdapter(callback));
    }

    /* ========== 同步 POST-JSON ========== */
    public String postJsonSync(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response resp = client.newCall(request).execute()) {
            assert resp.body() != null;
            return resp.body().string();
        }
    }

    /* ========== 异步 POST-JSON ========== */
    public void postJsonAsync(String url, String json, NetCallback callback) {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new CallbackAdapter(callback));
    }

    /* ========== 异步 POST-Form ========== */
    public void postFormAsync(String url, Map<String, String> form, NetCallback callback) {
        FormBody.Builder fb = new FormBody.Builder();
        if (form != null) {
            for (Map.Entry<String, String> e : form.entrySet()) {
                fb.add(e.getKey(), e.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(fb.build())
                .build();
        client.newCall(request).enqueue(new CallbackAdapter(callback));
    }

    /* ========== 异步 POST-Multipart（单文件示例） ========== */
    public void postFileAsync(String url,
                              Map<String, String> form,
                              String fileKey,
                              File file,
                              NetCallback callback) {

        MultipartBody.Builder mb = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (form != null) {
            for (Map.Entry<String, String> e : form.entrySet()) {
                mb.addFormDataPart(e.getKey(), e.getValue());
            }
        }
        mb.addFormDataPart(fileKey, file.getName(),
                RequestBody.create(file, MediaType.parse("application/octet-stream")));

        Request request = new Request.Builder()
                .url(url)
                .post(mb.build())
                .build();
        client.newCall(request).enqueue(new CallbackAdapter(callback));
    }

    /* ========== 工具 ========== */
    private String buildUrl(String base, Map<String, String> params) {
        if (params == null || params.isEmpty()) return base;
        HttpUrl.Builder b = HttpUrl.parse(base).newBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            b.addQueryParameter(e.getKey(), e.getValue());
        }
        return b.build().toString();
    }

    /* ========== 回调接口 ========== */
    public interface NetCallback {
        void onSuccess(String respBody);
        void onFailure(Exception e);
    }

    /* ========== 异步转接口 ========== */
    private static final class CallbackAdapter implements Callback {
        private final NetCallback target;
        CallbackAdapter(NetCallback target) { this.target = target; }
        @Override public void onFailure(@NonNull Call call, @NonNull IOException e) {
            target.onFailure(e);
        }
        @Override public void onResponse(@NonNull Call call, @NonNull Response response) {
            try (ResponseBody body = response.body()) {
                if (body == null) throw new IOException("body null");
                target.onSuccess(body.string());
            } catch (Exception e) {
                target.onFailure(e);
            }
        }
    }

    /* ========== 统一头拦截器 ========== */
    private static final class HeaderInterceptor implements Interceptor {
        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request ori = chain.request();
            Request build = ori.newBuilder()
                    .addHeader("User-Agent", "IwaraAndroid/1.0")
                    .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                    .addHeader("Authorization", "Bearer " + getToken()) // 自行实现
                    .build();
            return chain.proceed(build);
        }
    }

    /* ========== 获取本地缓存的 token，可换成你的 SP / MMKV ========== */
    @Nullable
    private static String getToken() {
        return "";   // TODO: return YourTokenManager.getToken();
    }
}