package com.example.Modelos

data class NotaRequest(
    val nota: Nota,
    val items: List<ItemTarea>? = null
)