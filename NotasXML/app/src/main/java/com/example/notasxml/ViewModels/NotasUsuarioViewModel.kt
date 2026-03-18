package com.example.notasxml.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Modelos.NotaConItems
import com.example.notasxml.API.Retrofit
import kotlinx.coroutines.launch

class NotasUsuarioViewModel : ViewModel() {

    private val serviceNotas = Retrofit.notasRetrofit

    private val _notas = MutableLiveData<List<NotaConItems>>()
    val notas: LiveData<List<NotaConItems>> = _notas

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    fun cargarNotasDelUsuario(idUsuario: Int) {
        viewModelScope.launch {
            try {
                val response = serviceNotas.obtenerPorUsuario(idUsuario)
                if (response.isSuccessful) {
                    _notas.postValue(response.body() ?: emptyList())
                }
            } catch (e: Exception) {
                _error.postValue("Fallo de red: ${e.message}")
            }
        }
    }

    fun borrarNota(id: Int, idUsuario:Int) {
        viewModelScope.launch {
            try {
                val response = serviceNotas.borrar(id)
                if (response.isSuccessful) {
                    cargarNotasDelUsuario(idUsuario)
                } else {
                    _error.postValue("No se pudo eliminar el elemento")
                }
            } catch (e: Exception) {
                _error.postValue("Error al intentar borrar: ${e.message}")
            }
        }
    }
}