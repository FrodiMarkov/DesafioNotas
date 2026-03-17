package com.example.Modelos

import kotlinx.serialization.Serializable
@Serializable
data class NotaConItems(
    val nota: Nota,
    val items: List<ItemTarea>
)