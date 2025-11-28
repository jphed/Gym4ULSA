# Evidencias – José Holguín

## Mis aportes para Gym4ULSA

Mis aportes al proyecto **Gym4ULSA** fueron variados. Mi contribución principal fue:

- Mejora de la pantalla de **Perfil**.  
- Eliminación del **Toast** del *Onboarding*.  
- Creación de la pantalla de **Términos y Condiciones**.  
- Cambios en la pantalla de **Login**.

En la pantalla de Login pude:
- Modificar el comportamiento del **teclado**, ya que antes no se cerraba.  
  Ahora, si el usuario toca fuera del teclado, este se oculta de inmediato.  
- **Evitar que la pantalla gire**, manteniéndola siempre en orientación vertical.

---

## ¿Qué aprendí?

Aprendí que existen **dos formas** de mantener una pantalla fija en orientación vertical.  
En mi caso, la mejor opción fue hacerlo **solo en las pantallas necesarias**, ya que bloquear toda la app podría limitar funcionalidades a futuro.

Para lograrlo utilicé el paquete `ActivityInfo`, específicamente:

```kotlin
ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
```

## Dificultades encontradas

En cuanto a dificultades, más bien surgieron por un cambio que hizo otro compañero.  
Hubo cambios en el `.gitignore` y no sé qué otras cosas movió mi compañero Jorge, pero eso ocasionó que **ya no pudiera tener la versión más actual**, incluso usando `git pull`.

Lo que tuve que hacer fue:

- Borrar mi contenido local.  
- Clonar el proyecto de nuevo.

Después de eso, no tuve más problemas.

---

## Plan para la entrega final

En la entrega final quiero desarrollar:

- La **encuesta** que se le hace a un usuario nuevo, para determinar su nivel de conocimiento.  
- Ver cómo **agregar la información de las rutinas** usando la API REST que tenemos en mente.  
- Estar abierto a toda **retroalimentación** de mis compañeros y profesores.

---
