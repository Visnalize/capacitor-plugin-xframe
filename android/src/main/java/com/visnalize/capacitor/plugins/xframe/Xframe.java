package com.visnalize.capacitor.plugins.xframe;

import android.webkit.WebResourceResponse;

import com.getcapacitor.JSObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Xframe {
    OkHttpClient client = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
            .build();

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

    public WebResourceResponse transform(Response response) {
        Map<String, String> responseHeaders = new HashMap<>();
        for (String headerName : response.headers().names()) {
            responseHeaders.put(headerName, response.header(headerName));
        }

        return new WebResourceResponse(
                getMimeType(response),
                getEncoding(response),
                response.code(),
                response.message().isEmpty() ? "OK" : response.message(),
                responseHeaders,
                Objects.requireNonNull(response.body()).byteStream()
        );
    }

    public JSObject getDocumentData(Response response, String requestUrl) throws IOException {
        // as the response body can only be consumed once,
        // use `peekBody` to create a copy to work around this limitation
        ResponseBody responseBody = response.peekBody(1024 * 1024); // peek only the first 1MB for memory safety
        Document doc = Jsoup.parse(responseBody.byteStream(), null, requestUrl);
        Element faviconElem = doc.head().selectFirst("[rel='icon'], [rel='shortcut icon']");

        JSObject result = new JSObject();
        result.put("url", requestUrl);
        result.put("title", doc.title());
        result.put("favicon", faviconElem == null ? "" : faviconElem.attr("abs:href"));
        return result;
    }

    public JSObject getResponseError(Response response, String requestUrl) {
        JSObject result = new JSObject();
        result.put("url", requestUrl);
        result.put("statusCode", response.code());
        result.put("message", response.message());
        return result;
    }

    public JSObject getGenericError(String requestUrl) {
        JSObject result = new JSObject();
        result.put("url", requestUrl);
        return result;
    }

    protected String getEncoding(Response response) {
        MediaType responseType = getResponseType(response);
        if (responseType == null) return "";
        Charset charset = responseType.charset();
        return charset == null ? "" : charset.toString();
    }

    protected String getMimeType(Response response) {
        MediaType responseType = getResponseType(response);
        return responseType == null ? "" : responseType.type() + "/" + responseType.subtype();
    }

    private MediaType getResponseType(Response response) {
        return response.body() != null ? response.body().contentType() : null;
    }
}
