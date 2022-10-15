# capacitor-plugin-xframe

Capacitor plugin to bypass CORS & same origin policy for iframe.

## Important note

> This plugin overrides the `shouldInterceptRequest` behavior of your webview.

As the core purpose of this plugin, `shouldInterceptRequest` needs to be leveraged to determine the outgoing requests and eliminate the __`X-Frame-Options`__ and __`Content-Security-Policy`__ headers present on the incoming responses for them to work in the embeded iframes.

## Supported platforms

* Android

## Install

```bash
npm install capacitor-plugin-xframe
npx cap sync
```

## Config options

Add these options in either `capacitor.config.json` or `capacitor.config.ts`.

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
* [`addListener('onLoad', ...)`](#addlisteneronload)
* [`addListener('onError', ...)`](#addlisteneronerror)
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
addListener(eventName: 'onLoad', listener: LoadEventListener) => Promise<PluginListenerHandle> & PluginListenerHandle
```

Listens to requests of `document` type and returns some useful information.

| Param           | Type                                                            |
| --------------- | --------------------------------------------------------------- |
| **`eventName`** | <code>'onLoad'</code>                                           |
| **`listener`**  | <code><a href="#loadeventlistener">LoadEventListener</a></code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt; & <a href="#pluginlistenerhandle">PluginListenerHandle</a></code>

--------------------


### addListener('onError', ...)

```typescript
addListener(eventName: 'onError', listener: ErrorEventListener) => Promise<PluginListenerHandle> & PluginListenerHandle
```

Listens to failed requests (of any type)

| Param           | Type                                                              |
| --------------- | ----------------------------------------------------------------- |
| **`eventName`** | <code>'onError'</code>                                            |
| **`listener`**  | <code><a href="#erroreventlistener">ErrorEventListener</a></code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt; & <a href="#pluginlistenerhandle">PluginListenerHandle</a></code>

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
