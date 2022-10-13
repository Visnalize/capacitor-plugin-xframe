# capacitor-plugin-xframe

Capacitor plugin to bypass CORS & same origin policy for iframe.

## Important note

> This plugin overrides the `shouldInterceptRequest` behavior of your webview.

As the core purpose of this plugin, `shouldInterceptRequest` needs to be leveraged to determine the incoming requests and eliminate the __`X-Frame-Options`__ and __`Content-Security-Policy`__ headers present on the responses for them to work in the embeded iframes.

## Supported platforms

* Android

## Install

```bash
npm install capacitor-plugin-xframe
npx cap sync
```

## Config options

### `userAgent`

Customize the request's `User-Agent` header.

```json
// capacitor.config.json or capacitor.config.ts
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

Registers the plugin to your app. This will override the `shouldInterceptRequest` behavior of your webview.

--------------------

### addListener('onError', ...)

```typescript
addListener(eventName: 'onError', listener: ErrorListener) => Promise<PluginListenerHandle> & PluginListenerHandle
```

Listens for any failed requests.

| Param           | Type                                                    |
| --------------- | ------------------------------------------------------- |
| **`eventName`** | <code>'onError'</code>                                  |
| **`listener`**  | <code><a href="#errorlistener">ErrorListener</a></code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt; & <a href="#pluginlistenerhandle">PluginListenerHandle</a></code>

--------------------

### Interfaces

#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |

#### ErrorDetails

| Prop             | Type                |
| ---------------- | ------------------- |
| **`url`**        | <code>string</code> |
| **`statusCode`** | <code>number</code> |
| **`message`**    | <code>string</code> |

### Type Aliases

#### ErrorListener

<code>(error: <a href="#errordetails">ErrorDetails</a>): void</code>

</docgen-api>
