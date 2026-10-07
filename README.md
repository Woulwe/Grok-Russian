# Woulwe Grok

Woulwe Grok is a desktop wrapper based on the open-source AnRkey/Grok-Desktop project.

## Desktop

- Electron desktop shell
- Grok tabs
- Windows portable/installer builds
- Existing Grok Desktop functionality preserved

## Test-only geolocation

For development/testing of websites that use the browser's HTML5 geolocation API, the desktop app can optionally provide fixed coordinates.

This is **not** a VPN, proxy, IP-location changer, account-region changer, or license bypass.

Enable it with environment variables:

- GROK_TEST_GEO_ENABLED=true
- GROK_TEST_GEO_LAT=55.7558
- GROK_TEST_GEO_LON=37.6173
- GROK_TEST_GEO_ACCURACY=100

When disabled (the default), the normal browser geolocation behavior is used.

## Build

```
npm install
npm start
```

Windows:

```
npm run build
```

Portable Windows:

```
npm run build-portable
```

## Mobile

The repository is structured so a separate Android/iOS shell can be added later without changing the desktop Electron core.

## Upstream

Based on AnRkey/Grok-Desktop. Preserve the upstream license and notices when redistributing.
