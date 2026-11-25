# OceanCentinell Business 1.0.0

Beacon of the Eagle LLC. Free B2B demonstration for enterprise conversations. Web and Android share a packaged, local demonstration interface with optional Internet AIS observations. Synthetic scenario only.

Available: sample fleet/asset register, fleet filtering, local incident creation and status updates, maintenance updates, pilot-scope form and checklist, executive/pilot TXT exports CSV registers for Excel, local JSON backup and validated recovery. Records stay in the browser or Android WebView. No analytics or automatic data transfer.

Not implemented: customer fleet tracking, telemetry, scanning, real AI, company accounts, MFA, RBAC, tenant isolation, encrypted application database, tamper-evident audit logs. The demonstration is not a production maritime service and must not be used for navigation, emergencies, safety-critical operations or real confidential customer records.

Source references reviewed: the owner's OceanCentinell Enterprise HTML and OceanCentinell USA register workflows. Original reference files and existing repositories are preserved. This is a separate demonstration, not the complete Enterprise implementation.

Android namespace: `com.beaconoftheeagle.oceancentinellbusiness`. Version 1.0.0 (1), minimum API 26, target API 36; Internet permission for the fixed AIS gateway. The AAB must be signed and tested before store submission. Google Play pricing: free demonstration, as selected by the owner. B2B services are subject to a separate agreement; the app contains no checkout, purchase or subscription implementation.

Support: centinellsuite@gmail.com.

## Preview and Android build

Open `dist/index.html` through a static HTTP server. No build step or external runtime is required. The reference map works offline with bundled Natural Earth coastlines, zoom, pan, vessel filters and approximate port reference areas. No invented AIS measurements are used.

GitHub Actions builds the Android APK and unsigned release AAB on pushes to main. Signing keys are never stored in this repository. Run `node --test tests/demo.test.cjs` for data and export checks.

For real AIS: select a provider and its permitted commercial usage, deploy a server connector with secrets outside client code, restrict the fleet and area, track message age and connection state, and then enable an authenticated client endpoint. No provider key has been configured.

## AIS activation

Gateway deployed separately in Railway: `OceanCentinell-AIS-gateway`. Add `AISSTREAM_API_KEY` as a service variable using the provider account. Keep it out of GitHub and Android assets. Then choose Fleet Map → Start AIS connection. The sample is limited to regional traffic at 24–34° N, 85–78° W; it does not identify a customer fleet. It marks reception age above 3 minutes as stale and expires observations after 30 minutes. No persistent AIS history or uptime guarantee is provided.

Run `node backend/build.mjs` to generate the single Railway Function source and `node --test tests/*.cjs tests/*.mjs` to check validation, stale-data handling and exports.
