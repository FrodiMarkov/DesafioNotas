package com.example.DAO

import Helpers.Database
import com.example.Modelos.Nota

class NotasDAOImpl : NotasDAO {
    override fun insertarRandom(nota: Nota, items: List<String>?): Boolean {
        var idAsignado = 1
        val queryCount = "SELECT u.id FROM usuario u LEFT JOIN nota n ON u.id = n.id_trabajador GROUP BY u.id ORDER BY COUNT(n.id) ASC LIMIT 1"

        Database.getConnection()?.use { conn ->
            val rsCount = conn.prepareStatement(queryCount).executeQuery()
            if (rsCount.next()) {
                idAsignado = rsCount.getInt("id_trabajador")
            }

            val sqlInsertNota = "INSERT INTO nota (titulo, descripcion, tipo, cargatrabajo, id_trabajador) VALUES (?, ?, ?, ?, ?)"
            val stNota = conn.prepareStatement(sqlInsertNota)
            stNota.setString(1, nota.titulo)
            stNota.setString(2, nota.descripcion)
            stNota.setString(3, nota.tipo)
            stNota.setInt(4, nota.cargatrabajo)
            stNota.setInt(5, idAsignado)

            val insertoNota = stNota.executeUpdate() > 0

            if (insertoNota && nota.tipo.lowercase() == "tarea" && items != null) {
                val sqlGetId = "SELECT MAX(id) FROM nota"
                val rsId = conn.prepareStatement(sqlGetId).executeQuery()

                if (rsId.next()) {
                    val notaId = rsId.getInt(1)
                    val sqlInsertItem = "INSERT INTO itemstarea (notaid, descripcion_item, completado) VALUES (?, ?, ?)"
                    val stItem = conn.prepareStatement(sqlInsertItem)

                    for (textoItem in items) {
                        stItem.setInt(1, notaId)
                        stItem.setString(2, textoItem)
                        stItem.setInt(3, 0)
                        stItem.executeUpdate()
                    }
                }
            }
            return insertoNota
        }
        return false
    }
    override fun insertarAIdEspecifico(nota: Nota, items: List<String>?): Boolean {
        // 1. Usamos directamente el id_trabajador que ya viene en el objeto nota
        val sqlInsertNota = "INSERT INTO nota (titulo, descripcion, tipo, cargatrabajo, id_trabajador) VALUES (?, ?, ?, ?, ?)"

        Database.getConnection()?.use { conn ->
            val stNota = conn.prepareStatement(sqlInsertNota)
            stNota.setString(1, nota.titulo)
            stNota.setString(2, nota.descripcion)
            stNota.setString(3, nota.tipo)
            stNota.setInt(4, nota.cargatrabajo)
            stNota.setInt(5, nota.id_trabajador) // El ID que tú le pasas

            val insertoNota = stNota.executeUpdate() > 0

            // 2. Si es una tarea, insertamos sus ítems
            if (insertoNota && nota.tipo.lowercase() == "tarea" && items != null) {
                val rsId = conn.prepareStatement("SELECT MAX(id) FROM nota").executeQuery()
                if (rsId.next()) {
                    val notaId = rsId.getInt(1)
                    val sqlInsertItem = "INSERT INTO itemstarea (notaid, descripcion_item, completado) VALUES (?, ?, ?)"
                    val stItem = conn.prepareStatement(sqlInsertItem)

                    for (textoItem in items) {
                        stItem.setInt(1, notaId)
                        stItem.setString(2, textoItem)
                        stItem.setInt(3, 0)
                        stItem.executeUpdate()
                    }
                }
            }
            return insertoNota
        }
        return false
    }
}