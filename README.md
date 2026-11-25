# OceanCentinell Business 1.0.0

Beacon of the Eagle LLC. Free B2B demonstration for enterprise conversations. Web and Android share a packaged, offline-capable demonstration interface. Synthetic scenario only.

Available: sample fleet/asset register, fleet filtering, local incident creation and status updates, maintenance updates, pilot-scope form and checklist, executive/pilot TXT exports CSV registers for Excel, local JSON backup and validated recovery. Records stay in the browser or Android WebView. No analytics or automatic data transfer.

Not implemented: live AIS/GPS, telemetry, scanning, real AI, company accounts, MFA, RBAC, tenant isolation, encrypted application database, tamper-evident audit logs . The demonstration is not a production maritime service and must not be used for navigation, emergencies, safety-critical operations or real confidential customer records.

Source references reviewed: the owner's OceanCentinell Enterprise HTML and OceanCentinell USA register workflows. Original reference files and existing repositories are preserved. This is a separate demonstration, not the complete Enterprise implementation.

Android namespace: `com.beaconoftheeagle.oceancentinellbusiness`. Version 1.0.0 (1), minimum API 26, target API 36. The AAB must be signed and tested before store submission. Google Play pricing: free demonstration, as selected by the owner. B2B services are subject to a separate agreement; the app contains no checkout, purchase or subscription implementation.

Support: centinellsuite@gmail.com.

## Preview and Android build

Open `dist/index.html` through a static HTTP server. No build step or external runtime is required. The map works offline with bundled Natural Earth coastlines, zoom, pan, vessel filters and approximate port reference areas. It contains no live positions or invented AIS measurements.

GitHub Actions builds the Android APK and unsigned release AAB on pushes to main. Signing keys are never stored in this repository. Run `node --test tests/demo.test.cjs` for data and export checks.

For real AIS: select a provider and its permitted commercial usage, deploy a server connector with secrets outside client code, restrict the fleet and area, track message age and connection state, and then enable an authenticated client endpoint. No provider key has been configured.
