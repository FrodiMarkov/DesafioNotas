package com.example.Modelos

import kotlinx.serialization.Serializable
@Serializable
data class NotaRequest(
    val nota: Nota,
    val items: List<ItemTarea>? = null
)