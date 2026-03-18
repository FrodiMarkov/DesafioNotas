package com.example.notasxml

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.notasxml.Helpers.NotasHolder
import com.example.notasxml.ViewModels.NotasViewModel
import com.example.notasxml.databinding.ActivityEditarNotaUsuarioBinding

class EditarNotaUsuario : AppCompatActivity() {

    lateinit var binding: ActivityEditarNotaUsuarioBinding
    private val viewModel: NotasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditarNotaUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val notaActual = NotasHolder.nota
        binding.etTituloNota.setText(notaActual.titulo)
        binding.etDescNota.setText(notaActual.descripcion)

        binding.btnGuardarNota.setOnClickListener {
            val titulo = binding.etTituloNota.text.toString()
            val desc = binding.etDescNota.text.toString()

            if (titulo.isNotEmpty()) {
                viewModel.actualizarNota(
                    id = notaActual.id ?: 0,
                    titulo = titulo,
                    descripcion = desc,
                    tipo = "Nota",
                    items = emptyList()
                )
            } else {
                Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancelarNota.setOnClickListener {
            finish()
        }

        viewModel.guardadoExitoso.observe(this) { exitoso ->
            if (exitoso) {
                Toast.makeText(this, "Nota actualizada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}