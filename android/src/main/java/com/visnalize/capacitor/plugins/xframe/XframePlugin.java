package com.visnalize.capacitor.plugins.xframe;

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

import java.util.Map;

import okhttp3.Response;

@SuppressWarnings("unused")
@CapacitorPlugin(name = "Xframe")
public class XframePlugin extends Plugin {
    private final Xframe xframe = new Xframe();
    private final String PLUGIN_ID = "Xframe";
    private final String EVENT_LOAD = "onLoad";
    private final String EVENT_ERROR = "onError";

    @Override
    public void load() {
        bridge.setWebViewClient(new BridgeWebViewClient(bridge) {
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
                    Response response = xframe.request(requestUrl, request.getMethod(), requestHeaders, null);
                    boolean isOk = response.isSuccessful();

                    if (!xframe.getMimeType(response).equals("text/html")) {
                        return xframe.transform(response);
                    }

                    if (isOk) {
                        notifyListeners(EVENT_LOAD, xframe.getDocumentData(response, requestUrl));
                    } else {
                        notifyListeners(EVENT_ERROR, xframe.getResponseError(response, requestUrl));
                    }
                    return xframe.transform(response);
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
