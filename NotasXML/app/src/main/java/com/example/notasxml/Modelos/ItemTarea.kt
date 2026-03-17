package com.example.Modelos

import kotlinx.serialization.Serializable
@Serializable
data class ItemTarea(
    val id: Int,
    val notaId: Int,
    val descripcion: String,
    val completado: Boolean
)