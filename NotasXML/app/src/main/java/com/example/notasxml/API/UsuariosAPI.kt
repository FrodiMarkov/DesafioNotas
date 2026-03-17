package com.example.notasxml.API

import Modelos.Persona
import Modelos.PersonaLogin
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuariosAPI {

    // POST http://localhost:8095/usuarios/login
    @POST("usuarios/login")
    suspend fun login(@Body p: PersonaLogin): Response<Persona>

    // POST http://localhost:8095/usuarios/registrar
    @POST("usuarios/registrar")
    suspend fun insertar(@Body persona: Persona): Response<Unit>

    // GET http://localhost:8095/usuarios/ver/{dni}
    @GET("usuarios/ver/{dni}")
    suspend fun obtener(@Path("dni") dni: String): Response<Persona>

    // PUT http://localhost:8095/usuarios/modificar/{dni}
    @PUT("usuarios/modificar/{dni}")
    suspend fun actualizar(@Path("dni") dni: String, @Body persona: Persona): Response<Unit>

    // DELETE http://localhost:8095/usuarios/borrar/{dni}
    @DELETE("usuarios/borrar/{dni}")
    suspend fun eliminar(@Path("dni") dni: String): Response<Unit>

    // GET http://localhost:8095/usuarios
    @GET("usuarios")
    suspend fun obtenerTodos(): Response<List<Persona>>
}