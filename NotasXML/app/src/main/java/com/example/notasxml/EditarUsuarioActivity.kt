package com.example.notasxml

import Modelos.Persona
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.RegistroViewModel
import com.example.notasxml.databinding.ActivityEditarUsuarioBinding

class EditarUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarUsuarioBinding
    private val viewModel: RegistroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val opcionesRol = arrayOf("Usuario", "Admin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opcionesRol)

        viewModel.registroExitoso.observe(this) { exito ->
            if (exito) {
                Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.error.observe(this) { mensaje ->
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        }

        binding.spinnerRolEditar.setAdapter(adapter)

        binding.btnVolverEditar.setOnClickListener { finish() }

        binding.btnGuardarCambios.setOnClickListener {
            val usuarioActual = UsuarioHolder.usuario
            if (usuarioActual != null) {
                val nombre = binding.etNombreEditar.text.toString().trim()
                val dni = binding.etDniEditar.text.toString().trim()
                val pass = binding.etPasswordEditar.text.toString().trim()
                val rolTexto = binding.spinnerRolEditar.text.toString()

                if (nombre.isNotEmpty() && dni.isNotEmpty() && pass.isNotEmpty()) {
                    val rolNum = if (rolTexto == "Admin") 1 else 0
                    val personaEditada = Persona(
                        id = usuarioActual.id,
                        dni = dni,
                        nombre = nombre,
                        password = pass,
                        rol = rolNum,
                        foto = usuarioActual.foto
                    )
                    viewModel.actualizarUsuario(personaEditada)
                } else {
                    Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val usuario = UsuarioHolder.usuario
        if (usuario != null) {
            binding.etNombreEditar.setText(usuario.nombre)
            binding.etDniEditar.setText(usuario.dni)
            binding.etPasswordEditar.setText(usuario.password)
            val rolTexto = if (usuario.rol == 1) {
                "Admin"
            } else {
                "Usuario"
            }
            binding.spinnerRolEditar.setText(rolTexto, false)
        }

    }
}