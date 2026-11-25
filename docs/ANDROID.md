# Android build and installation

Open the `android/` directory in Android Studio. Install Android SDK 36 and use Java 21 with Gradle 8.13. The application sources compile for Java 17.

```sh
cd android
./gradlew lintDebug assembleDebug assembleDebugAndroidTest bundleRelease
```

The debug APK is under `app/build/outputs/apk/debug`. The unsigned release bundle is under `app/build/outputs/bundle/release`. GitHub Actions publishes build outputs and the lint report as downloadable artifacts.

Use the publisher's upload key to sign the release AAB outside this repository. Do not commit signing files or passwords. Google Play App Signing manages the distribution signature after enrollment.

The application loads packaged assets through a restricted HTTPS WebView origin. All local records stay on the device. File exports and imports use the Android document picker. The native AIS method calls one fixed HTTPS gateway; it accepts no arbitrary URL. Internet permission is the only requested permission.

To test the installed interface, run the Actions Android smoke job. The instrumentation uses a clean emulator and captures the welcome, overview, vessel register and fleet map. A debug APK is a test build, not the Play Store distribution.
