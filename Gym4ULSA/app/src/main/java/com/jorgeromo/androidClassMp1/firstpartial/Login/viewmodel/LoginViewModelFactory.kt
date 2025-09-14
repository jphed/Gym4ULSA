package com.jorgeromo.androidClassMp1.firstpartial.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jorgeromo.androidClassMp1.firstpartial.login.model.repository.AuthRepository

/*
 LoginViewModelFactory

 Esta clase implementa el patrón Factory para crear instancias
 de LoginViewModel con dependencias personalizadas.

 ¿Por qué se necesita?
 - Android solo sabe cómo crear un ViewModel vacío por defecto.
 - Si un ViewModel recibe parámetros en el constructor (como el repo),
   necesitamos un Factory que le indique al sistema cómo instanciarlo.

 Flujo:
 1. La vista (Compose/Activity) pide un LoginViewModel con viewModel().
 2. Se pasa LoginViewModelFactory(repo) como parámetro.
 3. El Factory recibe el repo y construye el LoginViewModel(repo).
 4. El sistema conserva el ViewModel entre recomposiciones y cambios
    de configuración (rotación de pantalla, etc.).
*/
class LoginViewModelFactory(
    private val repo: AuthRepository
) : ViewModelProvider.Factory {

    /*
     create(modelClass)

     Método que se llama cuando el sistema necesita una instancia
     del ViewModel. Aquí devolvemos LoginViewModel(repo).

     El cast (as T) es seguro porque solo se usa para crear este ViewModel.
    */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(repo) as T
    }
}