package com.example.DAO

import Helpers.Database
import com.example.Modelos.ItemTarea
import com.example.Modelos.Nota
import com.example.Modelos.NotaConItems
import java.sql.Date // Importante para manejar la fecha de SQL

class NotasDAOImpl : NotasDAO {
    override fun insertarRandom(nota: Nota, items: List<ItemTarea>?): Boolean {
        var idAsignado = 1
        //hago el count de las veces que aparece el id en la base de datos
        //y de ahi saco el que tenga el mayor para asignarle la tarea.
        // Cambio: Ordenamos por la suma de cargatrabajo para encontrar al que menos tiene
        val queryCount = "SELECT u.id FROM usuario u LEFT JOIN nota n ON u.id = n.id_trabajador GROUP BY u.id ORDER BY SUM(n.cargatrabajo) ASC LIMIT 1"

        Database.getConnection()?.use { conn ->
            var statement = conn.prepareStatement(queryCount)
            val rsCount = statement.executeQuery()
            if (rsCount.next()) {
                // Usamos el índice 1 ya que el SELECT solo trae u.id
                idAsignado = rsCount.getInt(1)
            }

            // Añadimos el campo 'fecha' a la consulta SQL
            val sqlInsertNota = "INSERT INTO nota (titulo, descripcion, tipo, cargatrabajo, id_trabajador, fecha) VALUES (?, ?, ?, ?, ?, ?)"
            val stNota = conn.prepareStatement(sqlInsertNota)
            stNota.setString(1, nota.titulo)
            stNota.setString(2, nota.descripcion)
            stNota.setString(3, nota.tipo)
            stNota.setInt(4, nota.cargatrabajo)
            stNota.setInt(5, idAsignado)
            // Insertamos la fecha actual
            stNota.setDate(6, Date(System.currentTimeMillis()))

            val insertoNota = stNota.executeUpdate() > 0

            if (insertoNota && nota.tipo.lowercase() == "tarea" && items != null) {
                val sqlGetId = "SELECT MAX(id) FROM nota"
                var statementId = conn.prepareStatement(sqlGetId)
                val rsId = statementId.executeQuery()

                if (rsId.next()) {
                    val notaId = rsId.getInt(1)
                    val sqlInsertItem = "INSERT INTO itemstarea (notaid, descripcion_item, completado) VALUES (?, ?, ?)"
                    val stItem = conn.prepareStatement(sqlInsertItem)

                    for (item in items) {
                        stItem.setInt(1, notaId)
                        stItem.setString(2, item.descripcion)
                        stItem.setBoolean(3, item.completado)
                        stItem.executeUpdate() // Corregido: executeUpdate para inserción
                    }
                }
            }
            return insertoNota
        }
        return false
    }

    override fun insertarAIdEspecifico(nota: Nota, items: List<ItemTarea>?): Boolean {
        // Añadimos el campo 'fecha' a la consulta SQL
        val sqlInsertNota = "INSERT INTO nota (titulo, descripcion, tipo, cargatrabajo, id_trabajador, fecha) VALUES (?, ?, ?, ?, ?, ?)"

        Database.getConnection()?.use { conn ->
            val stNota = conn.prepareStatement(sqlInsertNota)
            stNota.setString(1, nota.titulo)
            stNota.setString(2, nota.descripcion)
            stNota.setString(3, nota.tipo)
            stNota.setInt(4, nota.cargatrabajo)
            stNota.setInt(5, nota.id_trabajador)
            // Insertamos la fecha actual
            stNota.setDate(6, Date(System.currentTimeMillis()))

            val insertoNota = stNota.executeUpdate() > 0

            if (insertoNota && nota.tipo.lowercase() == "tarea" && items != null) {
                var sql = "SELECT MAX(id) FROM nota"
                var statement = conn.prepareStatement(sql)
                val rsId = statement.executeQuery()
                if (rsId.next()) {
                    val notaId = rsId.getInt(1)
                    val sqlInsertItem = "INSERT INTO itemstarea (notaid, descripcion_item, completado) VALUES (?, ?, ?)"
                    val stItem = conn.prepareStatement(sqlInsertItem)

                    for (item in items) {
                        stItem.setInt(1, notaId)
                        stItem.setString(2, item.descripcion)
                        stItem.setBoolean(3, item.completado)
                        stItem.executeUpdate() // Corregido: executeUpdate para inserción
                    }
                }
            }
            return insertoNota
        }
        return false
    }

