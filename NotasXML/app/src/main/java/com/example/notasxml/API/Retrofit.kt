package com.example.notasxml.API

import com.example.clienteapi_serverktor_25_26.Api.Parametros
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory.*

object Retrofit {
    val usuariosRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Parametros.url + ":" + Parametros.puerto + "/")
            .addConverterFactory(create())
            .build()
            .create(UsuariosAPI::class.java)
    }
}