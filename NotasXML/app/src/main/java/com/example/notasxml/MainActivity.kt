package com.example.notasxml

import android.app.AlertDialog
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
                if (persona.rol == 1) {
                    AlertDialog.Builder(this)
                        .setTitle("Seleccionar modo")
                        .setMessage("Has iniciado sesión como Administrador. ¿Cómo deseas entrar?")
                        .setPositiveButton("Entrar como Admin") { _, _ ->
                            UsuarioHolder.usuario = persona
                            val intent = Intent(this, PaginaPrincipalAdminActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("Entrar como Usuario") { _, _ ->
                            UsuarioHolder.usuario = persona
                            val intent = Intent(this, PaginaPrincipalUsuarioActivity::class.java)
                            startActivity(intent)
                        }
                        .setNeutralButton("Cancelar", null)
                        .show()
                }
                else if (persona.rol == 0) {
                    UsuarioHolder.usuario = persona
                    val intent = Intent(this, PaginaPrincipalUsuarioActivity::class.java)
                    startActivity(intent)
                }
                else {
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