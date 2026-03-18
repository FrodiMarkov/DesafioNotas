package com.example.rutas

import Modelos.Respuesta
import com.example.DAO.NotasDAO
import com.example.DAO.NotasDAOImpl
import com.example.Modelos.NotaConItems
import com.example.Modelos.NotaRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.notaRouting() {
    val notaDAO: NotasDAO = NotasDAOImpl()

    route("/notas") {
        get {
            val notas = notaDAO.obtenerTodas()
            if (notas.isNotEmpty()) {
                call.respond(HttpStatusCode.OK, notas)
            } else {
                call.respond(HttpStatusCode.NoContent)
            }
        }
        get("/usuario/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest, "ID no válido")

            // Suponiendo que tu notaDAO tiene este método (debes implementarlo si no existe)
            val notasDelUsuario = notaDAO.obtenerPorUsuario(id)

            if (notasDelUsuario.isNotEmpty()) {
                call.respond(HttpStatusCode.OK, notasDelUsuario)
            } else {
                call.respond(HttpStatusCode.OK, emptyList<NotaConItems>())
            }
        }

        post("/registrarAuto") {
            try {
                val request = call.receive<NotaRequest>()
                if (notaDAO.insertarRandom(request.nota, request.items)) {
                    call.respond(HttpStatusCode.Created, Respuesta("Nota asignada automáticamente", 201))
                } else {
                    call.respond(HttpStatusCode.Conflict, "Error al insertar")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Datos mal formados")
            }
        }

        post("/registrar") {
            try {
                val request = call.receive<NotaRequest>()
                if (notaDAO.insertarAIdEspecifico(request.nota, request.items)) {
                    call.respond(HttpStatusCode.Created, "Nota asignada al trabajador ${request.nota.id_trabajador}")
                } else {
                    call.respond(HttpStatusCode.Conflict, "Error al insertar")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Datos mal formados")
            }
        }

        put("/modificar/{id?}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest, "id vacío en la url")
            try {
                val request = call.receive<NotaRequest>()
                if (notaDAO.actualizar(id, request.nota, request.items)) {
                    call.respond(HttpStatusCode.Accepted, Respuesta("Nota $id modificada", HttpStatusCode.Accepted.value))
                } else {
                    call.respond(HttpStatusCode.NotFound, Respuesta("Nota $id no encontrada", HttpStatusCode.NotFound.value))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Error en los datos")
            }
        }

        delete("/borrar/{id?}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText("id vacío en la url", status = HttpStatusCode.BadRequest)
            if (notaDAO.borrar(id)) {
                call.respond(HttpStatusCode.Accepted, true)
            } else {
                call.respond(HttpStatusCode.NotFound, Respuesta("Nota $id no encontrada", HttpStatusCode.NotFound.value))
            }
        }
    }
}