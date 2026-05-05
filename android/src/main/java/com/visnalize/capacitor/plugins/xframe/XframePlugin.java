package com.visnalize.capacitor.plugins.xframe;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.getcapacitor.BridgeWebViewClient;
import com.getcapacitor.JSObject;
import com.getcapacitor.Logger;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginConfig;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import okhttp3.Response;

@SuppressWarnings("unused")
@CapacitorPlugin(name = "Xframe")
public class XframePlugin extends Plugin {
    private final Xframe xframe = new Xframe();
    private final String PLUGIN_ID = "Xframe";
    private final String EVENT_LOAD = "onLoad";
    private final String EVENT_ERROR = "onError";
    private boolean enabled = false;

    private String getHost(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        try {
            return new URL(url).getHost();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public void load() {
        bridge.setWebViewClient(new BridgeWebViewClient(bridge) {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (!enabled) {
                    return super.shouldInterceptRequest(view, request);
                }

                PluginConfig config = bridge.getConfig().getPluginConfiguration(PLUGIN_ID);
                String userAgent = config.getString("userAgent");
                String requestUrl = request.getUrl().toString();
                String selfDomain = getHost(bridge.getAppUrl());

                if (requestUrl.contains(selfDomain) || !request.getMethod().equals("GET")) {
                    return super.shouldInterceptRequest(view, request);
                }

                Logger.debug(PLUGIN_ID, "Intercepting url: " + requestUrl);
                try {
                    Map<String, String> requestHeaders = request.getRequestHeaders();
                    if (userAgent != null) requestHeaders.put("User-Agent", userAgent);
                    Response response = xframe.request(requestUrl, request.getMethod(), requestHeaders, null);

                    try {
                        if (!xframe.getMimeType(response).equals("text/html")) {
                            return xframe.transform(response);
                        }

                        if (response.isSuccessful()) {
                            JSObject documentData = xframe.getDocumentData(response, requestUrl);
                            Logger.debug(PLUGIN_ID, "Document data: " + String.valueOf(documentData));
                            notifyListeners(EVENT_LOAD, documentData);
                        } else {
                            Logger.debug(PLUGIN_ID, "Error retrieving response for: " + requestUrl);
                            notifyListeners(EVENT_ERROR, xframe.getResponseError(response, requestUrl));
                        }
                        return xframe.transform(response);
                    } catch (Exception e) {
                        response.close();
                        throw e;
                    }
                } catch (Exception e) {
                    Logger.debug(PLUGIN_ID, "Error intercepting url: " + e.getMessage());
                    notifyListeners(EVENT_ERROR, xframe.getGenericError(requestUrl));
                    return super.shouldInterceptRequest(view, request);
                }
            }
        });
    }

    @PluginMethod
    public void start(PluginCall call) {
        enabled = true;
        call.resolve();
    }

    @PluginMethod
    public void stop(PluginCall call) {
        enabled = false;
        call.resolve();
    }
}
