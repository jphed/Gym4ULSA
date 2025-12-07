package com.ULSACUU.gym4ULSA.login.model.dto

/*
 Este archivo define los "Data Transfer Objects" (DTOs),
 es decir, las estructuras de datos que se usan para
 enviar y recibir información entre la app y el servidor
 en el flujo de autenticación (login).
*/

/*
 Petición de login

 Representa el cuerpo (body) de la petición HTTP POST
 que se envía al servidor para iniciar sesión.

 El backend espera recibir el email y la contraseña
 del usuario en formato JSON.

 Ejemplo JSON enviado:
 {
   "email": "jorge.romo@example.com",
   "password": "123456"
 }
*/
data class LoginRequest(
    val email: String,
    val password: String
)

/*
 Petición de registro

 Representa el cuerpo (body) de la petición HTTP POST
 que se envía al servidor para registrar un nuevo usuario.

 El backend espera recibir todos los campos del usuario
 en formato JSON.

 Ejemplo JSON enviado:
 {
   "name": "Jorge Parra",
   "email": "jorge@example.com",
   "password": "1234",
   "age": "25",
   "sex": "male",
   "height_cm": 179,
   "weight_kg": 82,
   "goal": {
     "type": "deficit",
     "target_weight_kg": "76",
     "rate_per_week_kg": "0.5"
   },
   "activity_level": ""
 }
*/
data class SignupRequest(
    val name: String,
    val email: String,
    val password: String,
    val age: String,
    val sex: String,
    val height_cm: String,
    val weight_kg: String,
    val goal: Goal,
    val activity_level: String,
    val experiencia: String,
    val created_utc: String = java.time.Instant.now().toString()
)

/*
 Respuesta de login

 Representa la respuesta que el servidor devuelve
 al intentar iniciar sesión.

 El backend devuelve un objeto con token y usuario
 cuando las credenciales son correctas.

 Ejemplo JSON recibido (éxito):
 {
   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
   "user": {
     "id": "6934a17755db886a529a4980",
     "role": "USER",
     "iat": 1736530743,
     "exp": 1765735543
   }
 }
*/
data class LoginResponse(
    val token: String,
    val user: UserResponse
)

/*
 Usuario en respuesta de login

 Representa el usuario que viene dentro de la respuesta
 del backend después del login exitoso.
*/
data class UserResponse(
    val id: String,
    val role: String,
    val iat: Long,
    val exp: Long
)

/*
 Modelo de objetivo del usuario

 Representa las metas de fitness del usuario
 como tipo de objetivo, peso objetivo y tasa
 de pérdida/ganancia de peso por semana.
*/
data class Goal(
    val type: String,
    val target_weight_kg: String,
    val rate_per_week_kg: String
)

/*
 Respuesta de registro

 Representa la respuesta que el servidor devuelve
 al intentar registrar un nuevo usuario.

 El backend devuelve la misma estructura que el login:
 token y usuario cuando el registro es exitoso.

 Ejemplo JSON recibido (éxito):
 {
   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
   "user": {
     "id": "6934a17755db886a529a4980",
     "role": "USER",
     "iat": 1736530743,
     "exp": 1765735543
   }
 }
*/
data class SignupResponse(
    val token: String,
    val user: UserResponse
)

/*
 Modelo de usuario

 Representa los datos básicos de un usuario en el sistema.
 Estos datos viajan dentro de la respuesta de login.

 - id: identificador único en la base de datos del backend.
 - name: nombre del usuario.
 - email: correo electrónico del usuario.
*/
data class User(
    val id: String,
    val name: String,
    val email: String
)