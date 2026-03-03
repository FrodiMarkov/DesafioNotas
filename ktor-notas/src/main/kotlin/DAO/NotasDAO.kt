package com.example.DAO

import Modelos.Persona
import com.example.Modelos.Nota

interface NotasDAO {
    fun insertarRandom(nota: Nota, items: List<String>?): Boolean
    fun insertarAIdEspecifico(nota: Nota, items: List<String>?) : Boolean
}