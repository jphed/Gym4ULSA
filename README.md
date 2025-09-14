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

- **Platform**: Android
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 35 (Android 15)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)

### Dependencies

- **Jetpack Compose BOM**: 2024.04.01
- **Navigation Compose**: 2.7.7
- **DataStore Preferences**: 1.1.1
- **Lifecycle ViewModel Compose**: 2.8.4
- **Retrofit**: 2.9.0 (for networking)
- **Material Icons Extended**: Latest

### Key Technologies

- **Jetpack Compose**: Modern declarative UI toolkit
- **Navigation Component**: Type-safe navigation between screens
- **DataStore**: Modern data storage solution
- **Material Design 3**: Latest design system implementation
- **Kotlin Coroutines**: Asynchronous programming

## Project Structure

```
app/
├── src/main/
│   ├── java/com/jorgeromo/androidClassMp1/
│   │   ├── firstpartial/          # Onboarding and main features
│   │   │   └── onboarding/        # Onboarding screens and logic
│   │   ├── ids/                   # Utility features
│   │   │   ├── imc/              # BMI calculator
│   │   │   ├── temperature/       # Temperature converter
│   │   │   ├── student/          # Student management
│   │   │   ├── location/         # Location services
│   │   │   ├── login/            # Authentication
│   │   │   └── sum/              # Mathematical calculator
│   │   ├── navigation/            # Navigation components
│   │   ├── ui/theme/             # App theming and colors
│   │   └── utils/                # Utility classes
│   └── res/
│       ├── values/               # Spanish strings (default)
│       ├── values-en/            # English strings
│       ├── values-es/            # Spanish strings
│       └── drawable/             # Images and icons
```

## Installation

### Prerequisites

- Android Studio Hedgehog or later
- JDK 11 or later
- Android SDK with API 24+ installed
- Git

### Build Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/jphed/Gym4ULSA.git
   cd Gym4ULSA
   ```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Build the project:
   ```bash
   ./gradlew assembleDebug
   ```

5. Install on device or emulator:
   ```bash
   ./gradlew installDebug
   ```

## Usage

### Getting Started

1. Launch the application
2. Complete the interactive onboarding by swiping through the screens
3. Navigate through the main interface using the bottom navigation bar
4. Access various tools and calculators from the main menu

### Main Features

- **Home**: Access to all utility tools and calculators
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
