# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

Repository layout note
- The Android project root is Gym4ULSA/ (nested). Use the Gradle wrapper located there when running commands from the repo root.

Common commands (PowerShell on Windows)
- Build (Debug):
  - .\Gym4ULSA\gradlew.bat assembleDebug
- Install on device/emulator (Debug):
  - .\Gym4ULSA\gradlew.bat installDebug
- Clean build:
  - .\Gym4ULSA\gradlew.bat clean
- Lint (Android Lint):
  - All variants: .\Gym4ULSA\gradlew.bat lint
  - Debug only: .\Gym4ULSA\gradlew.bat lintDebug
- Unit tests:
  - All: .\Gym4ULSA\gradlew.bat test
  - Debug unit tests: .\Gym4ULSA\gradlew.bat testDebugUnitTest
  - Single unit test (example):
    - .\Gym4ULSA\gradlew.bat testDebugUnitTest --tests "com.ULSACUU.gym4ULSA.ExampleUnitTest"
- Instrumented tests (requires running emulator or connected device):
  - All: .\Gym4ULSA\gradlew.bat connectedAndroidTest
  - Single instrumented test class (example):
    - .\Gym4ULSA\gradlew.bat connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.ULSACUU.gym4ULSA.ExampleInstrumentedTest

What this project is
- Android app written in Kotlin using Jetpack Compose and the MVVM pattern.
- Single module: app.
- SDKs: minSdk 24, target/compileSdk 35. Kotlin/JVM target 11.
- Uses Gradle Version Catalogs (gradle/libs.versions.toml) to manage dependency versions.

High‑level architecture and flow
- Entry point: com.ULSACUU.gym4ULSA.utils.SplashActivity (declared as LAUNCHER in AndroidManifest). This shows a splash and hands off to MainActivity.
- MainActivity (Compose):
  - Wraps UI in AndroidClassMP1Theme.
  - Observes a DataStore boolean flag (onboardingDone) via DataStoreManager.
  - Decides initial screen based on onboardingDone:
    - null: shows a simple SplashLoader (CircularProgressIndicator) while DataStore loads
    - false: shows OnboardingView (from routine.onboarding.views) and, on finish, sets onboardingDone=true in DataStore
    - true: shows TabBarNavigationView (main app shell)
- Navigation:
  - ScreenNavigation: sealed class enumerating routes: Home, Rutina (Routine), Nutrition (placeholder), Ajustes (Settings), Perfil, Login, Onboarding.
  - TabBarNavigationView:
    - Scaffold with CenterAlignedTopAppBar, a central FloatingActionButton, and a bottom NavigationBar.
    - Hides top/bottom bars on Login and Onboarding routes.
    - NavHost startDestination is Login. Routes include Home, Rutina, Nutrition (placeholder text), Ajustes, Perfil, Login, Onboarding.
- State & ViewModels:
  - Uses ViewModel integration for Compose screens (e.g., OnboardingViewModel via viewModel()).
- Data storage & security:
  - DataStoreManager: Preferences DataStore for simple app state (e.g., onboardingDone).
  - CredentialsStore backed by androidx.security:security-crypto for secure storage of credentials.

Build system and dependencies
- Gradle (Kotlin DSL). Top-level build.gradle.kts applies Android and Kotlin plugins via version catalogs aliases.
- Version catalog (gradle/libs.versions.toml) pins key versions, including:
  - agp 8.8.1, kotlin 2.0.0, compose BOM 2024.04.01
- Notable runtime/test deps (see Gym4ULSA/app/build.gradle.kts):
  - Compose UI, Material3, Navigation Compose (2.7.7), DataStore (1.1.1)
  - Lifecycle ViewModel for Compose (2.8.4)
  - Retrofit (2.9.0) + Gson converter + OkHttp logging (4.9.3)
  - AndroidX test libs and Compose test artifacts

Module overview (app)
- Package roots under com.ULSACUU.gym4ULSA/ include:
  - home/, routine/, profile/, settings/ — primary feature areas
  - navigation/ — ScreenNavigation sealed class and TabBarNavigationView scaffold/NavHost
  - utils/ — DataStoreManager, SplashActivity, etc.
  - onboarding/ — Onboarding views and ViewModel used during first-run flow

Notes for Warp
- Run Gradle via .\Gym4ULSA\gradlew.bat from the repository root (this is usually more convenient than changing directories).
- Instrumented tests require an emulator or device. If none is connected, connectedAndroidTest will fail.
- Navigation labels mix English and Spanish; route constants are English tokens (e.g., "HomeRoute").
- The Nutrition route is a placeholder view at the time of writing.
