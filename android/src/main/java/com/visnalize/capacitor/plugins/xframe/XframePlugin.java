package com.visnalize.capacitor.plugins.xframe;

import android.util.LruCache;
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
import java.util.Objects;

import okhttp3.Response;

@SuppressWarnings("unused")
@CapacitorPlugin(name = "Xframe")
public class XframePlugin extends Plugin {
    private final Xframe xframe = new Xframe();
    private final String PLUGIN_ID = "Xframe";
    private final String EVENT_LOAD = "onLoad";
    private final String EVENT_ERROR = "onError";
    private final String WWW = "www.";
    private final LruCache<String, Boolean> interceptedDomains = new LruCache<>(200);
    private String lastInterceptedDomain = null;

    @Override
    public void load() {
        bridge.setWebViewClient(new BridgeWebViewClient(bridge) {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                PluginConfig config = bridge.getConfig().getPluginConfiguration(PLUGIN_ID);
                String userAgent = config.getString("userAgent");
                String flag = config.getString("flag", "xframe=true");
                String requestUrl = request.getUrl().toString();
                Map<String, String> requestHeaders = request.getRequestHeaders();

                String refererDomain = "";
                try {
                    refererDomain = new URL(requestHeaders.get("Referer")).getHost();
                } catch (MalformedURLException e) {
                    return super.shouldInterceptRequest(view, request);
                }

                boolean hasFlagInUrl = requestUrl.contains(flag);
                boolean isSubresourceOfIntercepted = !hasFlagInUrl &&
                        ((refererDomain != null && interceptedDomains.get(refererDomain) != null) || lastInterceptedDomain != null);

                if (!hasFlagInUrl && !isSubresourceOfIntercepted) {
                    return super.shouldInterceptRequest(view, request);
                }

                if (hasFlagInUrl) {
                    String domain = request.getUrl().getHost();
                    interceptedDomains.put(domain, true);
                    // also cache www version of the domain to handle cases where subresources use www while main url doesn't or vice versa
                    if (!Objects.requireNonNull(domain).startsWith(WWW))
                        interceptedDomains.put(WWW + domain, true);
                    lastInterceptedDomain = domain;
                } else {
                    lastInterceptedDomain = null;
                }

                Logger.debug(PLUGIN_ID, "Intercepting url: " + requestUrl);
                try {
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
    public void register(PluginCall call) {
        // left blank intentionally
        // as the plugin modifies the WebViewClient, it seems that can only be done on plugin `load`
        // this function serves as a placeholder for the JS code to register the plugin.
    }
}
