package com.visnalize.capacitor.plugins.xframe;

import java.io.IOException;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Xframe {
    OkHttpClient client = new OkHttpClient();

    public Response request(String url, String method, Map<String, String> headers, RequestBody body) throws IOException {
        Request _request = new Request.Builder()
                .headers(Headers.of(headers))
                .url(url)
                .method(method, body)
                .build();

        Response.Builder responseBuilder = client.newCall(_request).execute().newBuilder();
        responseBuilder.removeHeader("X-Frame-Options");
        responseBuilder.removeHeader("Content-Security-Policy");
        return responseBuilder.build();
    }
}
