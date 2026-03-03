package com.example.notasxml.ViewModels

import Modelos.Persona
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notasxml.API.Retrofit
import Modelos.PersonaLogin // Asegúrate de importar tu modelo
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val usuariosService = Retrofit.usuariosRetrofit

    private val _usuarioLogeado = MutableLiveData<Persona?>()
    val usuarioLogeado: LiveData<Persona?> get() = _usuarioLogeado

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun loginUser(nombre: String, pass: String) {
        viewModelScope.launch {
            try {
                val datosLogin = PersonaLogin(nombre = nombre, password = pass)
                val response = usuariosService.login(datosLogin)

                if (response.isSuccessful && response.body() != null) {
                    _usuarioLogeado.value = response.body()
                } else {
                    val codigo = response.code()
                    val errorCuerpo = response.errorBody()?.string()

                    _errorMessage.value = "Error $codigo: $errorCuerpo"

                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            }
        }
    }
}