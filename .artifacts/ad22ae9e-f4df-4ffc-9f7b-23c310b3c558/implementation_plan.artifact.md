# Fix SecurityException: Unknown calling package name 'com.google.android.gms'

The error `java.lang.SecurityException: Unknown calling package name 'com.google.android.gms'` is likely caused by a combination of:
1.  **Incorrect Gradle Dependencies**: Gradle plugins (like `google-services` and `hilt`) are incorrectly added as `implementation` dependencies in the `build.gradle.kts` files. This can leak plugin internal classes into the app's classpath, causing conflicts with Google Play Services.
2.  **Experimental SDK Versions**: The `medicationstracker` module is targeting API 36, which is not yet stable/released. This can trigger unexpected security enforcements.
3.  **Missing Package Visibility**: API 30+ requires explicit declaration of package visibility for certain interactions, which might be necessary here due to the high target SDK.

## Proposed Changes

### [medicationstracker]

#### [MODIFY] [build.gradle.kts](file:///C:/Users/zheny/Documents/my/projects/workin/Medications_Tracker_Main/medicationstracker/build.gradle.kts)
- Downgrade `compileSdk` and `targetSdk` to 35.
- Remove `implementation(libs.com.google.gms.google.services.gradle.plugin)` from `dependencies`.
- Remove `implementation(libs.com.google.dagger.hilt.android.gradle.plugin)` from `dependencies`.

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/zheny/Documents/my/projects/workin/Medications_Tracker_Main/medicationstracker/src/main/AndroidManifest.xml)
- Add `<queries>` for `com.google.android.gms` to ensure proper package visibility.

### [app]

#### [MODIFY] [build.gradle.kts](file:///C:/Users/zheny/Documents/my/projects/workin/Medications_Tracker_Main/app/build.gradle.kts)
- Remove `implementation(libs.com.google.gms.google.services.gradle.plugin)` from `dependencies`.
- Remove `implementation(libs.com.google.relay.gradle.plugin)` from `dependencies`.

## Verification Plan

### Automated Tests
- Run `./gradlew :medicationstracker:assembleDebug` to ensure the project builds correctly after dependency cleanup.
- Run unit tests in `medicationstracker` to ensure no regressions in auth logic.

### Manual Verification
- Deploy the app to a device/emulator and verify that the "Failed to get service from broker" error no longer appears during login.
