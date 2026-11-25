# Validation record

Version: 1.0.0 (1). Package: com.beaconoftheeagle.oceancentinellbusiness.

- Eleven Node tests pass: local schema validation, duplicate records, invalid dates, spreadsheet formula escaping, map geography, AIS normalization, invalid coordinates, missing values, stale observations and expiration.
- Android release AAB and debug APK compile against API 36; Android lint passes.
- Signed release bundle contents have been compared byte-for-byte with the reviewed HTML, CSS, JavaScript and image assets.
- Browser checks pass: fleet filtering, incident creation, maintenance changes updating priorities, pilot draft and checklist persistence, CSV and JSON downloads, validated backup preview and restoration, map selection and gateway response.
- The regional gateway responds HTTP 200 with `provider_key_required`, an empty position list and disconnected status. This verifies connectivity and honest missing-configuration behavior; it does not verify a live AIS subscription.
- Installed Android emulator smoke test is being completed. Its workflow captures real app screens and checks fleet filtering, reference map, native AIS gateway and local task persistence. See the latest Actions run before treating it as passed.

Never describe live vessel tracking as verified until a provider key is configured and actual provider messages have been received and checked.
