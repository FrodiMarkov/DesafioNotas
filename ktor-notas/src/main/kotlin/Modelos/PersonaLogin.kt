package Modelos

import kotlinx.serialization.Serializable

@Serializable
data class PersonaLogin(val nombre:String,
                        val password:String)
