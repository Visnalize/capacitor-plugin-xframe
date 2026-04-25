import { WebPlugin } from '@capacitor/core';
import type { PluginListenerHandle } from '@capacitor/core';

import type {
  ErrorEventListener,
  LoadEventListener,
  XframePlugin,
} from './definitions';

export class XframeWeb extends WebPlugin implements XframePlugin {
  async register(): Promise<void> {
    // left blank intentionally
  }

  addListener(
    eventName: 'onLoad',
    listener: LoadEventListener,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
  addListener(
    eventName: 'onError',
    listener: ErrorEventListener,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
  addListener(
    eventName: 'onLoad' | 'onError',
    listener: LoadEventListener | ErrorEventListener,
  ): Promise<PluginListenerHandle> & PluginListenerHandle {
    return super.addListener(eventName, listener as (...args: any[]) => void) as Promise<PluginListenerHandle> & PluginListenerHandle;
  }
}
