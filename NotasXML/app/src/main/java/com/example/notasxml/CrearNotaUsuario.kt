package com.example.notasxml

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.NotasViewModel

class CrearNotaUsuario : AppCompatActivity() {
    private val viewModel: NotasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_nota_usuario)

        val btnGuardar = findViewById<Button>(R.id.btnGuardarNota)
        val btnCancelar = findViewById<Button>(R.id.btnCancelarNota)
        val etTitulo = findViewById<EditText>(R.id.etTituloNota)
        val etDesc = findViewById<EditText>(R.id.etDescNota)

        btnCancelar.setOnClickListener { finish() }

        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            val desc = etDesc.text.toString().trim()

            if (titulo.isNotEmpty()) {
                viewModel.guardarNota(titulo, desc, "Nota", emptyList(), UsuarioHolder.usuario.id)
            } else {
                Toast.makeText(this, "Introduce un título", Toast.LENGTH_SHORT).show()
            }
        }

        // Observador corregido: Ahora coincide con el nombre del ViewModel
        viewModel.guardadoExitoso.observe(this) { exitoso ->
            if (exitoso == true) {
                Toast.makeText(this, "Nota guardada con éxito", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Observador de errores para debug
        viewModel.error.observe(this) { mensaje ->
            if (mensaje != null) {
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            }
        }
    }
}