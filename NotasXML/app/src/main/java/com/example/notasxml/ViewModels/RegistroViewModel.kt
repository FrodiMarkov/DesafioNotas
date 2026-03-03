package com.example.notasxml.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notasxml.API.Retrofit
import Modelos.Persona
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {

    private val usuariosService = Retrofit.usuariosRetrofit

    private val _registroExitoso = MutableLiveData<Boolean>()
    val registroExitoso: LiveData<Boolean> get() = _registroExitoso

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun registrarUsuario(persona: Persona) {
        viewModelScope.launch {
            try {
                val response = usuariosService.insertar(persona)
                if (response.isSuccessful) {
                    _registroExitoso.value = true
                } else {
                    _error.value = "Error al registrar: Código ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
            }
        }
    }
    fun actualizarUsuario(persona : Persona) {
        viewModelScope.launch {
            try {
                val response = usuariosService.actualizar(persona.dni, persona)
                if (response.isSuccessful) {
                    _registroExitoso.value = true
                } else {
                    _error.value = "Error al actualizar: Código ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
            }
        }
    }
}