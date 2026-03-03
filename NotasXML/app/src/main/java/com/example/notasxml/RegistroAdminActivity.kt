package com.example.notasxml

import Modelos.Persona
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.notasxml.ViewModels.RegistroAdminViewModel
import com.example.notasxml.databinding.ActivityRegistroAdminBinding

class RegistroAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroAdminBinding
    // En Activity se usa directamente by viewModels() sin requireContext()
    private val viewModel: RegistroAdminViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflar el binding
        binding = ActivityRegistroAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        // 1. Configurar el Dropdown de Roles
        val opcionesRol = arrayOf("Usuario", "Admin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opcionesRol)
        binding.spinnerRol.setAdapter(adapter)

        // 2. Lógica del botón registrar
        binding.btnRegistrar.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val dni = binding.etDni.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            val rolTexto = binding.spinnerRol.text.toString()

            if (nombre.isNotEmpty() && dni.isNotEmpty() && pass.isNotEmpty() && rolTexto.isNotEmpty()) {
                val rolNum = if (rolTexto.equals("Admin", ignoreCase = true)) 1 else 0

                val nuevaPersona = Persona(
                    id = 0,
                    dni = dni,
                    nombre = nombre,
                    password = pass,
                    rol = rolNum,
                    foto = ""
                )

                viewModel.registrarUsuario(nuevaPersona)
            } else {
                Toast.makeText(this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.registroExitoso.observe(this) { exito ->
            if (exito) {
                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                // Cierra la Activity y vuelve a la anterior
                finish()
            }
        }

        viewModel.mensajeError.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }
}