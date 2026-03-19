package com.example.notasxml

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.NotasViewModel
import com.example.notasxml.databinding.ActivityCrearNotaUsuarioBinding

class CrearNotaUsuario : AppCompatActivity() {

    private val viewModel: NotasViewModel by viewModels()
    private lateinit var binding: ActivityCrearNotaUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCrearNotaUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCancelarNota.setOnClickListener { finish() }

        binding.btnGuardarNota.setOnClickListener {
            val titulo = binding.etTituloNota.text.toString().trim()
            val desc = binding.etDescNota.text.toString().trim()

            if (titulo.isNotEmpty()) {
                viewModel.guardarNota(titulo, desc, "Nota", emptyList(), UsuarioHolder.usuario.id)
            } else {
                Toast.makeText(this, "Introduce un título", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.guardadoExitoso.observe(this) { exitoso ->
            if (exitoso == true) {
                Toast.makeText(this, "Nota guardada con éxito", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.error.observe(this) { mensaje ->
            if (mensaje != null) {
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            }
        }
    }
}