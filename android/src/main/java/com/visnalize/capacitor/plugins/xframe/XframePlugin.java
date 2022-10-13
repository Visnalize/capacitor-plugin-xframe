package com.visnalize.capacitor.plugins.xframe;

import android.webkit.MimeTypeMap;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.getcapacitor.BridgeWebViewClient;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginConfig;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Response;

@SuppressWarnings("unused")
@CapacitorPlugin(name = "Xframe")
public class XframePlugin extends Plugin {
    private final Xframe xframe = new Xframe();
    private final String PLUGIN_ID = "Xframe";
    private final String EVENT_ERROR = "onError";

    @Override
    public void load() {
        bridge.setWebViewClient(new BridgeWebViewClient(bridge) {
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                JSObject result = new JSObject();
                result.put("url", request.getUrl());
                result.put("mimeType", errorResponse.getMimeType());
                result.put("statusCode", errorResponse.getStatusCode());
                result.put("message", errorResponse.getReasonPhrase());
                notifyListeners(EVENT_ERROR, result);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                PluginConfig config = bridge.getConfig().getPluginConfiguration(PLUGIN_ID);
                String userAgent = config.getString("userAgent");
                String requestUrl = request.getUrl().toString();

                // intercepting guards
                if (requestUrl.contains(bridge.getAppUrl()) || !request.getMethod().equals("GET")) {
                    return super.shouldInterceptRequest(view, request);
                }

                try {
                    Map<String, String> requestHeaders = request.getRequestHeaders();
                    if (userAgent != null) requestHeaders.put("User-Agent", userAgent);
                    Response _response = xframe.request(requestUrl, request.getMethod(), requestHeaders, null);
                    Map<String, String> _responseHeaders = new HashMap<>();
                    for (String headerName : _response.headers().names()) {
                        _responseHeaders.put(headerName, _response.header(headerName));
                    }

                    return new WebResourceResponse("", "utf-8",
                            _response.code(),
                            _response.message().isEmpty() ? "OK" : _response.message(),
                            _responseHeaders,
                            Objects.requireNonNull(_response.body()).byteStream()
                    );
                } catch (Exception e) {
                    return super.shouldInterceptRequest(view, request);
                }
            }
        });
    }

    @PluginMethod
    public void register(PluginCall call) {
        // left blank intentionally
        // as the plugin modifies the WebViewClient, it seems that can only be done on plugin `load`
        // this function serves as a placeholder for the JS code to register the plugin.
    }
}
