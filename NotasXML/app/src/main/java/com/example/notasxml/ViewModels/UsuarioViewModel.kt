package com.example.notasxml.ViewModels

import Modelos.Persona
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notasxml.API.Retrofit
import kotlinx.coroutines.launch

class UsuarioViewModel : ViewModel() {
    private val usuariosService = Retrofit.usuariosRetrofit

    private val _operacionExitosa = MutableLiveData<Boolean>()
    val operacionExitosa: LiveData<Boolean> get() = _operacionExitosa

    private val _usuarios = MutableLiveData<List<Persona>>()
    val usuarios: LiveData<List<Persona>> get() = _usuarios

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Ahora la función se llama manualmente
    fun obtenerUsuarios() {
        viewModelScope.launch {
            try {
                val response = usuariosService.obtenerTodos()

                if (response.isSuccessful && response.body() != null) {
                    _usuarios.value = response.body()
                } else {
                    val errorCuerpo = response.errorBody()?.string()
                    _errorMessage.value = "Error ${response.code()}: $errorCuerpo"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            }
        }
    }

    fun deletePersona(dni: String) {
        viewModelScope.launch {
            try {
                val respuesta = usuariosService.eliminar(dni)

                if (respuesta.isSuccessful) {
                    obtenerUsuarios()
                } else {
                    val errorCuerpo = respuesta.errorBody()?.string()
                    _errorMessage.value = "Error al eliminar (${respuesta.code()}): $errorCuerpo"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión al borrar: ${e.message}"
            }
        }
    }

    fun cambiarImagen(persona: Persona) {
        viewModelScope.launch {
            try {
                val response = usuariosService.actualizar(persona.dni, persona)
                if (response.isSuccessful) {
                    _operacionExitosa.value = true
                } else {
                    _errorMessage.value = "Error al cambiar imagen: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de red: ${e.message}"
            }
        }
    }

    fun cambiarPass(persona: Persona) {
        viewModelScope.launch {
            try {
                val response = usuariosService.actualizar(persona.dni, persona)
                if (response.isSuccessful) {
                    _operacionExitosa.value = true
                } else {
                    _errorMessage.value = "Error al cambiar contraseña: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de red: ${e.message}"
            }
        }
    }
}