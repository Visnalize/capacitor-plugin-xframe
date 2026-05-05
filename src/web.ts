import { WebPlugin } from '@capacitor/core';

import type { XframePlugin } from './definitions';

export class XframeWeb extends WebPlugin implements XframePlugin {
  async start(): Promise<void> {
    // do nothing on web
  }
  async stop(): Promise<void> {
    // do nothing on web
  }
}
