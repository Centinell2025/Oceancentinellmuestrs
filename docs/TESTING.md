# Final validation record

Version: 1.0.1 (3). Package: com.beaconoftheeagle.oceancentinellbusiness.

## Passed

- Eleven Node tests cover local schema validation, duplicate records, invalid dates, spreadsheet formula escaping, map geography, AIS normalization, invalid coordinates, missing values, stale observations and expiration.
- Android release AAB and debug APK compile against API 36; Android lint passes.
- The installed Android app passed on an API 35 emulator: entry, vessel filtering, reference map, native AIS gateway response and local maintenance persistence.
- Phone layout was corrected after reviewing real emulator captures. The final test also checks that the content and map occupy the visible viewport.
- Browser checks passed for incident creation, pilot draft/checklist persistence, maintenance priorities, CSV/JSON downloads and validated backup restoration.
- The signed release bundle's packaged interface HTML, CSS, JavaScript and image assets match the reviewed `dist/` source byte-for-byte. Its signature was verified and Google Play accepted version code 3.

Final build, lint and installed-app run: https://github.com/Centinell2025/Oceancentinellmuestrs/actions/runs/45534237633

The final run's build and Android smoke jobs both passed. The first smoke attempt stopped during GitHub action download, before testing; the rerun downloaded successfully and passed on the API 35 emulator.

The store media use actual Android welcome, vessel-register and fleet-map captures from the earlier successful viewport-validation run: https://github.com/Centinell2025/Oceancentinellmuestrs/actions/runs/45532429137. Those captures display the previous version label; the demonstration workflows are unchanged in 1.0.1.

## AIS boundary

The deployed gateway answers HTTP 200 with `provider_key_required`, empty observations and disconnected status. This proves app-to-gateway connectivity and missing-configuration behavior. It does not prove a live AIS subscription. The publisher still needs to activate its provider account/key. Never label fictional sample vessels as live tracking.

The tests cover this demonstration's workflows. They are not an independent penetration test, enterprise security certification, maritime navigation approval or guarantee of Google Play acceptance.

Version 1.0.1 (3) advances metadata after Play Console reported version codes 1 and 2 already used. The signed bundle is uploaded and saved in a Google Play production draft. Final publisher declarations and review submission remain pending.
