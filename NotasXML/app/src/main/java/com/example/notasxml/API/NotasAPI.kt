package com.example.notasxml.API

import Modelos.Respuesta
import com.example.Modelos.NotaConItems
import com.example.Modelos.NotaRequest
import retrofit2.Response
import retrofit2.http.*

interface NotasAPI {
    @POST("notas/registrarAuto")
    suspend fun insertarRandom(@Body request: NotaRequest): Response<Respuesta>

    @POST("notas/registrar")
    suspend fun insertarAIdEspecifico(@Body request: NotaRequest): Response<String>

    @PUT("notas/modificar/{id}")
    suspend fun actualizar(@Path("id") id: Int, @Body request: NotaRequest): Response<Respuesta>

    @DELETE("notas/borrar/{id}")
    suspend fun borrar(@Path("id") id: Int): Response<Boolean>

    @GET("notas/usuario/{id}")
    suspend fun obtenerPorUsuario(@Path("id") id: Int): Response<List<NotaConItems>>
}