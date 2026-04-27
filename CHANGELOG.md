# Changelog

All notable changes to this project will be documented in this file.

## 8.1.0

- 💥 Breaking change: The behavior of this plugin has been adjusted to only intercept requests with the URLs matching the pattern specified with the `flag` option, rather than intercepting almost all requests, which affected performance and memory usage.
- Remove `ignore` config option in favor of the `flag` option for behavioral change.
- Fix OutOfMemoryError due to inadequate handling of large response bodies.

## 8.0.0

- 💥 Breaking change: Upgrade to Capacitor 8.x
