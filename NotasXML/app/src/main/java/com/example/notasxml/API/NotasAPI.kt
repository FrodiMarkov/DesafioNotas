package com.example.notasxml.API

import Modelos.Respuesta
import com.example.Modelos.NotaConItems
import com.example.Modelos.NotaRequest
import retrofit2.Response
import retrofit2.http.*

interface NotasAPI {

    // GET http://localhost:8095/notas
    @GET("notas")
    suspend fun obtenerTodas(): Response<List<NotaConItems>>

    // POST http://localhost:8095/notas/registrarAuto
    @POST("notas/registrarAuto")
    suspend fun insertarRandom(@Body request: NotaRequest): Response<Respuesta>

    // POST http://localhost:8095/notas/registrar
    @POST("notas/registrar")
    suspend fun insertarAIdEspecifico(@Body request: NotaRequest): Response<String>

    // PUT http://localhost:8095/notas/modificar/5
    @PUT("notas/modificar/{id}")
    suspend fun actualizar(@Path("id") id: Int, @Body request: NotaRequest): Response<Respuesta>

    // DELETE http://localhost:8095/notas/borrar/5
    @DELETE("notas/borrar/{id}")
    suspend fun borrar(@Path("id") id: Int): Response<Boolean>

    @GET("notas/usuario/{id}")
    suspend fun obtenerPorUsuario(@Path("id") id: Int): Response<List<NotaConItems>>
}