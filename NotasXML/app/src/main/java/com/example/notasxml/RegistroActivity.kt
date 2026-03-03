package com.example.notasxml

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.notasxml.ViewModels.RegistroViewModel
import com.example.notasxml.databinding.ActivityRegistroBinding
import Modelos.Persona

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val viewModel: RegistroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.registroExitoso.observe(this) { exito ->
            if (exito) {
                Toast.makeText(this, "¡Registro completado!", Toast.LENGTH_SHORT).show()
                finish() // Cerramos esta pantalla y volvemos al Login
            }
        }

        viewModel.error.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }

        binding.btnFinalizarRegistro.setOnClickListener {
            val dni = binding.etRegDni.text.toString()
            val nombre = binding.etRegNombre.text.toString()
            val pass = binding.etRegPassword.text.toString()

            if (dni.isNotEmpty() && nombre.isNotEmpty() && pass.isNotEmpty()) {
                val nuevaPersona = Persona(0, dni, nombre, pass, 0, "")
                viewModel.registrarUsuario(nuevaPersona)
            } else {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}