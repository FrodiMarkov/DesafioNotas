package com.example.Modelos

import kotlinx.serialization.Serializable
import java.sql.Date

@Serializable
data class Nota(
    val id: Int? = null,
    val titulo: String,
    val descripcion: String,
    val tipo: String,
    val cargatrabajo: Int,
    val id_trabajador: Int,
    val fecha: String
)