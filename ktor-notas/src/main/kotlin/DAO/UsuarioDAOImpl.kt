package DAO

import Helpers.Database
import Modelos.Persona
import Modelos.PersonaLogin

class UsuarioDAOImpl : UsuarioDAO {
    override fun insertar(persona: Persona): Boolean {
        val sql = "INSERT INTO usuario (nombre, password, foto, rol, dni) VALUES (?, ?, ?, ?, ?)"
        val connection = Database.getConnection()
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setString(1, persona.nombre)
            statement.setString(2, persona.password)
            statement.setString(3, persona.foto)
            statement.setInt(4, persona.rol)
            statement.setString(5, persona.dni)
            try {
                return statement.executeUpdate() > 0
            } catch (e: Exception) {
                return false
            }

        }
        return false
    }

    override fun obtener(dni: String): Persona? {
        val sql = "SELECT * FROM usuario WHERE dni = ?"
        val connection = Database.getConnection()
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setString(1, dni)
            val resultSet = statement.executeQuery()

            if (resultSet.next()) {
                return Persona(
                    dni = resultSet.getString("dni"),
                    nombre = resultSet.getString("nombre"),
                    password = resultSet.getString("password"),
                    rol = resultSet.getInt("rol"),
                    foto = resultSet.getString("foto"),
                    id = resultSet.getInt("id")
                )
            }
        }
        return null
    }

    override fun actualizar(dni:String, persona: Persona): Boolean {
        val sql = "UPDATE usuario SET nombre = ?, password = ?, foto = ?, rol = ? WHERE dni = ?"
        val connection = Database.getConnection()
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setString(1, persona.nombre)
            statement.setString(2, persona.password)
            statement.setString(3, persona.foto)
            statement.setInt(4, persona.rol)
            statement.setString(5, dni)

            return statement.executeUpdate() > 0
        }
        return false
    }

    override fun eliminar(dni: String): Boolean {
        val sql = "DELETE FROM usuario WHERE dni = ?"
        val connection = Database.getConnection()
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setString(1, dni)

            return statement.executeUpdate() > 0
        }
        return false
    }

    override fun obtenerTodos(): List<Persona> {
        val personas = mutableListOf<Persona>()
        val sql = "SELECT * FROM usuario"
        val connection = Database.getConnection()
        connection?.use {
            val statement = it.prepareStatement(sql)
            val resultSet = statement.executeQuery()

            while (resultSet.next()) {
                val persona = Persona(
                    dni = resultSet.getString("dni"),
                    nombre = resultSet.getString("nombre"),
                    password = resultSet.getString("password"),
                    rol = resultSet.getInt("rol"),
                    foto = resultSet.getString("foto"),
                    id = resultSet.getInt("id")
                )
                personas.add(persona)
            }
        }
        return personas
    }

    override fun login(p: PersonaLogin): Persona? {
        val sql = "SELECT * FROM usuario WHERE nombre = ? AND password = ?"

        return try {
            Database.getConnection()?.use { conn ->
                val statement = conn.prepareStatement(sql)
                statement.setString(1, p.nombre.trim())
                statement.setString(2, p.password.trim())
                val resultSet = statement.executeQuery()

                if (resultSet.next()) {
                    Persona(
                        dni = resultSet.getString("dni"),
                        nombre = resultSet.getString("nombre"),
                        password = resultSet.getString("password"),
                        rol = resultSet.getInt("rol"),
                        foto = resultSet.getString("foto"),
                        id = resultSet.getInt("id")
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            println("ERROR SQL: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}