package com.example.notasxml.ViewModels

import Modelos.Persona
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Modelos.NotaRequest
import com.example.notasxml.API.Retrofit
import kotlinx.coroutines.launch

class AgregarTareaViewModel : ViewModel() {

    private val serviceUsuarios = Retrofit.usuariosRetrofit
    private val serviceNotas = Retrofit.notasRetrofit

    private val _usuarios = MutableLiveData<List<Persona>>()
    val usuarios: LiveData<List<Persona>> = _usuarios

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun cargarUsuarios() {
        viewModelScope.launch {
            try {
                val response = serviceUsuarios.obtenerTodos()
                if (response.isSuccessful) {
                    _usuarios.postValue(response.body() ?: emptyList())
                }
            } catch (e: Exception) {
                _error.postValue("Error cargando usuarios: ${e.message}")
            }
        }
    }

    fun crearTareaRandom(request: NotaRequest) {
        viewModelScope.launch {
            try {
                val response = serviceNotas.insertarRandom(request)
                if (!response.isSuccessful) _error.postValue("Error al guardar random")
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }

    fun crearTareaEspecifica(request: NotaRequest) {
        viewModelScope.launch {
            try {
                val response = serviceNotas.insertarAIdEspecifico(request)
                if (!response.isSuccessful) _error.postValue("Error al guardar usuario específico")
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }
}