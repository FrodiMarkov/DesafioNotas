package com.example.notasxml

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.UsuarioViewModel
import com.example.notasxml.databinding.ActivityCambiarPassBinding

class CambiarPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCambiarPassBinding
    private val viewModel: UsuarioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCambiarPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.operacionExitosa.observe(this) { exito ->
            if (exito) {
                Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }

        // Botón Volver
        binding.btnVolverPass.setOnClickListener {
            finish()
        }

        // Botón Guardar
        binding.btnGuardarPass.setOnClickListener {
            val pass1 = binding.etNuevaPass.text.toString()
            val pass2 = binding.etConfirmarPass.text.toString()

            if (pass1.isNotEmpty() && pass1 == pass2) {
                // Obtenemos el usuario actual del Singleton y modificamos el password
                val usuarioEditado = UsuarioHolder.usuario
                usuarioEditado.password = pass1

                viewModel.cambiarPass(usuarioEditado)
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden o están vacías", Toast.LENGTH_SHORT).show()
            }
        }
    }
}