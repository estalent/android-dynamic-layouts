package com.yahoo.android.dlayout.demo.http;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.yahoo.android.dlayout.demo.R;

import java.io.IOException;
import java.io.InputStream;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by vgaurav on 3/15/16.
 */
public class Httpd extends NanoHTTPD {
    private final Context context;

    public Httpd(Context ctx, int port) {
        super("127.0.0.1", port);
        this.context = ctx;
    }

    @Override
    public void start() throws IOException {
        super.start();
        Log.d("DLayout", "server started");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        Log.d("DLayout", "[Server::serve] uri=" + uri);

        Resources res = context.getResources();
        InputStream input = null;

        if("/01".equals(uri)) {
            input = res.openRawResource(R.raw.dynamic_layout_01);
        } else {
            input = res.openRawResource(R.raw.dynamic_layout_02);
        }
        return newChunkedResponse(Response.Status.OK, "application/octet-stream", input);
    }
}
