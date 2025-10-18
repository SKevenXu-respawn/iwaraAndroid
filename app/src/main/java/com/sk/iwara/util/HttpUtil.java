package com.sk.iwara.util;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

public final class HttpUtil {

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
                .build();
    }

    /* ================== 异步 GET ================== */
    public Call getAsync(String url,
                         @Nullable Map<String, String> params,
                         @Nullable Map<String, String> headers,
                         NetCallback callback) {
        Request.Builder rb = new Request.Builder()
                .url(buildUrl(url, params))
                .headers(Headers.of(defaultHeaders(headers))); // 允许空
        Request request = rb.build();
        Call call = client.newCall(request);
        Log.d("HttpUtil",new Gson().toJson(request));
        call.enqueue(new CallbackAdapter(callback));
        return call;   // 外部可 cancel()
    }

    /* ================== 异步 POST-JSON ================== */
    public Call postJsonAsync(String url,
                              @Nullable String json,
                              @Nullable Map<String, String> headers,
                              NetCallback callback) {
        RequestBody body = RequestBody.create((json == null) ? "{}" : json,
                MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(defaultHeaders(headers)))
                .post(body)
                .build();
        Log.d("HttpUtil",new Gson().toJson(request));
        Call call = client.newCall(request);
        call.enqueue(new CallbackAdapter(callback));
        return call;
    }

    /* ================== 异步 POST-Form ================== */
    public Call postFormAsync(String url,
                              @Nullable Map<String, String> form,
                              @Nullable Map<String, String> headers,
                              NetCallback callback) {
        FormBody.Builder fb = new FormBody.Builder();
        if (form != null) {
            for (Map.Entry<String, String> e : form.entrySet()) {
                fb.add(e.getKey(), e.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(defaultHeaders(headers)))
                .post(fb.build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(new CallbackAdapter(callback));
        return call;
    }

    /* ================== 异步 POST-Multipart（单文件） ================== */
    public Call postFileAsync(String url,
                              @Nullable Map<String, String> form,
                              String fileKey,
                              File file,
                              @Nullable Map<String, String> headers,
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
                .headers(Headers.of(defaultHeaders(headers)))
                .post(mb.build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(new CallbackAdapter(callback));
        return call;
    }

    /* ================== 工具 ================== */
    private String buildUrl(String base, @Nullable Map<String, String> params) {
        if (params == null || params.isEmpty()) return base;
        HttpUrl.Builder b = HttpUrl.parse(base).newBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            b.addQueryParameter(e.getKey(), e.getValue());
        }
        return b.build().toString();
    }

    /* 合并默认头 + 自定义头，自定义头优先 */
    private Map<String, String> defaultHeaders(@Nullable Map<String, String> custom) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Mobile Safari/537.36 Edg/141.0.0.0");
        map.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        if (custom != null) map.putAll(custom);
        return map;
    }

    /* ================== 回调接口 ================== */
    public interface NetCallback {
        void onSuccess(String respBody);
        void onFailure(Exception e);
    }

    private static final class CallbackAdapter implements Callback {
        private final NetCallback target;
        CallbackAdapter(NetCallback target) { this.target = target; }
        @Override public void onFailure(@NonNull Call call, @NonNull IOException e) {
            target.onFailure(e);
        }
        @Override public void onResponse(@NonNull Call call, @NonNull Response response) {
            try (ResponseBody body = response.body()) {

                if (body == null) throw new IOException("body null");
                String json = body.string();
// 先解析再格式化，保证合法
                JsonElement je = new JsonParser().parse(json);
                String pretty = new GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .toJson(je);
                Log.d("HttpUtil", pretty);
                target.onSuccess(json);
            } catch (Exception e) {
                target.onFailure(e);
            }
        }
    }
}