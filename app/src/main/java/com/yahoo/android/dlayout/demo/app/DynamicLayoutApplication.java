package com.yahoo.android.dlayout.demo.app;

import android.app.Application;
import android.util.Log;

import com.yahoo.android.dlayout.LayoutLoader;
import com.yahoo.android.dlayout.demo.cache.LayoutCache;
import com.yahoo.android.dlayout.demo.http.Httpd;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by vgaurav on 3/15/16.
 */
public class DynamicLayoutApplication extends Application {

    private LayoutLoader layoutLoader;
    private Httpd httpd;
    private OkHttpClient httpClient;
    private LayoutCache layoutCache;

    @Override
    public void onCreate() {
        super.onCreate();
        layoutLoader = new LayoutLoader().initialize();
        startServer();
        layoutCache = new LayoutCache();
        initializeLayoutCache(getLayoutCache());
    }

    private void initializeLayoutCache(LayoutCache layoutCache) {

        for(int i = 1; i <= 2; i++) {
            String url = "http://127.0.0.1:8888/0" + i;

            Request req = new Request.Builder().url(url).get().build();
            OkHttpClient client = getHttpClient();

            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.w("DLayout", "[FAILED] Loaded Layout: " + call.request().url(), e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] data = response.body().bytes();
                    String url = response.request().url().toString();
                    Log.w("DLayout", "Loaded Layout: " + url);
                    getLayoutCache().put(url, data);
                }
            });
        }

    }

    public LayoutLoader getLayoutLoader() {
        return layoutLoader;
    }

    public void startServer() {
        if(httpd == null) {
            httpd = new Httpd(this, 8888);
            try {
                httpd.start();
                Log.d("DLayout", "Server started...");
            } catch (IOException e) {
                Log.d("DLayout", "Server failed to start", e);
            }
        }
    }

    public void stopServer() {
        if(httpd != null) {
            httpd.stop();
            httpd = null;
        }
    }

    public OkHttpClient getHttpClient() {
        if(httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request req = chain.request();
                    Log.d("DLayout", "[http-request] url=" + req.url());
                    return chain.proceed(req);
                }
            }).build();
        }
        return httpClient;
    }

    public LayoutCache getLayoutCache() {
        return layoutCache;
    }
}
