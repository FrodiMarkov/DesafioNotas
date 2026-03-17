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



fun Route.userRouting() {
    val usuarioDAO: UsuarioDAO = UsuarioDAOImpl()

    route("/usuarios") {
        // GET http://localhost:8095/usuarios
        get {
            val usuarios = usuarioDAO.obtenerTodos()
            if (usuarios.isNotEmpty()) {
                call.respond(HttpStatusCode.OK, usuarios)
            } else {
                call.respond(HttpStatusCode.NoContent)
            }
        }

        // GET http://localhost:8095/usuarios/ver/12345678X
        get("/ver/{dni?}") {
            val dni = call.parameters["dni"] ?: return@get call.respond(HttpStatusCode.BadRequest, "DNI vacío")
            val usuario = usuarioDAO.obtener(dni)
            if (usuario == null) {
                call.respond(HttpStatusCode.NotFound, Respuesta("Usuario $dni no encontrado", 404))
            } else {
                call.respond(HttpStatusCode.OK, usuario)
            }
        }

        // POST http://localhost:8095/usuarios/login
        post("/login") {
            val us = call.receive<PersonaLogin>()
            val usuario = usuarioDAO.login(us)
            if (usuario == null) {
                call.respond(HttpStatusCode.NotFound, Respuesta("Login incorrecto para ${us.nombre}", 404))
            } else {
                call.respond(HttpStatusCode.OK, usuario)
            }
        }

        // POST http://localhost:8095/usuarios/registrar
        post("/registrar") {
            val us = call.receive<Persona>()
            if (usuarioDAO.insertar(us)) {
                call.respond(HttpStatusCode.Created, "Registro creado correctamente")
            } else {
                call.respond(HttpStatusCode.Conflict, "Clave duplicada")
            }
        }

        // DELETE http://localhost:8095/usuarios/borrar/12345678X
        delete("/borrar/{dni?}") {
            val dni = call.parameters["dni"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "DNI vacío")
            if (usuarioDAO.eliminar(dni)) {
                call.respond(HttpStatusCode.Accepted, true)
            } else {
                call.respond(HttpStatusCode.NotFound, Respuesta("Usuario $dni no encontrado", 404))
            }
        }

        // PUT http://localhost:8095/usuarios/modificar/12345678X
        put("/modificar/{dni?}") {
            val dni = call.parameters["dni"] ?: return@put call.respond(HttpStatusCode.BadRequest, "DNI vacío")
            val us = call.receive<Persona>()
            if (usuarioDAO.actualizar(dni, us)) {
                call.respond(HttpStatusCode.Accepted, Respuesta("Usuario $dni modificado", 202))
            } else {
                call.respond(HttpStatusCode.NotFound, Respuesta("Usuario $dni no encontrado", 404))
            }
        }
    }
}