package com.example.notasxml.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notasxml.Helpers.NotasHolder
import com.example.notasxml.ViewModels.NotasViewModel
import com.example.notasxml.databinding.FragmentEditarNotaUsuarioBinding

class EditarNotaUsuarioFragment : Fragment() {

    private lateinit var binding: FragmentEditarNotaUsuarioBinding
    private val viewModel: NotasViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditarNotaUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notaActual = NotasHolder.nota
        binding.etTituloNota.setText(notaActual.titulo)
        binding.etDescNota.setText(notaActual.descripcion)

        binding.btnGuardarNota.setOnClickListener {
            val titulo = binding.etTituloNota.text.toString().trim()
            val desc = binding.etDescNota.text.toString().trim()

            if (titulo.isNotEmpty()) {
                viewModel.actualizarNota(
                    id = notaActual.id ?: 0,
                    titulo = titulo,
                    descripcion = desc,
                    tipo = "Nota",
                    items = emptyList()
                )
            } else {
                Toast.makeText(requireContext(), "El título es obligatorio", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancelarNota.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.guardadoExitoso.observe(viewLifecycleOwner) { exitoso ->
            if (exitoso) {
                Toast.makeText(requireContext(), "Nota actualizada", Toast.LENGTH_SHORT).show()
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