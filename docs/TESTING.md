# Validation record

Version: 1.0.0 (1). Package: com.beaconoftheeagle.oceancentinellbusiness.

- Eleven Node tests pass: local schema validation, duplicate records, invalid dates, spreadsheet formula escaping, map geography, AIS normalization, invalid coordinates, missing values, stale observations and expiration.
- Android release AAB and debug APK compile against API 36; Android lint passes.
- Signed release bundle contents have been compared byte-for-byte with the reviewed HTML, CSS, JavaScript and image assets.
- Browser checks pass: fleet filtering, incident creation, maintenance changes updating priorities, pilot draft and checklist persistence, CSV and JSON downloads, validated backup preview and restoration, map selection and gateway response.
- The regional gateway responds HTTP 200 with `provider_key_required`, an empty position list and disconnected status. This verifies connectivity and honest missing-configuration behavior; it does not verify a live AIS subscription.
- Installed Android emulator smoke test PASSED on API 35: the packaged app loaded, fleet filtering worked, the reference map rendered, the native AIS gateway responded and maintenance changes persisted locally. Workflow run: https://github.com/Centinell2025/Oceancentinellmuestrs/actions/runs/45531588674. Real app screens were captured in the same run.

Never describe live vessel tracking as verified until a provider key is configured and actual provider messages have been received and checked.

A visual review of the initial emulator captures found the inherited mobile sidebar occupying the viewport. The phone layout has been corrected and viewport visibility assertions have been added; the final capture run must pass before submission.
