package com.example.DAO

import com.example.Modelos.ItemTarea
import com.example.Modelos.Nota
import com.example.Modelos.NotaConItems

interface NotasDAO {
    fun insertarRandom(nota: Nota, items: List<ItemTarea>?): Boolean
    fun insertarAIdEspecifico(nota: Nota, items: List<ItemTarea>?) : Boolean
    fun obtenerTodas(): List<NotaConItems>
    fun actualizar(id: Int, nota: Nota, items: List<ItemTarea>?): Boolean
    fun borrar(id: Int) : Boolean
}