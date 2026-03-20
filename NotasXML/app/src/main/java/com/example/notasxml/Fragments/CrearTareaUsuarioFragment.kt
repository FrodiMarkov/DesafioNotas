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
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.NotasViewModel
import com.example.notasxml.databinding.FragmentCrearTareaUsuarioBinding

class CrearTareaUsuarioFragment : Fragment() {

    private lateinit var binding: FragmentCrearTareaUsuarioBinding
    private val viewModel: NotasViewModel by viewModels()
    private lateinit var adapter: TareaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrearTareaUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TareaAdapter()
        binding.rvItemsTarea.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItemsTarea.adapter = adapter

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

        binding.btnCancelarTarea.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnGuardarTarea.setOnClickListener {
            val titulo = binding.etTituloTarea.text.toString().trim()
            val desc = binding.etDescTarea.text.toString().trim()

            if (titulo.isEmpty()) {
                Toast.makeText(requireContext(), "El título es obligatorio", Toast.LENGTH_SHORT).show()
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

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.guardadoExitoso.observe(viewLifecycleOwner) { exitoso ->
            if (exitoso) {
                Toast.makeText(requireContext(), "Tarea personal guardada", Toast.LENGTH_SHORT).show()
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