// Test-only HTML5 geolocation configuration for Woulwe Grok Desktop.
// It is intentionally separate from network/IP/licensing logic.
// Enable with GROK_TEST_GEO_ENABLED=true and choose coordinates with:
// GROK_TEST_GEO_LAT=..., GROK_TEST_GEO_LON=...
module.exports = { enabled: process.env.GROK_TEST_GEO_ENABLED === 'true' };