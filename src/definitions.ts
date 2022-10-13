/// <reference types="@capacitor/cli" />

import type { PluginListenerHandle } from '@capacitor/core';

export interface XframePlugin {
  /**
   * Registers the plugin to your app. This will override the `shouldInterceptRequest` behavior of your webview.
   */
  register(): Promise<void>;

  /**
   * Listens for any failed requests.
   */
  addListener(
    eventName: 'onError',
    listener: ErrorListener,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
}

export declare type ErrorListener = (error: ErrorDetails) => void;

export interface ErrorDetails {
  url: string;
  mimeType: string;
  statusCode: number;
  message: string;
}

declare module '@capacitor/cli' {
  export interface PluginsConfigs {
    xframe?: {
      userAgent: string;
    };
  }
}
