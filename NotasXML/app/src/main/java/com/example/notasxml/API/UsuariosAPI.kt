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
    @POST("login")
    suspend fun login(@Body p: PersonaLogin): Response<Persona>

    @POST("registrar")
    suspend fun insertar(@Body persona: Persona): Response<Unit>

    // Ktor: route("/usuario") { get("{dni?}") }
    @GET("usuario/{dni}")
    suspend fun obtener(@Path("dni") dni: String): Response<Persona>

    // Ktor: route("/modificar") { put("{dni?}") }
    @PUT("modificar/{dni}")
    suspend fun actualizar(@Path("dni") dni: String, @Body persona: Persona): Response<Unit>

    // Ktor: route("/borrar") { delete("{dni?}") }
    @DELETE("borrar/{dni}")
    suspend fun eliminar(@Path("dni") dni: String): Response<Unit>

    @GET("usuario")
    suspend fun obtenerTodos(): Response<List<Persona>>
}