    override fun obtenerTodas(): List<NotaConItems> {
        val lista = mutableListOf<NotaConItems>()
        val sql = "SELECT * FROM nota"
        val connection = Database.getConnection()

        connection?.use { conn ->
            val statement = conn.prepareStatement(sql)
            val resultSet = statement.executeQuery()

            while (resultSet.next()) {
                val nota = Nota(
                    id = resultSet.getInt("id"),
                    titulo = resultSet.getString("titulo"),
                    descripcion = resultSet.getString("descripcion"),
                    tipo = resultSet.getString("tipo"),
                    cargatrabajo = resultSet.getInt("cargatrabajo"),
                    id_trabajador = resultSet.getInt("id_trabajador"),
                    fecha = resultSet.getDate("fecha").toString() // Recuperamos la fecha como String
                )

                val items = mutableListOf<ItemTarea>()
                if (nota.tipo.lowercase() == "tarea") {
                    val sqlItems = "SELECT * FROM itemstarea WHERE notaid = ?"
                    val stItems = conn.prepareStatement(sqlItems)
                    stItems.setInt(1, nota.id!!)
                    val rsItems = stItems.executeQuery()
                    while (rsItems.next()) {
                        items.add(ItemTarea(
                            id = rsItems.getInt("id"),
                            notaId = rsItems.getInt("notaid"),
                            descripcion = rsItems.getString("descripcion_item"),
                            completado = rsItems.getBoolean("completado")
                        ))
                    }
                }
                lista.add(NotaConItems(nota, items))
            }
        }
        return lista
    }

    override fun actualizar(id: Int, nota: Nota, items: List<ItemTarea>?): Boolean {
        // En el UPDATE solemos mantener la fecha original o actualizarla a la actual según prefieras
        val sql = "UPDATE nota SET titulo = ?, descripcion = ?, tipo = ?, cargatrabajo = ?, id_trabajador = ?, fecha = ? WHERE id = ?"
        val connection = Database.getConnection()

        connection?.use { conn ->
            val statement = conn.prepareStatement(sql)
            statement.setString(1, nota.titulo)
            statement.setString(2, nota.descripcion)
            statement.setString(3, nota.tipo)
            statement.setInt(4, nota.cargatrabajo)
            statement.setInt(5, nota.id_trabajador)
            statement.setDate(6, Date(System.currentTimeMillis())) // Actualizamos a fecha de hoy
            statement.setInt(7, id)

            val resultado = statement.executeUpdate() > 0

            if (resultado && nota.tipo.lowercase() == "tarea" && items != null) {
                //borro las tareas que tenia antes en la base de datos y las vuelvo a meter
                //para no tener que ir dando vueltas todo el rato
                //y buscar la id interna que se habia creado para el item de la tarea exacta que he borrado
                //ya que esto me ha dado muchos fallos
                val sqlDelete = "DELETE FROM itemstarea WHERE notaid = ?"
                val stDelete = conn.prepareStatement(sqlDelete)
                stDelete.setInt(1, id)
                stDelete.executeUpdate()

                val sqlInsertItem = "INSERT INTO itemstarea (notaid, descripcion_item, completado) VALUES (?, ?, ?)"
                for (item in items) {
                    val stItem = conn.prepareStatement(sqlInsertItem)
                    stItem.setInt(1, id)
                    stItem.setString(2, item.descripcion)
                    stItem.setInt(3, if (item.completado) 1 else 0)
                    stItem.executeUpdate()
                }
            }
            return resultado
        }
        return false
    }

    override fun borrar(id: Int): Boolean {
        val sqlItems = "DELETE FROM itemstarea WHERE notaid = ?"
        val sqlNota = "DELETE FROM nota WHERE id = ?"
        val connection = Database.getConnection()

        return connection?.use { conn ->
            // 1. Forzamos el borrado de los items primero
            val statementItems = conn.prepareStatement(sqlItems)
            statementItems.setInt(1, id)
            statementItems.executeUpdate()

            // 2. Ahora borramos la nota
            val statementNota = conn.prepareStatement(sqlNota)
            statementNota.setInt(1, id)

            val filasBorradas = statementNota.executeUpdate()
            filasBorradas > 0 // Si devuelve false aquí, el ID no coincide exactamente
        } ?: false
    }
}