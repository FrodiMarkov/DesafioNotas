package com.example.notasxml

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.NotasUsuarioViewModel
import com.example.notasxml.databinding.FragmentNotasUsuarioBinding

class NotasUsuarioFragment : Fragment() {

    private var _binding: FragmentNotasUsuarioBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotasUsuarioViewModel by viewModels()
    private lateinit var notasAdapter: NotasUsuarioAdapter

    var usuario = UsuarioHolder.usuario

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotasUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupButtons()
        setupObservers()

        // Carga inicial de datos
        viewModel.cargarNotasDelUsuario(usuario.id)
    }

    private fun setupRecyclerView() {
        notasAdapter = NotasUsuarioAdapter(viewModel)
        binding.rvNotasUsuario.apply {
            adapter = notasAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupButtons() {
        // Acción para añadir Nota
        binding.btnAnadirNota.setOnClickListener {
            // Aquí navegarías a tu Fragment de creación de notas
            Toast.makeText(context, "Navegando a Nueva Nota...", Toast.LENGTH_SHORT).show()
        }

        // Acción para añadir Tarea
        binding.btnAnadirTarea.setOnClickListener {
            // Aquí navegarías a tu Fragment de creación de tareas
            Toast.makeText(context, "Navegando a Nueva Tarea...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupObservers() {
        // Observar la lista de notas
        viewModel.notas.observe(viewLifecycleOwner) { lista ->
            notasAdapter.submitList(lista)
        }

        // Observar posibles errores de la API
        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            if (mensaje.isNotEmpty()) {
                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}