# GYM4ULSA

A comprehensive fitness and utility application designed for the Universidad La Salle (ULSA) community, built with modern Android development practices using Jetpack Compose.

## Overview

GYM4ULSA is a multi-functional Android application that combines fitness tracking, health calculators, and educational utilities in a single, user-friendly interface. The app features a modern, internationalized design with support for both Spanish and English languages.

## Features

### Core Functionality

- **Interactive Onboarding**: Modern swipe-based onboarding experience with full-screen background images and gradient overlays
- **Multi-language Support**: Complete internationalization with Spanish (default) and English language support
- **Modern UI/UX**: Futuristic design with animated elements, gradient effects, and Material Design 3 components

### Health & Fitness Tools

- **BMI Calculator**: Calculate Body Mass Index with weight and height inputs
- **Temperature Converter**: Convert between Celsius and Fahrenheit temperatures
- **Mathematical Calculator**: Basic arithmetic operations and calculations

### Educational Features

- **Student Management**: Track and manage student information
- **Location Services**: View and manage location data
- **Login System**: Secure user authentication

### Technical Features

- **Swipe Navigation**: Intuitive gesture-based navigation throughout the app
- **Data Persistence**: Local data storage using DataStore preferences
- **Modern Architecture**: MVVM pattern with ViewModels and Compose state management
- **Responsive Design**: Optimized for various screen sizes and orientations

## Technical Specifications

### Development Environment
- **Language**: Kotlin
- **UI**: Jetpack Compose, Material 3
- **Architecture**: MVVM
- **Navigation**: Navigation Compose
- **Data**: DataStore (preferences)
- **Network**: Retrofit (Auth API)
- **Min/Target SDK**: 24 / 35

## Project structure (high level)
```
app/
  src/main/
    java/com/ULSACUU/gym4ULSA/
      navigation/            # NavHost, routes, bottom bar
      login/                 # Login screen + ViewModel
      home/                  # Home and exercise details
      nutrition/             # Nutrition view
      profile/               # ProfileView (formerly Perfil)
      routine/               # Routine view (Rutina)
      settings/              # SettingsView (formerly Ajustes)
      onboarding/            # Onboarding flow
      qr/                    # QR scanner
      utils/                 # DataStore, credentials, splash, etc.
    res/
      values/                # Default strings (ES)
      values-en/             # English strings
      values-es/             # Spanish strings
```

## Getting started
1. Install Android Studio (Hedgehog or later) and Android SDK (API 24+).
2. Open the project folder `Gym4ULSA` in Android Studio.
3. Let Gradle sync. Use JDK 17 (bundled is fine).
4. Run on an emulator or device.

Command line builds:
- **Routine**: Fitness and workout related features
- **Profile**: User profile and settings
- **Settings**: Application configuration and preferences

### Language Support

The application automatically detects the device language and displays content in Spanish (default) or English. Language can be changed through the device settings.

## Development

### Code Style

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Implement proper error handling
- Maintain consistent code formatting

### Architecture Guidelines

- Use MVVM pattern for screen components
- Implement proper separation of concerns
- Use Compose state management best practices
- Follow Material Design guidelines

### Testing

Run the test suite:
```bash
./gradlew test
```

Run instrumented tests:
```bash
./gradlew connectedAndroidTest
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Version History

### Version 1.0
- Initial release
- Complete onboarding system with internationalization
- BMI calculator and temperature converter
- Student management and location services
- Modern UI with Material Design 3
- Swipe-based navigation

## License

This project is developed for educational purposes at Universidad La Salle (ULSA). All rights reserved.

## Contact

For questions or support regarding this application, please contact the development team.

---

**GYM4ULSA** - Empowering fitness and education through technology
