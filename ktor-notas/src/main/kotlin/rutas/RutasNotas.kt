package com.example.rutas

import Modelos.Respuesta
import com.example.DAO.NotasDAO
import com.example.DAO.NotasDAOImpl
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
                return@get call.respond(HttpStatusCode.OK, notas)
            } else {
                // Siguiendo tu estilo de responder 201 o null cuando no hay datos
                return@get call.respond(201, null)
            }
        }
    }

    route("/registrarAuto") {
        post {
            try {
                val request = call.receive<NotaRequest>()
                if (notaDAO.insertarRandom(request.nota, request.items)) {
                    return@post call.respond(HttpStatusCode.Created, "Nota asignada automáticamente")
                } else {
                    return@post call.respond(HttpStatusCode.Conflict, "Error al insertar")
                }
            } catch (e: Exception) {
                return@post call.respond(HttpStatusCode.BadRequest, "Datos mal formados")
            }
        }
    }

    route("/registrar") {
        post {
            try {
                val request = call.receive<NotaRequest>()
                if (notaDAO.insertarAIdEspecifico(request.nota, request.items)) {
                    return@post call.respond(HttpStatusCode.Created, "Nota asignada al trabajador ${request.nota.id_trabajador}")
                } else {
                    return@post call.respond(HttpStatusCode.Conflict, "Error al insertar")
                }
            } catch (e: Exception) {
                return@post call.respond(HttpStatusCode.BadRequest, "Datos mal formados")
            }
        }
    }

    route("/modificar") {
        put("{id?}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest, "id vacío en la url")
            try {
                val request = call.receive<NotaRequest>()
                if (notaDAO.actualizar(id, request.nota, request.items)) {
                    return@put call.respond(HttpStatusCode.Accepted, Respuesta("Nota $id modificada", HttpStatusCode.Accepted.value))
                } else {
                    return@put call.respond(HttpStatusCode.NotFound, Respuesta("Nota $id no encontrada", HttpStatusCode.NotFound.value))
                }
            } catch (e: Exception) {
                return@put call.respond(HttpStatusCode.BadRequest, "Error en los datos")
            }
        }
    }

    route("/borrar") {
        delete("{id?}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText("id vacío en la url", status = HttpStatusCode.BadRequest)

            if (notaDAO.borrar(id)) {
                return@delete call.respond(HttpStatusCode.Accepted, true)
            } else {
                return@delete call.respond(HttpStatusCode.NotFound, Respuesta("Nota $id no encontrada", HttpStatusCode.NotFound.value))
            }
        }
    }
}