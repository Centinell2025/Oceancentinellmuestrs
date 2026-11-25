# AIS integration

The Android application requests regional observations from the deployed OceanCentinell gateway. Provider credentials are stored only in the gateway environment, never in an APK, HTML, GitHub or client input form.

Provider documentation: https://aisstream.io/documentation
Gateway privacy policy: https://oceancentinell-ais-gateway-production.up.railway.app/privacy

## Activation

1. The owner creates an AISStream account using its supported GitHub sign-in.
2. The owner creates a provider key and sets `AISSTREAM_API_KEY` in the Railway `OceanCentinell-AIS-gateway` service. Do not paste it into chat.
3. Redeploy the service after changing variables. `PORT=3000` matches the generated service domain.
4. Open Fleet Map and choose Start AIS connection.
5. Confirm provider messages, their source, receipt time and reported coordinates. An empty feed is not proof that no vessels are present.

Current state: gateway deployed; provider account/key not configured. No live observation has been verified.

## Scope and limits

- Region: 24–34° N and 85–78° W. The sample fleet is separate from this regional traffic.
- One upstream WebSocket per service instance; bounded reconnection backoff and compression.
- Up to 1,000 cached observations; up to 500 returned per response. No persistent position history.
- A report becomes stale after 3 minutes since gateway receipt and expires after 30 minutes. A provider timestamp is shown separately when supplied.
- Refresh every 15 seconds while the map is active. Pausing or leaving the map stops automatic client refresh. The upstream connection closes when inactive.
- Invalid coordinates, identifiers, unavailable speed/course sentinels and out-of-window timestamps are rejected or marked unavailable.
- Read-only endpoint with bounded request rates. It exposes public regional observations, not customer fleet records. This is not a tenant-authenticated enterprise backend.

AIS coverage, delays and provider service interruptions apply. This map is not a nautical chart or a navigation system. Review provider licensing and service terms before a commercial production deployment.
