import { WebPlugin } from '@capacitor/core';

import type { XframePlugin } from './definitions';

export class XframeWeb extends WebPlugin implements XframePlugin {
  async register(): Promise<void> {
    // left blank intentionally
  }
}
