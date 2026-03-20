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
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.NotasUsuarioAdapter
import com.example.notasxml.R
import com.example.notasxml.ViewModels.NotasViewModel
import com.example.notasxml.databinding.FragmentNotasUsuarioBinding

class NotasUsuarioFragment : Fragment() {

    private lateinit var binding: FragmentNotasUsuarioBinding
    private val viewModel: NotasViewModel by viewModels()
    private lateinit var notasAdapter: NotasUsuarioAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNotasUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notasAdapter = NotasUsuarioAdapter(viewModel)
        binding.rvNotasUsuario.apply {
            adapter = notasAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.btnAnadirNota.setOnClickListener {
            findNavController().navigate(R.id.action_notasUsuarioFragment_to_crearNotaUsuarioFragment)
        }

        binding.btnAnadirTarea.setOnClickListener {
            findNavController().navigate(R.id.action_notasUsuarioFragment_to_crearTareaUsuarioFragment)
        }

        viewModel.notas.observe(viewLifecycleOwner) { lista ->
            notasAdapter.submitList(lista?.toList())
        }

        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            mensaje?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val id = UsuarioHolder.usuario.id
        if (id > 0) {
            viewModel.cargarNotasDelUsuario(id)
        }
    }
}