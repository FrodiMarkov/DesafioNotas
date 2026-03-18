package com.example.notasxml

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Modelos.ItemTarea
import com.example.notasxml.Adaptadores.TareaAdapter
import com.example.notasxml.Helpers.NotasHolder
import com.example.notasxml.ViewModels.NotasViewModel
import com.example.notasxml.databinding.ActivityEditarTareaUsuarioBinding

class EditarTareaUsuario : AppCompatActivity() {

    lateinit var binding: ActivityEditarTareaUsuarioBinding
    private val viewModel: NotasViewModel by viewModels()
    private lateinit var itemsAdapter: TareaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditarTareaUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val notaActual = NotasHolder.nota
        binding.etTituloTarea.setText(notaActual.titulo)
        binding.etDescTarea.setText(notaActual.descripcion)

        itemsAdapter = TareaAdapter()
        binding.rvItemsTarea.layoutManager = LinearLayoutManager(this)
        binding.rvItemsTarea.adapter = itemsAdapter

        itemsAdapter.submitList(NotasHolder.items.toMutableList())

        binding.btnAgregarItem.setOnClickListener {
            val texto = binding.etNuevoItem.text.toString().trim()
            if (texto.isNotEmpty()) {
                val nuevoItem = ItemTarea(0, notaActual.id ?: 0, texto, false)
                val listaNueva = itemsAdapter.currentList.toMutableList()
                listaNueva.add(nuevoItem)
                itemsAdapter.submitList(listaNueva)
                binding.etNuevoItem.text.clear()
            }
        }

        binding.btnGuardarTarea.setOnClickListener {
            val titulo = binding.etTituloTarea.text.toString()
            val desc = binding.etDescTarea.text.toString()

            if (titulo.isEmpty()) {
                Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.actualizarNota(
                id = notaActual.id ?: 0,
                titulo = titulo,
                descripcion = desc,
                tipo = "Tarea",
                items = itemsAdapter.currentList
            )
        }

        binding.btnCancelarTarea.setOnClickListener {
            finish()
        }

        viewModel.guardadoExitoso.observe(this) { exitoso ->
            if (exitoso) {
                Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}