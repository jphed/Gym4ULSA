package com.jorgeromo.androidClassMp1.firstpartial.login.model.dto

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
 Respuesta de login

 Representa la respuesta que el servidor devuelve
 al intentar iniciar sesión.

 Contiene:
 - success: indica si el login fue exitoso o no.
 - message: mensaje del servidor (ej. "Inicio de sesión exitoso" o "Contraseña incorrecta").
 - token: JWT o token de autenticación que se usará en llamadas posteriores.
 - user: datos del usuario autenticado (si el login fue exitoso).

 Ejemplo JSON recibido:
 {
   "success": true,
   "message": "Inicio de sesión exitoso",
   "token": "eyJhbGciOiJIUzI1NiIsInR...",
   "user": {
       "id": "68b9d1d45a6a11ee9c6914f7",
       "name": "Jorge Romo",
       "email": "jorge.romo@example.com"
   }
 }
*/
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: User? = null
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