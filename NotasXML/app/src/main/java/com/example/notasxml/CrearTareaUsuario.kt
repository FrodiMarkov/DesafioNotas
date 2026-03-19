package com.example.notasxml

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Modelos.ItemTarea
import com.example.notasxml.Adaptadores.TareaAdapter
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.NotasViewModel
import com.example.notasxml.databinding.ActivityCrearTareaUsuarioBinding
class CrearTareaUsuario : AppCompatActivity() {

    private val viewModel: NotasViewModel by viewModels()
    private lateinit var binding: ActivityCrearTareaUsuarioBinding
    private lateinit var adapter: TareaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCrearTareaUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TareaAdapter()
        binding.rvItemsTarea.layoutManager = LinearLayoutManager(this)
        binding.rvItemsTarea.adapter = adapter

        // Agregar Item
        binding.btnAgregarItem.setOnClickListener {
            val texto = binding.etNuevoItem.text.toString().trim()
            if (texto.isNotEmpty()) {
                val nuevoItem = ItemTarea(id = 0, notaId = 0, descripcion = texto, completado = false)
                val listaActual = adapter.currentList.toMutableList()
                listaActual.add(nuevoItem)
                adapter.submitList(listaActual)
                binding.etNuevoItem.text.clear()
            }
        }

        binding.btnCancelarTarea.setOnClickListener { finish() }

        binding.btnGuardarTarea.setOnClickListener {
            val titulo = binding.etTituloTarea.text.toString().trim()
            val desc = binding.etDescTarea.text.toString().trim()

            if (titulo.isEmpty()) {
                Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.guardarNota(
                titulo = titulo,
                descripcion = desc,
                tipo = "Tarea",
                items = adapter.currentList,
                idTrabajador = UsuarioHolder.usuario.id
            )
        }

        viewModel.guardadoExitoso.observe(this) { exitoso ->
            if (exitoso) {
                Toast.makeText(this, "Tarea personal guardada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}