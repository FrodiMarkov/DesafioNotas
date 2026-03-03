package rutas

import DAO.UsuarioDAO
import DAO.UsuarioDAOImpl
import Modelos.Persona
import Modelos.PersonaLogin
import Modelos.Respuesta
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*



fun Route.userRouting(){  //Esta ruta se incluirá en el archivo Routing.
    val usuarioDAO: UsuarioDAO = UsuarioDAOImpl()

        route("/usuario") {
            get {
                val usuarios = usuarioDAO.obtenerTodos()
                if (usuarios.isNotEmpty()) {
                    return@get call.respond(HttpStatusCode.OK, usuarios)
                } else {
                    //call.respondText("No hay usuarios", status = HttpStatusCode.OK)
                    return@get call.respond(201, null)
                }
            }
            get("{dni?}") {
                val dni = call.parameters["dni"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Parámetro vacío")
                try {
                    //Esto es para probar si el dato de la url es correcto.
                    val pruebaParam = dni.toString()
                } catch (e: Exception) {
                    return@get call.respond(HttpStatusCode.BadRequest, "Parámetro ${dni} no válido")
                }
                val usuario =usuarioDAO.obtener(dni)
                if (usuario == null) {
                    //call.response.status(HttpStatusCode.NotFound)
                    //call.respondText("Usuario ${id} no encontrado", status = HttpStatusCode.NotFound)
                    return@get call.respond(HttpStatusCode.NotFound, null)
                    //return@get call.respond(HttpStatusCode.NotFound, Respuesta("Usuario ${dni} no encontrado", HttpStatusCode.NotFound.value))
                }
                return@get call.respond(HttpStatusCode.OK, usuario)
            }
        }
        route("/login") {
            post{
                val us = call.receive<PersonaLogin>()
                val usuario = usuarioDAO.login(us)
                if (usuario == null) {
                    call.response.status(HttpStatusCode.NotFound)
                    return@post call.respond(HttpStatusCode.NotFound,
                        Respuesta("Usuario ${us.nombre} login incorrecto", HttpStatusCode.NotFound.value)
                    )
                    //call.respondText("Usuario ${id} no encontrado", status = HttpStatusCode.NotFound)
                }
                return@post call.respond(HttpStatusCode.OK, usuario)
            }
        }
        route("/registrar") {
            post{
                val us = call.receive<Persona>()
                if (usuarioDAO.insertar(us)) {
                    return@post call.respond(HttpStatusCode.Created, "Registro creado correctamente")
                    //call.rXespondText("Usuario creado",status = HttpStatusCode.Created)
                }
                else {
                    return@post call.respond(HttpStatusCode.Conflict, "Clave duplicada")
                }
            }
        }
        route("/borrar") {
            delete("{dni?}") {
                val dni = call.parameters["dni"] ?: return@delete call.respondText("id vacío en la url", status = HttpStatusCode.BadRequest)
                if (usuarioDAO.eliminar(dni)){
                    //call.respondText("Usuario eliminado",status = HttpStatusCode.Accepted)
                    //return@delete call.respond(HttpStatusCode.Accepted, Respuesta("Usuario ${dni} eliminado", HttpStatusCode.Accepted.value))
                    return@delete call.respond(HttpStatusCode.Accepted, true)
                }
                else {
                    return@delete call.respond(HttpStatusCode.NotFound, Respuesta("Usuario ${dni} no encontrado", HttpStatusCode.NotFound.value))
                    //return@delete call.respond(HttpStatusCode.Accepted, false)
                    //call.respondText("No encontrado",status = HttpStatusCode.NotFound)
                }
            }
        }
        route("/modificar") {
            put("{dni?}") {
                val dni = call.parameters["dni"] ?: return@put call.respondText("id vacío en la url", status = HttpStatusCode.BadRequest)
                val us = call.receive<Persona>()
                //Cambiamos los datos del usuario con ese dni (menos el dni).
                if (usuarioDAO.actualizar(dni, us)){
                    //call.respondText("No encontrado",status = HttpStatusCode.NotFound)
                    return@put call.respond(HttpStatusCode.Accepted, Respuesta("Usuario ${dni} modificado", HttpStatusCode.Accepted.value))
                }
                else {
                    return@put call.respond(HttpStatusCode.NotFound, Respuesta("Usuario ${dni} no encontrado", HttpStatusCode.NotFound.value))
                    //call.respondText("Usuario modificado",status = HttpStatusCode.Accepted)
                }
            }
        }


}