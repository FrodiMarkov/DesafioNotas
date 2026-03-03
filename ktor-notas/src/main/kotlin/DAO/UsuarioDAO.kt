package DAO

import Modelos.Persona
import Modelos.PersonaLogin

interface UsuarioDAO {
    fun insertar(persona: Persona): Boolean
    fun obtener(dni: String): Persona?
    fun actualizar(dni:String, persona: Persona): Boolean
    fun eliminar(dni: String): Boolean
    fun obtenerTodos(): List<Persona>
    fun login(p: PersonaLogin):Persona?
}