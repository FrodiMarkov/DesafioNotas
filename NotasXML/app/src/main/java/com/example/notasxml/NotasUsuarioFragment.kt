package com.example.notasxml

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.NotasViewModel
import com.example.notasxml.databinding.FragmentNotasUsuarioBinding

class NotasUsuarioFragment : Fragment() {

    // Uso de lateinit como pediste para evitar los nulos de _binding
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
            startActivity(Intent(requireContext(), CrearNotaUsuario::class.java))
        }

        binding.btnAnadirTarea.setOnClickListener {
            startActivity(Intent(requireContext(), CrearTareaUsuario::class.java))
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