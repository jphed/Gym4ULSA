# GYM4ULSA

Apliación integral de fitness y utilidades diseñada para la comunidad de la Universidad La Salle (ULSA), construida con prácticas modernas de desarrollo Android usando Jetpack Compose.

## Descripción general

GYM4ULSA es una aplicación Android multifuncional que combina seguimiento de actividad física, calculadoras de salud y utilidades educativas en una sola interfaz fácil de usar. La app cuenta con un diseño moderno e internacionalizado con soporte para idiomas español e inglés.

## Características

### Funcionalidad principal

- **Onboarding interactivo**: Experiencia moderna de introducción basada en deslizamientos, con imágenes de fondo a pantalla completa y superposiciones en degradado
- **Soporte multiidioma**: Internacionalización completa con soporte para español (predeterminado) e inglés
- **UI/UX moderna**: Diseño futurista con elementos animados, efectos de degradado y componentes de Material Design 3

### Herramientas de salud y fitness

- **Calculadora de IMC**: Calcula el Índice de Masa Corporal a partir del peso y la estatura
- **Convertidor de temperatura**: Conversión entre grados Celsius y Fahrenheit
- **Calculadora matemática**: Operaciones aritméticas básicas y cálculos generales

### Funcionalidades educativas

- **Gestión de estudiantes**: Registro y administración de información de estudiantes
- **Servicios de localización**: Visualización y gestión de datos de ubicación
- **Sistema de login**: Autenticación segura de usuarios

### Características técnicas

- **Navegación por gestos (swipe)**: Navegación intuitiva basada en gestos en toda la aplicación
- **Persistencia de datos**: Almacenamiento local mediante DataStore (preferencias)
- **Arquitectura moderna**: Patrón MVVM con ViewModels y manejo de estado en Compose
- **Diseño responsivo**: Optimizada para diferentes tamaños y orientaciones de pantalla

## Especificaciones técnicas

### Entorno de desarrollo
- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose, Material 3
- **Arquitectura**: MVVM
- **Navegación**: Navigation Compose
- **Datos**: DataStore (preferencias)
- **Red**: Retrofit (API de autenticación)
- **Min/Target SDK**: 24 / 35

## Estructura del proyecto (alto nivel)
```
app/
  src/main/
    java/com/ULSACUU/gym4ULSA/
      navigation/            # NavHost, rutas, bottom bar
      login/                 # Pantalla de login + ViewModel
      home/                  # Pantalla principal y detalle de ejercicios
      nutrition/             # Vista de nutrición
      profile/               # ProfileView (antes Perfil)
      routine/               # Vista de rutina (Rutina)
      settings/              # SettingsView (antes Ajustes)
      onboarding/            # Flujo de onboarding
      qr/                    # Lector de códigos QR
      utils/                 # DataStore, credenciales, splash, etc.
    res/
      values/                # Strings por defecto (ES)
      values-en/             # Strings en inglés
      values-es/             # Strings en español
```

## Primeros pasos
1. Instala Android Studio (Hedgehog o superior) y el Android SDK (API 24+).
2. Abre la carpeta del proyecto `Gym4ULSA` en Android Studio.
3. Espera a que Gradle sincronice. Usa JDK 17 (el incluido en Android Studio es suficiente).
4. Ejecuta la app en un emulador o dispositivo físico.

Compilación por línea de comandos:
- **Routine**: Funcionalidades relacionadas con fitness y rutinas de ejercicio
- **Profile**: Perfil de usuario y ajustes relacionados
- **Settings**: Configuración general de la aplicación y preferencias

### Soporte de idiomas

La aplicación detecta automáticamente el idioma del dispositivo y muestra el contenido en español (predeterminado) o inglés. El idioma puede modificarse desde la configuración del dispositivo.

## Desarrollo

### Estilo de código

- Sigue las convenciones oficiales de Kotlin
- Utiliza nombres significativos para variables y funciones
- Implementa un manejo adecuado de errores
- Mantén un formato de código consistente

### Guías de arquitectura

- Utiliza el patrón MVVM para los componentes de pantalla
- Mantén una adecuada separación de responsabilidades (separation of concerns)
- Usa buenas prácticas de manejo de estado en Compose
- Sigue las guías de diseño de Material Design

### Pruebas

Para ejecutar el conjunto de pruebas:
```bash
./gradlew test
```

Para ejecutar pruebas instrumentadas en dispositivo/emulador:
```bash
./gradlew connectedAndroidTest
```

## Contribuciones

1. Haz un fork del repositorio
2. Crea una rama de funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. Realiza tus cambios y haz commit (`git commit -m 'Agregar nueva funcionalidad'`)
4. Haz push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## Historial de versiones

### Versión 1.0
- Versión inicial
- Sistema de onboarding completo con internacionalización
- Calculadora de IMC y conversor de temperatura
- Gestión de estudiantes y servicios de localización
- Interfaz moderna con Material Design 3
- Navegación basada en gestos (swipe)

## Licencia

Este proyecto se desarrolla con fines educativos en la Universidad La Salle (ULSA). Todos los derechos reservados.

## Contacto

Para dudas o soporte relacionado con esta aplicación, por favor contacta al equipo de desarrollo.

---

**GYM4ULSA** - Potenciando el fitness y la educación a través de la tecnología

