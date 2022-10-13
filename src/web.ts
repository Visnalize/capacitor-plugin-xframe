import { WebPlugin } from '@capacitor/core';

import type { XframePlugin } from './definitions';

export class XframeWeb extends WebPlugin implements XframePlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
