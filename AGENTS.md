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

## Validation Performed
- Built successfully with:
- `./gradlew :android:assembleDebug`
- Installed to connected physical Android device:
- `adb install -r android/build/outputs/apk/debug/android-debug.apk`
- Launched app from ADB and checked logcat for startup/runtime fatals.
- Confirmed no fatal exceptions on startup after migration.

## Result
- `Minimal-Jezz` now builds and runs with modern Android/libGDX toolchain.
- Legacy billing and old ad implementation are removed.
- Modern banner/interstitial ad flow is in place and integrated with gameplay states.
