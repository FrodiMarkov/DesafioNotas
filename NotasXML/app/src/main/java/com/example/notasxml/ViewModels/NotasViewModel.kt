package com.example.notasxml.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Modelos.ItemTarea
import com.example.Modelos.Nota
import com.example.Modelos.NotaRequest
import com.example.Modelos.NotaConItems
import com.example.notasxml.API.Retrofit
import com.example.notasxml.Helpers.UsuarioHolder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotasViewModel : ViewModel() {

    private val notasService = Retrofit.notasRetrofit

    private val _guardadoExitoso = MutableLiveData<Boolean>()
    val guardadoExitoso: LiveData<Boolean> = _guardadoExitoso

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    private val _notas = MutableLiveData<List<NotaConItems>>()
    val notas: LiveData<List<NotaConItems>> = _notas

    fun guardarNota(titulo: String, descripcion: String, tipo: String, items: List<ItemTarea>, idTrabajador: Int?) {
        _guardadoExitoso.value = false
        viewModelScope.launch {
            try {
                val esTarea = tipo.equals("Tarea", ignoreCase = true)
                val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                val notaObj = Nota(
                    id = null,
                    titulo = titulo,
                    descripcion = descripcion,
                    tipo = tipo,
                    cargatrabajo = if (esTarea) 5 else 0,
                    id_trabajador = idTrabajador ?: 0,
                    fecha = fechaActual
                )

                val request = NotaRequest(nota = notaObj, items = items)
                val response = notasService.insertarAIdEspecifico(request)

                if (response.isSuccessful || response.code() == 409) {
                    _guardadoExitoso.postValue(true)
                } else {
                    _error.postValue("Error servidor: ${response.code()}")
                }
            } catch (e: Exception) {
                _guardadoExitoso.postValue(true)
            }
        }
    }

    fun cargarNotasDelUsuario(idUsuario: Int) {
        viewModelScope.launch {
            try {
                val response = notasService.obtenerPorUsuario(idUsuario)
                if (response.isSuccessful) {
                    _notas.postValue(response.body() ?: emptyList())
                } else {
                    _error.postValue("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _error.postValue("Fallo de red al cargar")
            }
        }
    }
    fun borrarNota(id: Int, idUsuario:Int) {
        viewModelScope.launch {
            try {
                val response = notasService.borrar(id)
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

    fun actualizarNota(id: Int, titulo: String, descripcion: String, tipo: String, items: List<ItemTarea>) {
        _guardadoExitoso.value = false
        viewModelScope.launch {
            try {
                val notaObj = Nota(
                    id = id,
                    titulo = titulo,
                    descripcion = descripcion,
                    tipo = tipo,
                    cargatrabajo = if (tipo.equals("Tarea", ignoreCase = true)) items.size else 0,
                    id_trabajador = UsuarioHolder.usuario.id,
                    fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                )

                val request = NotaRequest(nota = notaObj, items = items)
                val response = notasService.actualizar(id, request)

                if (response.isSuccessful) {
                    _guardadoExitoso.postValue(true)
                } else {
                    _error.postValue("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _error.postValue("Error de conexión")
            }
        }
    }
}