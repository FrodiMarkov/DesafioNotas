package com.example.notasxml.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController // Importante para NavController
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.NotasViewModel
import com.example.notasxml.databinding.FragmentCrearNotaUsuarioBinding

class CrearNotaUsuarioFragment : Fragment() {

    private lateinit var binding: FragmentCrearNotaUsuarioBinding
    private val viewModel: NotasViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrearNotaUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancelarNota.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnGuardarNota.setOnClickListener {
            val titulo = binding.etTituloNota.text.toString().trim()
            val desc = binding.etDescNota.text.toString().trim()

            if (titulo.isNotEmpty()) {
                viewModel.guardarNota(titulo, desc, "Nota", emptyList(), UsuarioHolder.usuario.id)
            } else {
                Toast.makeText(requireContext(), "Introduce un título", Toast.LENGTH_SHORT).show()
            }
        }

        // OBSERVADORES
        viewModel.guardadoExitoso.observe(viewLifecycleOwner) { exitoso ->
            if (exitoso == true) {
                Toast.makeText(requireContext(), "Nota guardada con éxito", Toast.LENGTH_SHORT).show()
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