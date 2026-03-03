package com.example.notasxml

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.LoginViewModel
import com.example.notasxml.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.errorMessage.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        viewModel.usuarioLogeado.observe(this) { persona ->
            if (persona != null) {
                val destino = when (persona.rol) {
                    1 -> PaginaPrincipalAdminActivity::class.java
                    0 -> PaginaPrincipalUsuarioActivity::class.java
                    else -> null
                }

                if (destino != null) {
                    val intent = Intent(this, destino)
                    UsuarioHolder.usuario = persona
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Error: Rol no definido para este usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val dni = binding.etUsername.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            if (dni.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.loginUser(dni, pass)
            } else {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}