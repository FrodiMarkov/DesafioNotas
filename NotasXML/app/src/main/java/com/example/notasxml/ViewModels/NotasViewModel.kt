package com.example.notasxml.ViewModels

import Modelos.Persona
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Modelos.ItemTarea
import com.example.Modelos.Nota
import com.example.Modelos.NotaRequest
import com.example.notasxml.API.NotasAPI
import com.example.notasxml.API.UsuariosAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotasViewModel : ViewModel() {

    private lateinit var notasService: NotasAPI
    private lateinit var usuariosService: UsuariosAPI

    private val _usuarios = MutableStateFlow<List<Persona>>(emptyList())
    val usuarios: StateFlow<List<Persona>> = _usuarios

    private val _guardadoExitoso = MutableStateFlow(false)
    val guardadoExitoso: StateFlow<Boolean> = _guardadoExitoso

    fun cargarUsuarios() {
        viewModelScope.launch {
            try {
                val response = usuariosService.obtenerTodos()
                if (response.isSuccessful) {
                    _usuarios.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun guardarNota(titulo: String, descripcion: String, tipo: String, itemsRaw: List<String>, idTrabajador: Int?) {
        viewModelScope.launch {
            try {
                val esTarea = tipo == "Tarea"

                // Definimos la nota según el tipo
                val notaObj = Nota(
                    id = null, // El servidor suele generar el ID
                    titulo = titulo,
                    descripcion = descripcion,
                    tipo = tipo,
                    cargatrabajo = if (esTarea) 5 else 0,
                    id_trabajador = if (esTarea) idTrabajador ?: 0 else 0,
                    fecha = java.time.LocalDate.now().toString()
                )

                // CORRECCIÓN DEL ERROR DE LA IMAGEN:
                // Convertimos List<String> a List<ItemTarea> pasando todos los parámetros
                val listaObjetosItem = if (esTarea) {
                    itemsRaw.map { texto ->
                        ItemTarea(
                            id = 0,             // O null si tu modelo lo permite
                            descripcion = texto,
                            completado = false, // Parámetro faltante en tu captura
                            notaId = 0         // O null, el servidor lo vincula al insertar
                        )
                    }
                } else {
                    emptyList()
                }

                val request = NotaRequest(nota = notaObj, items = listaObjetosItem)
                val response = notasService.insertarAIdEspecifico(request)

                if (response.isSuccessful) {
                    _guardadoExitoso.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}