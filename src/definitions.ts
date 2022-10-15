/// <reference types="@capacitor/cli" />

import type { PluginListenerHandle } from '@capacitor/core';

export interface XframePlugin {
  /**
   * Registers the plugin to your app.
   *
   * Registering this plugin will override the `shouldInterceptRequest` behavior of your webview.
   */
  register(): Promise<void>;

  /**
   * Listens to requests of `document` type and returns some useful information.
   */
  addListener(
    eventName: 'onLoad',
    listener: LoadEventListener,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;

  /**
   * Listens to failed requests (of any type)
   */
  addListener(
    eventName: 'onError',
    listener: ErrorEventListener,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
}

export declare type LoadEventListener = (eventData: LoadEventData) => void;

export declare type ErrorEventListener = (eventData: ErrorEventData) => void;

export interface LoadEventData {
  url: string;
  title: string;
  favicon: string;
}

export interface ErrorEventData {
  url: string;
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
