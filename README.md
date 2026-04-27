# capacitor-plugin-xframe

Capacitor plugin to bypass CORS & same origin policy for iframe.

## How it works

This plugin overrides the `shouldInterceptRequest` behavior of your webview to intercept the outgoing requests that are marked with a [`flag`](#flag) present in the request URL and modifies the resulted responses by eliminating the __`X-Frame-Options`__ and __`Content-Security-Policy`__ headers for them to work in the embeded iframes.

## Supported platforms

* Android

## Install

```bash
npm install capacitor-plugin-xframe
npx cap sync
```

## Config options

Add these options in either `capacitor.config.json` or `capacitor.config.ts`.

### `flag`

The plugin will look for this `flag` in the outgoing requests' URLs to determine whether to intercept them or not. You can set it to any string that you want, but make sure to add it to the URLs of the requests that you want to be intercepted. Default value is `xframe=true`.

```json
{
  ...
  "plugins": {
    "Xframe": {
      "flag": "xframe=true"
    }
  }
}
```

For example, if you want to allow `https://youtube.com` to be embedded in an iframe, you can modify the URL to `https://youtube.com?xframe=true` and the plugin will intercept the request and modify the response to make it work in an iframe.

### `userAgent`

Customize the outgoing requests' `User-Agent` header. Useful to modify the resulted responses.

```json
{
  ...
  "plugins": {
    "Xframe": {
      "userAgent": "<your_custom_user_agent>"
    }
  }
}
```

## API

<docgen-index>

* [`register()`](#register)
* [`addListener('onLoad', ...)`](#addlisteneronload-)
* [`addListener('onError', ...)`](#addlisteneronerror-)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### register()

```typescript
register() => Promise<void>
```

Registers the plugin to your app.

Registering this plugin will override the `shouldInterceptRequest` behavior of your webview.

--------------------


### addListener('onLoad', ...)

```typescript
addListener(eventName: 'onLoad', listener: LoadEventListener) => Promise<PluginListenerHandle>
```

Listens to requests of `document` type and returns some useful information.

| Param           | Type                                                            |
| --------------- | --------------------------------------------------------------- |
| **`eventName`** | <code>'onLoad'</code>                                           |
| **`listener`**  | <code><a href="#loadeventlistener">LoadEventListener</a></code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### addListener('onError', ...)

```typescript
addListener(eventName: 'onError', listener: ErrorEventListener) => Promise<PluginListenerHandle>
```

Listens to failed requests (of any type)

| Param           | Type                                                              |
| --------------- | ----------------------------------------------------------------- |
| **`eventName`** | <code>'onError'</code>                                            |
| **`listener`**  | <code><a href="#erroreventlistener">ErrorEventListener</a></code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### Interfaces


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


#### LoadEventData

| Prop          | Type                |
| ------------- | ------------------- |
| **`url`**     | <code>string</code> |
| **`title`**   | <code>string</code> |
| **`favicon`** | <code>string</code> |


#### ErrorEventData

| Prop             | Type                |
| ---------------- | ------------------- |
| **`url`**        | <code>string</code> |
| **`statusCode`** | <code>number</code> |
| **`message`**    | <code>string</code> |


### Type Aliases


#### LoadEventListener

<code>(eventData: <a href="#loadeventdata">LoadEventData</a>): void</code>


#### ErrorEventListener

<code>(eventData: <a href="#erroreventdata">ErrorEventData</a>): void</code>

</docgen-api>
