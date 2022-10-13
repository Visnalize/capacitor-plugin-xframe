import { registerPlugin } from '@capacitor/core';

import type { XframePlugin } from './definitions';

const Xframe = registerPlugin<XframePlugin>('Xframe', {
  web: () => import('./web').then(m => new m.XframeWeb()),
});

export * from './definitions';
export { Xframe };
