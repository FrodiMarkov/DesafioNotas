package com.example.notasxml.ViewModels

import Modelos.Persona
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notasxml.API.Retrofit
import kotlinx.coroutines.launch

class RegistroAdminViewModel : ViewModel() {
    private val usuariosService = Retrofit.usuariosRetrofit

    private val _registroExitoso = MutableLiveData<Boolean>()
    val registroExitoso: LiveData<Boolean> get() = _registroExitoso

    private val _mensajeError = MutableLiveData<String>()
    val mensajeError: LiveData<String> get() = _mensajeError

    fun registrarUsuario(persona: Persona) {
        viewModelScope.launch {
            try {
                val response = usuariosService.insertar(persona)
                if (response.isSuccessful) {
                    _registroExitoso.value = true
                } else {
                    _mensajeError.value = "Error al registrar: Código ${response.code()}"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error de red: ${e.message}"
            }
        }
    }
}