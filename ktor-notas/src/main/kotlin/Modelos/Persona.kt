package Modelos

import kotlinx.serialization.Serializable

@Serializable
data class Persona(val id:Int,
                   val dni:String,
                   val nombre:String,
                   val password:String,
                   val rol:Int,
                   val foto: String)
