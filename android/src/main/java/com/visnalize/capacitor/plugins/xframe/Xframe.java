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

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

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
        ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
        Document doc = Jsoup.parse(responseBody.byteStream(), null, "");
        Element faviconElem = doc.head().selectFirst("[rel='icon']");

        JSObject result = new JSObject();
        result.put("url", requestUrl);
        result.put("title", doc.title());
        result.put("favicon", faviconElem == null ? "" : faviconElem.attr("href"));
        return result;
    }

    public JSObject getResponseError(Response response, String requestUrl) {
        JSObject result = new JSObject();
        result.put("url", requestUrl);
        result.put("statusCode", response.code());
        result.put("message", response.message());
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
        try {
            // as the response body can only be consumed once,
            // use `peekBody` to create a copy to work around this limitation
            ResponseBody responseBody = response.peekBody(Integer.MAX_VALUE);
            // a document response should not have an empty body
            return responseBody.string().isEmpty() ? null : responseBody.contentType();
        } catch (Exception exception) {
            return null;
        }
    }
}
