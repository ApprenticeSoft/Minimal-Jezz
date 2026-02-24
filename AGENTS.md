# AGENTS.md

This file summarizes the modernization and optimization pass applied to `Minimal-Jezz`.

## Scope
- Revive the Android build for current devices and Play requirements.
- Replace legacy ads/billing implementation with modern ad integration.
- Improve runtime stability and core code quality.
- Keep gameplay behavior while reducing technical debt.

## Build and Toolchain Modernization
- Upgraded Gradle wrapper to `8.10.2`.
- Migrated root build scripts to modern Gradle/AGP style.
- Upgraded Android Gradle Plugin to `8.8.2`.
- Upgraded libGDX dependencies to `1.14.0`.
- Updated project to Android + Core modules (desktop removed from active build graph).
- Updated Java toolchain to Java 17 for `core` and Android compile options.
- Updated Gradle properties for modern Android builds and parallel execution.

## Android Compatibility Updates
- Updated Android module config:
- `compileSdk 35`
- `targetSdk 35`
- `minSdk 23`
- Added modern manifest attributes and exported launcher activity.
- Removed deprecated AdActivity manifest entry.
- Added AdMob application metadata string (`admob_app_id`).
- Removed obsolete billing permission and legacy in-app billing AIDL/util source set.

## Native Runtime Packaging Fixes
- Added `natives` configuration and ABI extraction task.
- Added `copyAndroidNatives` task for:
- `arm64-v8a`
- `armeabi-v7a`
- `x86_64`
- Wired `preBuild` to native extraction task.
- Added `jniLibs` source dir and ignored generated ABI folders in `.gitignore`.

## Ads Migration
- Replaced legacy ad logic with modern Google Mobile Ads API.
- Implemented anchored adaptive banners with dynamic width refresh on layout/config changes.
- Implemented interstitial preload and break-based show policy with cooldown.
- Added new `ActionResolver` API:
- `preloadInterstitial()`
- `onNaturalBreak()`
- `showBanner()`
- `hideBanner()`
- `getBannerHeightPx()`

## Core Runtime Refactor
- Removed per-frame ad polling logic from `MyGdxGame`.
- Integrated ad behavior at screen/gameplay level.
- Updated startup loading/menu/game screens to show/hide banner intentionally.
- Trigger interstitial only on natural breaks (win/loss) in gameplay.
- Added listener binding guards on screens to avoid duplicate listener registration.

## Gameplay/Code Optimizations
- Converted legacy global state identifiers to ASCII-safe names in `Variables`.
- Cleaned `LevelHandler` parsing and removed static mutable world/camera references.
- Updated `Balle` impulse usage to scalar overload to reduce allocations.
- Fixed string comparisons in `Barre` (`equals` instead of `==`).
- Added new `pinchStop()` implementation required by current libGDX `GestureListener`.
- Removed menu social-button integration from `MainMenuScreen` to match the prior project pass.

## Additional Gameplay Tuning (Post-Revival)
- Ball speed was reduced by 50% at runtime (`Balle` speed cap scaling) so all levels remain playable on modern high-refresh devices.
- Added the same custom pause button style used in `Minimal-Brick-Breaker-Free`:
- Transparent square with white border and `"II"` label.
- Pressed state keeps a subtle dark fill.
- Button size set to the Brick Breaker ratio (`(screenWidth / 16) * 1.3`) and anchored top-right with margin.
- Added pause-button-specific logic in `GameScreen`:
- Visibility only during active gameplay.
- Tap toggles pause/resume using existing `TablesJeu` pause flows.
- Back key now uses the same pause toggle path during gameplay.
- Pause button bounds are recalculated on resize.
- Pause button textures are explicitly disposed to avoid texture leaks.
- Removed the startup cross-promo modal ("Try our new game !") from `MainMenuScreen`.
- Updated Level Cleared UI in `TablesJeu`:
- Secondary button text changed from Replay to Menu.
- Secondary button now routes to `MainMenuScreen` instead of restarting the current level.
- Updated Pause UI in `TablesJeu`:
- Removed the Restart button from the pause menu.
- Pause actions are now `Resume -> Menu -> Quit` with consistent row spacing and no empty gap.
- Added a captured-zone render overdraw in `GameScreen`:
- Each filled exclusion surface is expanded by 1px on all sides (with floor/ceil alignment) to remove visible HiDPI seams between adjacent zones.
- Reduced runtime ball speed again by 10%:
- `Balle` speed scale updated from `0.50` to `0.45`.
- Updated Level Selection back-button placement in `NiveauxScreen`:
- Bottom-left back button now uses ad-safe Y bounds based on banner height plus margin.
- Added a fallback minimum Y floor (`12%` of screen height) for cases where banner height reports late/zero.

## Validation Performed
- Built successfully with:
- `./gradlew :android:assembleDebug`
- Installed to connected physical Android device:
- `adb install -r android/build/outputs/apk/debug/android-debug.apk`
- Launched app from ADB and checked logcat for startup/runtime fatals.
- Confirmed no fatal exceptions on startup after migration.
- Rebuilt after the gameplay/pause-button update:
- `./gradlew :android:assembleDebug`
- APK reinstall succeeded.
- ADB launch/logcat smoke test was blocked by device authorization reset (`device unauthorized`) after daemon restart; runtime crash scan needs re-authorization on device.
- Rebuilt after menu/popup update:
- `./gradlew :android:assembleDebug`
- Installed to device with `adb install -r` (`Success`).
- Launched with `adb shell am start -n com.minimal.jezz.android/.AndroidLauncher`.
- Rebuilt after pause-menu cleanup:
- `./gradlew :android:assembleDebug`
- Installed to device with `adb install -r` (`Success`).
- Launched with `adb shell am start -n com.minimal.jezz.android/.AndroidLauncher`.
- Rebuilt after seam/speed/back-button updates:
- `./gradlew :android:assembleDebug`
- Installed to device with `adb install -r` (`Success`).
- Relaunched with `adb shell am force-stop` + `adb shell am start -n com.minimal.jezz.android/.AndroidLauncher`.

## Result
- `Minimal-Jezz` now builds and runs with modern Android/libGDX toolchain.
- Legacy billing and old ad implementation are removed.
- Modern banner/interstitial ad flow is in place and integrated with gameplay states.
