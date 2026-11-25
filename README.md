# OceanCentinell Business

**Android enterprise demonstration · Version 1.0.0 (1)**

Beacon of the Eagle LLC. A free sample for companies evaluating maritime operating workflows. B2B services, integrations, licensing and support are agreed separately.

## What works

- Maritime Operations Center with priorities calculated from the local scenario.
- Vessel search and port filtering, equipment and IT/OT asset inventory.
- Synthetic incident creation, ownership and status changes; maintenance follow-up.
- Interactive geographic reference map, zoom, pan and port selection.
- Pilot scope, acceptance checklist and executive/pilot brief exports.
- CSV registers for Excel, JSON backup and validated restore with replacement confirmation.
- Optional regional AIS connection through a separate server gateway. The provider key is pending; no live positions are claimed.

## Build and verify

```sh
node --test tests/*.cjs tests/*.mjs
cd android
./gradlew lintDebug assembleDebug assembleDebugAndroidTest bundleRelease
```

The Actions workflow builds Android outputs, runs lint, and tests the installed application on an emulator. Read the latest workflow result before treating the emulator check as passed. Signing credentials are kept outside this repository.

| Component | Location |
| --- | --- |
| Packaged interface | `dist/` |
| Android project | `android/` |
| AIS gateway | `backend/` |
| Data and AIS tests | `tests/` |
| Store copy and privacy | `play-store/` |
| Installation | [Android guide](docs/ANDROID.md) |
| Provider activation | [AIS guide](docs/AIS.md) |
| Test evidence | [Validation record](docs/TESTING.md) |
| Store progress | [Google Play preparation](docs/GOOGLE_PLAY.md) |

Package: `com.beaconoftheeagle.oceancentinellbusiness`. Minimum API 26; target/compile API 36. Internet permission is used only for the optional fixed AIS gateway. No device location, contacts, camera, microphone, advertising ID or broad storage permissions are requested. Local records do not synchronize to a company server.

## Evaluation boundaries

The sample uses three fictional vessels and six equipment records. The reference map is not live vessel tracking. Optional AIS observations are a separate public regional layer with timestamps, stale-state marking and expiration. The gateway runs; the publisher still needs to activate its AISStream account/key.

Production company accounts, MFA, RBAC, tenant isolation, encrypted server data, telemetry, threat detection, real AI, audit integrity and service guarantees are not implemented. Use synthetic records only. This application is not for navigation, emergencies or safety-critical decisions.

The owner's Enterprise HTML is the design basis. Original files and other OceanCentinell repositories are preserved. `dist/` is the shared app interface and a development preview, not an additional public SaaS release.

Support and privacy: **centinellsuite@gmail.com**.
© 2026 Beacon of the Eagle LLC. All rights reserved. See [third-party map notices](THIRD_PARTY_NOTICES.md).
