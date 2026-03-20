package com.example.notasxml.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Modelos.ItemTarea
import com.example.notasxml.Adaptadores.TareaAdapter
import com.example.notasxml.Helpers.NotasHolder
import com.example.notasxml.ViewModels.NotasViewModel
import com.example.notasxml.databinding.FragmentEditarTareaUsuarioBinding

class EditarTareaUsuarioFragment : Fragment() {

    private lateinit var binding: FragmentEditarTareaUsuarioBinding
    private val viewModel: NotasViewModel by viewModels()
    private lateinit var itemsAdapter: TareaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditarTareaUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notaActual = NotasHolder.nota

        binding.etTituloTarea.setText(notaActual.titulo)
        binding.etDescTarea.setText(notaActual.descripcion)

        itemsAdapter = TareaAdapter()
        binding.rvItemsTarea.layoutManager = LinearLayoutManager(requireContext())
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
            val titulo = binding.etTituloTarea.text.toString().trim()
            val desc = binding.etDescTarea.text.toString().trim()

            if (titulo.isEmpty()) {
                Toast.makeText(requireContext(), "El título es obligatorio", Toast.LENGTH_SHORT).show()
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

        // Botón Cancelar
        binding.btnCancelarTarea.setOnClickListener {
            findNavController().navigateUp()
        }

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.guardadoExitoso.observe(viewLifecycleOwner) { exitoso ->
            if (exitoso) {
                Toast.makeText(requireContext(), "Cambios guardados", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            mensaje?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}