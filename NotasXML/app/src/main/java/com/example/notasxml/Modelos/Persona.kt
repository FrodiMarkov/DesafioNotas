package Modelos

import kotlinx.serialization.Serializable

@Serializable
data class Persona(var id:Int,
                   var dni:String,
                   var nombre:String,
                   var password:String,
                   var rol:Int,
                   var foto: String)
