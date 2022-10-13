export interface XframePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
